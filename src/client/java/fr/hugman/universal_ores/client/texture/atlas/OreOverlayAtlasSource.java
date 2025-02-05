package fr.hugman.universal_ores.client.texture.atlas;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.universal_ores.client.texture.atlas.ore.OreTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteDimensions;
import net.minecraft.client.texture.SpriteOpener;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.AtlasSourceType;
import net.minecraft.client.texture.atlas.AtlasSprite;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.metadata.ResourceMetadata;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class OreOverlayAtlasSource implements AtlasSource {
    static final Logger LOGGER = LogUtils.getLogger();

    public static final MapCodec<OreOverlayAtlasSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.list(Identifier.CODEC).fieldOf("textures").forGetter(source -> source.textures),
                    Codec.unboundedMap(Codec.STRING, OreTextures.CODEC).fieldOf("ores").forGetter(source -> source.ores)
            ).apply(instance, OreOverlayAtlasSource::new));
    private final List<Identifier> textures;
    private final Map<String, OreTextures> ores;

    public OreOverlayAtlasSource(List<Identifier> textures, Map<String, OreTextures> ores) {
        this.textures = textures;
        this.ores = ores;
    }

    @Override
    public void load(ResourceManager resourceManager, SpriteRegions regions) {
        Map<String, int[]> orePixelMap = new HashMap<>();
        Map<String, int[]> shadowPixelMap = new HashMap<>();
        this.ores.forEach((key, overlay) -> {
            orePixelMap.put(key, open(resourceManager, overlay.ore()));
            overlay.shadow().ifPresent(shadow -> shadowPixelMap.put(key, open(resourceManager, shadow)));
        });

        for (Identifier identifier : this.textures) {
            Identifier fullPath = RESOURCE_FINDER.toResourcePath(identifier);
            Optional<Resource> optional = resourceManager.getResource(fullPath);
            if (optional.isEmpty()) {
                LOGGER.warn("Unable to find resource {}", fullPath);
            } else {
                AtlasSprite atlasSprite = new AtlasSprite(fullPath, optional.get(), orePixelMap.size());

                for (Map.Entry<String, int[]> orePixels : orePixelMap.entrySet()) {
                    Identifier location = identifier.withSuffixedPath("_" + orePixels.getKey());
                    int[] shadow = null;
                    if (shadowPixelMap.containsKey(orePixels.getKey())) {
                        shadow = shadowPixelMap.get(orePixels.getKey());
                    }
                    regions.add(location, new OreSpriteRegion(atlasSprite, orePixels.getValue(), shadow, location));
                }
            }
        }

    }

    private static int[] open(ResourceManager resourceManager, Identifier texture) {
        Optional<Resource> optional = resourceManager.getResource(RESOURCE_FINDER.toResourcePath(texture));
        if (optional.isEmpty()) {
            LOGGER.error("Failed pixels load image {}", texture);
            throw new IllegalArgumentException();
        }
        try {
            InputStream inputStream = optional.get().getInputStream();

            int[] var5;
            try (NativeImage nativeImage = NativeImage.read(inputStream)) {
                var5 = nativeImage.copyPixelsArgb();
            } catch (Throwable var10) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable var7) {
                        var10.addSuppressed(var7);
                    }
                }

                throw var10;
            }

            if (inputStream != null) {
                inputStream.close();
            }

            return var5;
        } catch (Exception var11) {
            LOGGER.error("Couldn't load image {}", texture, var11);
            throw new IllegalArgumentException();
        }

    }

    @Override
    public AtlasSourceType getType() {
        return UniversalOresAtlasSources.ORE_OVERLAY;
    }

    @Environment(EnvType.CLIENT)
    record OreSpriteRegion(
            AtlasSprite baseImage,
            int[] overlay,
            int @Nullable [] shadow,
            Identifier location
    ) implements AtlasSource.SpriteRegion {
        @Nullable
        public SpriteContents apply(SpriteOpener spriteOpener) {
            try {
                NativeImage nativeImage = this.baseImage.read();
                NativeImage overlayImage = new NativeImage(nativeImage.getWidth(), nativeImage.getHeight(), true);
                int[] pixels = nativeImage.copyPixelsArgb();
                int[] newPixels = OreTextures.apply(
                        nativeImage.getWidth(),
                        nativeImage.getHeight(),
                        pixels,
                        this.overlay,
                        this.shadow
                );
                for (int i = 0; i < newPixels.length; i++) {
                    overlayImage.setColorArgb(i % nativeImage.getWidth(), i / nativeImage.getWidth(), newPixels[i]);
                }
                return new SpriteContents(
                        this.location,
                        new SpriteDimensions(nativeImage.getWidth(), nativeImage.getHeight()),
                        overlayImage,
                        ResourceMetadata.NONE
                );
            } catch (IllegalArgumentException | IOException var7) {
                LOGGER.error("Unable pixels apply overlay pixels {}", this.location, var7);
            } finally {
                this.baseImage.close();
            }
            return null;
        }

        @Override
        public void close() {
            this.baseImage.close();
        }
    }
}
