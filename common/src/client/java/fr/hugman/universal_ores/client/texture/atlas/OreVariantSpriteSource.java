package fr.hugman.universal_ores.client.texture.atlas;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.client.texture.OreTemplate;
import fr.hugman.universal_ores.client.texture.OreTextureGenerator;
import fr.hugman.universal_ores.client.texture.OreTextureSettings;
import fr.hugman.universal_ores.client.texture.OreVariantTextures;
import fr.hugman.universal_ores.variant.OreType;
import fr.hugman.universal_ores.variant.OreVariant;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Adds the texture of every registered ore variant, drawn from the textures of its stone and of its original ore.
 * <p>
 * Everything is read from the loaded resource packs, so retextured stones and ores are followed.
 * A variant texture that a resource pack provides itself is used as it is.
 * <p>
 * Set the {@code universal_ores.dumpTextures} system property to a folder to save every generated texture there.
 */
public record OreVariantSpriteSource() implements SpriteSource {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final OreVariantSpriteSource INSTANCE = new OreVariantSpriteSource();
    public static final MapCodec<OreVariantSpriteSource> MAP_CODEC = MapCodec.unit(INSTANCE);
    private static final String DUMP_PROPERTY = "universal_ores.dumpTextures";

    @Override
    public void run(@NotNull ResourceManager resourceManager, @NotNull Output output) {
        OreTextureSettings settings = OreTextureSettings.DEFAULT;
        Map<Identifier, Supplier<Optional<OreTemplate>>> templates = new HashMap<>();
        for (RegisteredOreVariant registered : OreVariantRegistrar.registered()) {
            OreVariant variant = registered.variant();
            var template = templates.computeIfAbsent(variant.oreType().block(), id -> Suppliers.memoize(() -> loadTemplate(resourceManager, variant.oreType(), settings)));
            for (OreVariantTextures.Face face : OreVariantTextures.faces(registered)) {
                if (OreVariantTextures.exists(resourceManager, face.sprite())) {
                    continue;
                }
                Optional<Resource> stone = resourceManager.getResource(TEXTURE_ID_CONVERTER.idToFile(face.stone()));
                if (stone.isEmpty()) {
                    LOGGER.warn("Can't draw {}: its stone texture {} is missing", face.sprite(), face.stone());
                    continue;
                }
                output.add(face.sprite(), new Loader(face.sprite(), stone.get(), template, settings));
            }
        }
    }

    @Override
    public @NotNull MapCodec<OreVariantSpriteSource> codec() {
        return MAP_CODEC;
    }

    private static Optional<OreTemplate> loadTemplate(ResourceManager resourceManager, OreType type, OreTextureSettings settings) {
        Identifier id = type.block();
        try {
            Image ore = Image.read(resourceManager, type.textureOrDefault());
            if (ore == null) {
                LOGGER.error("Can't draw the variants of {}: its texture {} is missing", id, type.textureOrDefault());
                return Optional.empty();
            }
            Image host = Image.read(resourceManager, type.hostTextureOrDefault());
            if (host == null) {
                LOGGER.warn("The texture {} of the stone {} is found in is missing, so its whole texture is drawn over other stones", type.hostTextureOrDefault(), id);
                return Optional.of(OreTemplate.fromOverlay(ore.width, ore.height, ore.pixels, settings));
            }
            host = host.scaledTo(ore.width, ore.height);

            Identifier hint = UniversalOres.id("ore_hint/" + type.block().getNamespace() + "/" + type.block().getPath());
            Image oreHint = Image.read(resourceManager, hint);
            Image haloHint = Image.read(resourceManager, hint.withSuffix("_halo"));
            return Optional.of(OreTemplate.extract(ore.width, ore.height, ore.pixels, host.pixels, mask(oreHint, ore), mask(haloHint, ore), settings));
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.error("Can't draw the variants of {}", id, e);
            return Optional.empty();
        }
    }

    private static boolean @Nullable [] mask(@Nullable Image hint, Image texture) {
        if (hint == null || hint.width != texture.width || hint.height != texture.height) {
            return null;
        }
        boolean[] mask = new boolean[hint.pixels.length];
        for (int i = 0; i < mask.length; i++) {
            mask[i] = (hint.pixels[i] >>> 24) != 0;
        }
        return mask;
    }

    private record Loader(Identifier sprite, Resource stone, Supplier<Optional<OreTemplate>> template, OreTextureSettings settings) implements SpriteSource.DiscardableLoader {
        @Override
        public @Nullable SpriteContents get(@NotNull SpriteResourceLoader loader) {
            Optional<OreTemplate> template = this.template.get();
            if (template.isEmpty()) {
                return null;
            }
            try (InputStream stream = this.stone.open()) {
                Image stone = Image.of(NativeImage.read(stream));
                int[] pixels = OreTextureGenerator.generate(template.get(), stone.width, stone.height, stone.pixels, this.settings);
                NativeImage image = new NativeImage(stone.width, stone.height, false);
                for (int i = 0; i < pixels.length; i++) {
                    image.setPixel(i % stone.width, i / stone.width, pixels[i]);
                }
                dump(image);
                return new SpriteContents(this.sprite, new FrameSize(stone.width, stone.height), image);
            } catch (IOException | IllegalArgumentException e) {
                LOGGER.error("Can't draw {}", this.sprite, e);
                return null;
            }
        }

        private void dump(NativeImage image) {
            String folder = System.getProperty(DUMP_PROPERTY);
            if (folder == null) {
                return;
            }
            Path file = Path.of(folder).resolve(this.sprite.getNamespace()).resolve(this.sprite.getPath() + ".png");
            try {
                Files.createDirectories(file.getParent());
                image.writeToFile(file);
            } catch (IOException e) {
                LOGGER.warn("Couldn't save {} to {}", this.sprite, file, e);
            }
        }
    }

    /**
     * The pixels of a texture's first frame.
     */
    private record Image(int width, int height, int[] pixels) {
        static @Nullable Image read(ResourceManager resourceManager, Identifier texture) throws IOException {
            Optional<Resource> resource = resourceManager.getResource(TEXTURE_ID_CONVERTER.idToFile(texture));
            if (resource.isEmpty()) {
                return null;
            }
            try (InputStream stream = resource.get().open()) {
                return of(NativeImage.read(stream));
            }
        }

        /**
         * Animated textures stack their frames vertically: only the first (square) one is kept.
         */
        static Image of(NativeImage image) {
            try (image) {
                int width = image.getWidth();
                int height = image.getHeight() > width && image.getHeight() % width == 0 ? width : image.getHeight();
                int[] all = image.getPixels();
                int[] pixels = new int[width * height];
                System.arraycopy(all, 0, pixels, 0, pixels.length);
                return new Image(width, height, pixels);
            }
        }

        Image scaledTo(int width, int height) {
            if (width == this.width && height == this.height) {
                return this;
            }
            int[] scaled = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    scaled[y * width + x] = this.pixels[(y * this.height / height) * this.width + x * this.width / width];
                }
            }
            return new Image(width, height, scaled);
        }
    }
}
