package fr.hugman.universal_ores.client.texture;

import org.jetbrains.annotations.Nullable;

/**
 * The extra textures of the <a href="https://shaderlabs.org/wiki/LabPBR_Materials">LabPBR</a> format, that PBR
 * resource packs give blocks for shaders (as {@code <texture>_n.png} and {@code <texture>_s.png}).
 * <p>
 * An ore variant's map is put together like its texture: the ore pixels take the map of the original ore, and the
 * rest (the halo included, since it is redrawn from the stone) takes the map of the stone. Where one of them has no
 * map, the value shader mods use for textures without maps is used, so that the stone part of a variant looks like
 * the stone itself.
 */
public enum PbrMap {
    /**
     * Normals (red and green), ambient occlusion (blue) and height (alpha). Neutral: flat, unoccluded, at the top.
     */
    NORMAL("_n", 0xFF7F7FFF),
    /**
     * Smoothness (red), reflectance or metal (green), porosity or subsurface scattering (blue) and emission (alpha).
     * Neutral: everything at zero.
     */
    SPECULAR("_s", 0x00000000);

    private final String suffix;
    private final int neutral;

    PbrMap(String suffix, int neutral) {
        this.suffix = suffix;
        this.neutral = neutral;
    }

    /**
     * The suffix of the texture names of this map.
     */
    public String suffix() {
        return this.suffix;
    }

    /**
     * The value (ARGB) of a pixel without anything special: the one Iris uses for textures without this map.
     */
    public int neutral() {
        return this.neutral;
    }

    /**
     * Puts together the map of an ore variant.
     *
     * @param oreMap   the map of the original ore, at the template's size, if it has one
     * @param stoneMap the map of the stone, at the given size, if it has one
     * @return the map of the variant, at the given size
     */
    public int[] compose(OreTemplate template, int width, int height, int @Nullable [] oreMap, int @Nullable [] stoneMap) {
        return TextureLayers.compose(template, width, height, oreMap, stoneMap, this.neutral);
    }
}
