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
import fr.hugman.universal_ores.client.texture.TextureFrames;
import fr.hugman.universal_ores.variant.OreType;
import fr.hugman.universal_ores.variant.OreVariant;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
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
import java.util.*;
import java.util.function.Supplier;

/**
 * Adds the texture of every registered ore variant, drawn from the textures of its stone and of its original ore.
 * <p>
 * Everything is read from the loaded resource packs, so retextured stones and ores are followed.
 * A variant texture that a resource pack provides itself is used as it is.
 * <p>
 * Animated ores give animated variants, with a frame drawn from each frame of the ore. Otherwise, animated stones give
 * animated variants.
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
        Map<Identifier, Supplier<Optional<OreFrames>>> templates = new HashMap<>();
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

    private static Optional<OreFrames> loadTemplate(ResourceManager resourceManager, OreType type, OreTextureSettings settings) {
        Identifier id = type.block();
        try {
            Texture ore = Texture.read(resourceManager, type.textureOrDefault());
            if (ore == null) {
                LOGGER.error("Can't draw the variants of {}: its texture {} is missing", id, type.textureOrDefault());
                return Optional.empty();
            }
            Optional<AnimationMetadataSection> animation = ore.isAnimated() ? ore.animation() : Optional.empty();
            List<int[]> frames = ore.isAnimated() ? ore.frames() : List.of(ore.firstFrame());
            List<OreTemplate> templates = new ArrayList<>();
            Texture host = Texture.read(resourceManager, type.hostTextureOrDefault());
            if (host == null) {
                LOGGER.warn("The texture {} of the stone {} is found in is missing, so its whole texture is drawn over other stones", type.hostTextureOrDefault(), id);
                frames.forEach(frame -> templates.add(OreTemplate.fromOverlay(ore.width(), ore.height(), frame, settings)));
                return Optional.of(new OreFrames(templates, animation));
            }
            int[] hostPixels = host.firstFrameScaledTo(ore.width(), ore.height());

            Identifier hint = UniversalOres.id("ore_hint/" + type.block().getNamespace() + "/" + type.block().getPath());
            boolean[] oreHint = mask(Texture.read(resourceManager, hint), ore);
            boolean[] haloHint = mask(Texture.read(resourceManager, hint.withSuffix("_halo")), ore);
            for (int[] frame : frames) {
                templates.add(OreTemplate.extract(ore.width(), ore.height(), frame, hostPixels, oreHint, haloHint, settings));
            }
            return Optional.of(new OreFrames(templates, animation));
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.error("Can't draw the variants of {}", id, e);
            return Optional.empty();
        }
    }

    private static boolean @Nullable [] mask(@Nullable Texture hint, Texture texture) {
        if (hint == null || hint.width() != texture.width() || hint.height() != texture.height()) {
            return null;
        }
        int[] pixels = hint.firstFrame();
        boolean[] mask = new boolean[pixels.length];
        for (int i = 0; i < mask.length; i++) {
            mask[i] = (pixels[i] >>> 24) != 0;
        }
        return mask;
    }

    /**
     * The templates of an ore: one per frame of its texture when it is animated.
     */
    private record OreFrames(List<OreTemplate> templates, Optional<AnimationMetadataSection> animation) {
    }

    private record Loader(Identifier sprite, Resource stone, Supplier<Optional<OreFrames>> template, OreTextureSettings settings) implements SpriteSource.DiscardableLoader {
        @Override
        public @Nullable SpriteContents get(@NotNull SpriteResourceLoader loader) {
            Optional<OreFrames> ore = this.template.get();
            if (ore.isEmpty()) {
                return null;
            }
            try {
                Texture stone = Texture.of(this.stone);
                List<int[]> frames = new ArrayList<>();
                Optional<AnimationMetadataSection> animation;
                if (ore.get().animation().isPresent()) {
                    // The ore's animation, drawn on the first frame of the stone
                    for (OreTemplate template : ore.get().templates()) {
                        frames.add(OreTextureGenerator.generate(template, stone.width(), stone.height(), stone.firstFrame(), this.settings));
                    }
                    animation = ore.get().animation();
                } else {
                    OreTemplate template = ore.get().templates().getFirst();
                    for (int[] stoneFrame : stone.isAnimated() ? stone.frames() : List.of(stone.firstFrame())) {
                        frames.add(OreTextureGenerator.generate(template, stone.width(), stone.height(), stoneFrame, this.settings));
                    }
                    animation = stone.isAnimated() ? stone.animation() : Optional.empty();
                }

                int[] pixels = TextureFrames.stack(frames, stone.width(), stone.height());
                NativeImage image = new NativeImage(stone.width(), stone.height() * frames.size(), false);
                for (int i = 0; i < pixels.length; i++) {
                    image.setPixel(i % stone.width(), i / stone.width(), pixels[i]);
                }
                dump(image);
                // The frames are now stacked, at the size of the stone's
                Optional<AnimationMetadataSection> metadata = animation.map(section -> new AnimationMetadataSection(section.frames(), Optional.of(stone.width()), Optional.of(stone.height()), section.defaultFrameTime(), section.interpolatedFrames()));
                return new SpriteContents(this.sprite, new FrameSize(stone.width(), stone.height()), image, metadata, List.of(), Optional.empty());
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
     * The frames of a texture (a single one unless it is animated), with its animation.
     *
     * @param width  the width of a frame
     * @param height the height of a frame
     */
    private record Texture(int width, int height, List<int[]> frames, Optional<AnimationMetadataSection> animation) {
        static @Nullable Texture read(ResourceManager resourceManager, Identifier texture) throws IOException {
            Optional<Resource> resource = resourceManager.getResource(TEXTURE_ID_CONVERTER.idToFile(texture));
            return resource.isEmpty() ? null : of(resource.get());
        }

        static Texture of(Resource resource) throws IOException {
            Optional<AnimationMetadataSection> animation = resource.metadata().getSection(AnimationMetadataSection.TYPE);
            try (InputStream stream = resource.open(); NativeImage image = NativeImage.read(stream)) {
                int width = image.getWidth();
                int height = image.getHeight();
                FrameSize frame = animation.map(section -> section.calculateFrameSize(width, height)).orElse(new FrameSize(width, height));
                return new Texture(frame.width(), frame.height(), TextureFrames.split(image.getPixels(), width, height, frame.width(), frame.height()), animation);
            }
        }

        boolean isAnimated() {
            return this.animation.isPresent() && this.frames.size() > 1;
        }

        int[] firstFrame() {
            return this.frames.getFirst();
        }

        int[] firstFrameScaledTo(int width, int height) {
            int[] pixels = this.firstFrame();
            if (width == this.width && height == this.height) {
                return pixels;
            }
            int[] scaled = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    scaled[y * width + x] = pixels[(y * this.height / height) * this.width + x * this.width / width];
                }
            }
            return scaled;
        }
    }
}
