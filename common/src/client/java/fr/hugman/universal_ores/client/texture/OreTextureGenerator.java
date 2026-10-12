package fr.hugman.universal_ores.client.texture;

/**
 * Draws an {@link OreTemplate} on a stone texture.
 * <p>
 * The halo is redrawn with the stone's own colors: each halo pixel takes the stone color whose lightness sits at the
 * same level between the stone's dark and light ends, then gets the ore's tint. Rare colors of the stone are skipped,
 * so that a speck doesn't end up all around the ore.
 */
public final class OreTextureGenerator {
    private OreTextureGenerator() {
    }

    /**
     * @param stonePixels the stone texture (ARGB), of any size: the template is scaled to it
     * @return the ore texture, of the stone texture's size
     */
    public static int[] generate(OreTemplate template, int width, int height, int[] stonePixels, OreTextureSettings settings) {
        if (stonePixels.length != width * height) {
            throw new IllegalArgumentException("The stone texture has " + stonePixels.length + " pixels instead of " + width * height);
        }
        Palette stone = Palette.of(stonePixels);
        Palette candidates = stone.significant(settings.minColorShare());
        float low = stone.lightnessPercentile(settings.lowPercentile());
        float high = stone.lightnessPercentile(settings.highPercentile());
        float meanLightness = stone.meanLightness();

        int[] result = stonePixels.clone();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * width + x;
                // Nearest neighbor, so that a 16x ore fits a 32x stone
                int t = (y * template.height() / height) * template.width() + (x * template.width() / width);
                int ore = template.ore(t);
                if (ore != 0) {
                    result[i] = adaptContrast(ore, template.hostLightness(), meanLightness, settings);
                } else if (template.isHalo(t)) {
                    float lightness = low + template.haloLevel(t) * (high - low);
                    float[] lab = candidates.lab(candidates.closestIndex(lightness));
                    float a = lab[OkLab.A] + settings.tintStrength() * template.haloTintA(t);
                    float b = lab[OkLab.B] + settings.tintStrength() * template.haloTintB(t);
                    result[i] = OkLab.toArgb(lab[OkLab.L], a, b, stonePixels[i] >>> 24);
                }
            }
        }
        return result;
    }

    /**
     * Dark greys rely on the stone being lighter than them: coal would vanish on a dark stone. Like Mojang does for
     * deepslate, they get darker when the new stone takes away the contrast they had on their own stone (up to
     * {@link OreTextureSettings#contrastThreshold()}). Colorful ore pixels are left alone.
     */
    private static int adaptContrast(int color, float hostLightness, float stoneLightness, OreTextureSettings settings) {
        float[] lab = OkLab.fromArgb(color);
        if (OkLab.chroma(lab) > 0.03F || lab[OkLab.L] >= stoneLightness) {
            return color;
        }
        float wanted = Float.isNaN(hostLightness) ? settings.contrastThreshold() : Math.min(hostLightness - lab[OkLab.L], settings.contrastThreshold());
        float contrast = stoneLightness - lab[OkLab.L];
        if (contrast >= wanted) {
            return color;
        }
        float lightness = Math.max(lab[OkLab.L] - (wanted - contrast) * settings.contrastStrength(), 0.05F);
        return OkLab.toArgb(lightness, lab[OkLab.A], lab[OkLab.B], color >>> 24);
    }
}
