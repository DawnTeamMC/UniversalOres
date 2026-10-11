package fr.hugman.universal_ores.client.texture.atlas;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.universal_ores.client.texture.atlas.ore.OreTextures;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.LazyLoadedImage;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Adds one sprite per (texture, ore) pair, named {@code <texture>_<ore>}: the texture with the ore drawn over it.
 */
public record OreOverlaySpriteSource(List<Identifier> textures, Map<String, OreTextures> ores) implements SpriteSource {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final MapCodec<OreOverlaySpriteSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.list(Identifier.CODEC).fieldOf("textures").forGetter(OreOverlaySpriteSource::textures),
                    Codec.unboundedMap(Codec.STRING, OreTextures.CODEC).fieldOf("ores").forGetter(OreOverlaySpriteSource::ores)
            ).apply(instance, OreOverlaySpriteSource::new));

    @Override
    public void run(@NotNull ResourceManager resourceManager, @NotNull Output output) {
        Map<String, Supplier<int[]>> orePixels = new HashMap<>();
        Map<String, Supplier<int[]>> shadowPixels = new HashMap<>();
        this.ores.forEach((key, ore) -> {
            orePixels.put(key, Suppliers.memoize(() -> loadPixels(resourceManager, ore.ore())));
            ore.shadow().ifPresent(shadow -> shadowPixels.put(key, Suppliers.memoize(() -> loadPixels(resourceManager, shadow))));
        });

        for (Identifier textureLocation : this.textures) {
            Identifier textureId = TEXTURE_ID_CONVERTER.idToFile(textureLocation);
            Optional<Resource> resource = resourceManager.getResource(textureId);
            if (resource.isEmpty()) {
                LOGGER.warn("Unable to find texture {}", textureId);
                continue;
            }
            LazyLoadedImage baseImage = new LazyLoadedImage(textureId, resource.get(), orePixels.size());
            orePixels.forEach((key, overlay) -> {
                Identifier location = textureLocation.withSuffix("_" + key);
                output.add(location, new OreSpriteSupplier(baseImage, overlay, shadowPixels.get(key), location));
            });
        }
    }

    private static int[] loadPixels(ResourceManager resourceManager, Identifier texture) {
        Optional<Resource> resource = resourceManager.getResource(TEXTURE_ID_CONVERTER.idToFile(texture));
        if (resource.isEmpty()) {
            throw new IllegalArgumentException("Unable to find texture " + texture);
        }
        try (InputStream inputStream = resource.get().open(); NativeImage image = NativeImage.read(inputStream)) {
            return image.getPixels();
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't load texture " + texture, e);
        }
    }

    @Override
    public @NotNull MapCodec<OreOverlaySpriteSource> codec() {
        return MAP_CODEC;
    }

    private record OreSpriteSupplier(
            LazyLoadedImage baseImage,
            Supplier<int[]> overlay,
            @Nullable Supplier<int[]> shadow,
            Identifier location
    ) implements SpriteSource.DiscardableLoader {
        @Override
        public @Nullable SpriteContents get(@NotNull SpriteResourceLoader loader) {
            try {
                NativeImage base = this.baseImage.get();
                int width = base.getWidth();
                int height = base.getHeight();
                int[] pixels = OreTextures.apply(
                        width,
                        height,
                        base.getPixels(),
                        this.overlay.get(),
                        this.shadow == null ? null : this.shadow.get()
                );
                NativeImage image = new NativeImage(width, height, false);
                for (int i = 0; i < pixels.length; i++) {
                    image.setPixel(i % width, i / width, pixels[i]);
                }
                return new SpriteContents(this.location, new FrameSize(width, height), image);
            } catch (IOException | IllegalArgumentException e) {
                LOGGER.error("Unable to apply ore overlay to {}", this.location, e);
                return null;
            } finally {
                this.baseImage.release();
            }
        }

        @Override
        public void discard() {
            this.baseImage.release();
        }
    }
}
