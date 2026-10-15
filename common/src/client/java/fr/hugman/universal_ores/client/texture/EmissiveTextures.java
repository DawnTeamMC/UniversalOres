package fr.hugman.universal_ores.client.texture;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * Emissive textures in the OptiFine format, which mods like Continuity draw at full brightness over the texture they
 * go with. Resource packs turn them on with {@code assets/minecraft/optifine/emissive.properties}, which names the
 * suffix of emissive textures ({@code suffix.emissive=_e}): {@code diamond_ore_e.png} is the emissive texture of
 * {@code diamond_ore.png}. Only their opaque pixels glow.
 */
public final class EmissiveTextures {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Identifier PROPERTIES = Identifier.withDefaultNamespace("optifine/emissive.properties");

    private EmissiveTextures() {
    }

    /**
     * The suffix of emissive textures, if a resource pack turns them on.
     */
    public static Optional<String> suffix(ResourceManager resourceManager) {
        Optional<Resource> resource = resourceManager.getResource(PROPERTIES);
        if (resource.isEmpty()) {
            return Optional.empty();
        }
        try (InputStream stream = resource.get().open()) {
            Properties properties = new Properties();
            properties.load(stream);
            String suffix = properties.getProperty("suffix.emissive");
            return suffix == null || suffix.isBlank() ? Optional.empty() : Optional.of(suffix.trim());
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.warn("Couldn't read {}", PROPERTIES, e);
            return Optional.empty();
        }
    }

    /**
     * The emissive texture of an ore variant: the emissive pixels of the ore on the ore, and those of the stone
     * elsewhere. Pixels without one are transparent, so they don't glow.
     */
    public static int[] compose(OreTemplate template, int width, int height, int @Nullable [] oreEmissive, int @Nullable [] stoneEmissive) {
        return TextureLayers.compose(template, width, height, oreEmissive, stoneEmissive, 0);
    }
}
