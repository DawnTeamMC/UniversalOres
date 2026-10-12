package fr.hugman.universal_ores.client.texture;

/**
 * Tuning of the ore texture generation. The defaults were fitted against the vanilla ores and the hand-made
 * Universal Ores textures.
 *
 * @param lowPercentile     share of a stone's pixels darker than its "dark end" (halo level 0)
 * @param highPercentile    share of a stone's pixels darker than its "light end" (halo level 1)
 * @param minColorShare     colors used by fewer pixels than this are never picked for the halo
 * @param minTint           smaller halo tints (OKLab a/b distance) are treated as grey
 * @param tintStrength      how much of the halo tint is carried over
 * @param haloRadius        how far from the ore (in pixels) the halo is looked for
 * @param rampExtension     how many lightness steps beyond a stone's colors still count as that stone
 * @param oreMinDistance    how far (OKLab) from the stone's colors an ore pixel must be
 * @param oreMinChroma      how much more saturated than the stone an ore pixel must be, unless out of its lightness range
 * @param oreFillNeighbors  a changed pixel with at least this many ore neighbors is ore too
 * @param contrastThreshold dark grey ore pixels are darkened until they are this much darker than the new stone
 * @param contrastStrength  how much of that darkening is applied
 * @param embossThreshold   shading strength from which a pixel next to an overlay gets shadowed or lit
 * @param embossDepth       how far beyond the stone's ends that shading goes
 */
public record OreTextureSettings(
        float lowPercentile,
        float highPercentile,
        float minColorShare,
        float minTint,
        float tintStrength,
        int haloRadius,
        float rampExtension,
        float oreMinDistance,
        float oreMinChroma,
        int oreFillNeighbors,
        float contrastThreshold,
        float contrastStrength,
        float embossThreshold,
        float embossDepth
) {
    public static final OreTextureSettings DEFAULT = new OreTextureSettings(
            0.1F, 0.9F,
            0.03F,
            0.01F, 1.0F,
            1,
            1.5F, 0.04F, 0.05F, 6,
            0.25F, 0.5F,
            0.3F, 0.25F
    );
}
