package fr.hugman.universal_ores.client.texture;

/**
 * Tuning of the ore texture generation. The defaults were fitted against the vanilla ores and the hand-made
 * Universal Ores textures.
 *
 * @param lowPercentile      share of a stone's pixels darker than its "dark end" (halo level 0)
 * @param highPercentile     share of a stone's pixels darker than its "light end" (halo level 1)
 * @param minColorShare      colors used by fewer pixels than this are never picked for the halo
 * @param minTint            smaller halo tints (OKLab a/b distance) are treated as grey
 * @param tintStrength       how much of the halo tint is carried over
 * @param haloRadius         how far from the ore (in pixels) the halo is looked for
 * @param haloMinChange      how much (OKLab) a pixel near the ore must differ from the stone under it to be part of
 *                           the halo: ore textures often redraw their stone slightly, which isn't shading
 * @param haloPatternWeight  how much of the new stone's own pattern shows through the halo (0 redraws the halo from
 *                           its lightness levels alone, 1 shifts the stone pixels under it)
 * @param rampExtension      how many lightness steps beyond a stone's colors still count as that stone
 * @param oreMinDistance     how far (OKLab) from the stone's colors an ore pixel must be
 * @param oreMinChroma       how far from the stone's hue (OKLab a/b distance) an ore pixel must be, unless out of its lightness range
 * @param oreFillNeighbors   a changed pixel with at least this many ore neighbors is ore too
 * @param faintOreShare      when less than this share of the pixels that differ from the stone are found to be ore, the
 *                           ore is looked for again with the two thresholds below
 * @param faintOreChroma     {@code oreMinChroma} when looking again, for ores of a faint hue
 * @param faintOreDistance   {@code oreMinDistance} when looking again
 * @param glowHueShare       the edge pixels of a colorful ore with less than this share of its strongest hue (OKLab a/b
 *                           distance to the stone) are the glow or shadow it casts, not the ore
 * @param glowMaxOutside     ...as long as their lightness is less than this beyond the stone's
 * @param contrastThreshold  dark grey ore pixels are darkened until they are this much darker than the new stone
 * @param contrastStrength   how much of that darkening is applied
 * @param stoneLikeHue       ore pixels closer than this (OKLab a/b distance) to their stone's hue are shades of it
 * @param stoneHueTransfer   how much those shades take the hue of the new stone
 * @param minVisibility      how far (median OKLab distance) an ore must stay from the new stone's colors, unless it was
 *                           already closer to its own stone's colors
 * @param maxVisibilityShift how much the lightness of an ore can be shifted to keep it visible
 * @param embossThreshold    shading strength from which a pixel next to an overlay gets shadowed or lit
 * @param embossDepth        how far beyond the stone's ends that shading goes
 */
public record OreTextureSettings(
        float lowPercentile,
        float highPercentile,
        float minColorShare,
        float minTint,
        float tintStrength,
        int haloRadius,
        float haloMinChange,
        float haloPatternWeight,
        float rampExtension,
        float oreMinDistance,
        float oreMinChroma,
        int oreFillNeighbors,
        float faintOreShare,
        float faintOreChroma,
        float faintOreDistance,
        float glowHueShare,
        float glowMaxOutside,
        float contrastThreshold,
        float contrastStrength,
        float stoneLikeHue,
        float stoneHueTransfer,
        float minVisibility,
        float maxVisibilityShift,
        float embossThreshold,
        float embossDepth
) {
    public static final OreTextureSettings DEFAULT = new OreTextureSettings(
            0.1F, 0.9F,
            0.03F,
            0.01F, 1.0F,
            1, 0.04F, 0.25F,
            1.5F, 0.04F, 0.05F, 6,
            0.125F, 0.012F, 0.01F,
            0.3F, 0.2F,
            0.25F, 0.5F,
            0.03F, 0.5F,
            0.08F, 0.3F,
            0.3F, 0.25F
    );
}
