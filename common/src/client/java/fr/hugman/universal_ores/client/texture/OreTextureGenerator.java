package fr.hugman.universal_ores.client.texture;

/**
 * Draws an {@link OreTemplate} on a stone texture.
 * <p>
 * The halo is redrawn with the stone's own colors: each halo pixel takes the stone color whose lightness sits at the
 * same level between the stone's dark and light ends (mixed with the stone pixel under it, shifted like the original
 * halo pixel was), then gets the ore's tint. Rare colors of the stone are skipped, so that a speck doesn't end up all
 * around the ore.
 * <p>
 * The ore keeps its colors, except where the new stone would hide it: its shades of stone take the new stone's hue,
 * dark greys get darker on darker stones, and an ore that would blend in is made lighter or darker as a whole.
 */
public final class OreTextureGenerator {
    private static final float SHIFT_STEP = 0.01F;

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
        int[] ore = oreColors(template, stone, candidates, settings);

        int[] result = stonePixels.clone();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * width + x;
                // Nearest neighbor, so that a 16x ore fits a 32x stone
                int t = (y * template.height() / height) * template.width() + (x * template.width() / width);
                if (ore[t] != 0) {
                    result[i] = ore[t];
                } else if (template.isHalo(t)) {
                    float lightness = low + template.haloLevel(t) * (high - low);
                    float offset = template.haloOffset(t);
                    if (!Float.isNaN(offset)) {
                        float shifted = OkLab.fromArgb(stonePixels[i])[OkLab.L] + offset * (high - low);
                        lightness += settings.haloPatternWeight() * (shifted - lightness);
                    }
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
     * The ore's colors on the new stone, at the template's size (0 where there is no ore).
     */
    private static int[] oreColors(OreTemplate template, Palette stone, Palette candidates, OreTextureSettings settings) {
        int size = template.width() * template.height();
        float stoneLightness = stone.meanLightness();
        int[] colors = new int[size];
        float[][] labs = new float[size][];
        boolean[] changed = new boolean[size];
        for (int t = 0; t < size; t++) {
            colors[t] = template.ore(t);
            if (colors[t] == 0) {
                continue;
            }
            float[] original = OkLab.fromArgb(colors[t]);
            float[] lab = adaptContrast(original, template.hostLightness(), stoneLightness, settings);
            changed[t] = lab != original;
            if (template.isStoneShade(t) && settings.stoneHueTransfer() != 0) {
                // Coal on netherrack gets a reddish black: the shade moves from the hue of its stone to the new one's
                float[] from = template.stoneShadeHue(t);
                float[] to = stone.abAt(lab[OkLab.L]);
                lab = new float[]{lab[OkLab.L], lab[OkLab.A] + settings.stoneHueTransfer() * (to[0] - from[0]), lab[OkLab.B] + settings.stoneHueTransfer() * (to[1] - from[1])};
                changed[t] = true;
            }
            labs[t] = lab;
        }

        float shift = visibilityShift(labs, template.visibility(), stoneLightness, candidates, settings);
        for (int t = 0; t < size; t++) {
            float[] lab = labs[t];
            // Untouched pixels keep their exact color
            if (lab != null && (changed[t] || shift != 0)) {
                colors[t] = OkLab.toArgb(clampLightness(lab[OkLab.L] + shift), lab[OkLab.A], lab[OkLab.B], colors[t] >>> 24);
            }
        }
        return colors;
    }

    /**
     * Dark greys rely on the stone being lighter than them: coal would vanish on a dark stone. Like Mojang does for
     * deepslate, they get darker when the new stone takes away the contrast they had on their own stone (up to
     * {@link OreTextureSettings#contrastThreshold()}). Colorful ore pixels are left alone.
     */
    private static float[] adaptContrast(float[] lab, float hostLightness, float stoneLightness, OreTextureSettings settings) {
        if (OkLab.chroma(lab) > 0.03F || lab[OkLab.L] >= stoneLightness) {
            return lab;
        }
        float wanted = Float.isNaN(hostLightness) ? settings.contrastThreshold() : Math.min(hostLightness - lab[OkLab.L], settings.contrastThreshold());
        float contrast = stoneLightness - lab[OkLab.L];
        if (contrast >= wanted) {
            return lab;
        }
        float lightness = Math.max(lab[OkLab.L] - (wanted - contrast) * settings.contrastStrength(), 0.05F);
        return new float[]{lightness, lab[OkLab.A], lab[OkLab.B]};
    }

    /**
     * How much lighter (positive) or darker the whole ore must be to stand out from the new stone as well as it did
     * from its own one (up to {@link OreTextureSettings#minVisibility()}): tin on diorite gets darker, coal on
     * blackstone lighter. Shifting the whole ore keeps its own shading.
     */
    private static float visibilityShift(float[][] labs, float hostVisibility, float stoneLightness, Palette candidates, OreTextureSettings settings) {
        float wanted = Float.isNaN(hostVisibility) ? settings.minVisibility() : Math.min(hostVisibility, settings.minVisibility());
        float best = 0;
        float bestVisibility = visibility(labs, 0, candidates);
        if (Float.isNaN(bestVisibility) || bestVisibility >= wanted) {
            return 0;
        }
        // Away from the stone first: an ore lighter than the stone gets lighter
        float direction = meanLightness(labs) >= stoneLightness ? 1 : -1;
        for (float step = SHIFT_STEP; step <= settings.maxVisibilityShift() + 1.0E-4F; step += SHIFT_STEP) {
            for (float shift : new float[]{direction * step, -direction * step}) {
                float visibility = visibility(labs, shift, candidates);
                if (visibility >= wanted) {
                    return shift;
                }
                if (visibility > bestVisibility) {
                    bestVisibility = visibility;
                    best = shift;
                }
            }
        }
        return best;
    }

    /**
     * The median OKLab distance between the ore pixels (with their lightness shifted) and the closest stone color.
     * The median, so that a dark outline doesn't make up for the rest of the ore blending in.
     */
    private static float visibility(float[][] labs, float shift, Palette stone) {
        float[] distances = new float[labs.length];
        int count = 0;
        for (float[] lab : labs) {
            if (lab != null) {
                distances[count++] = stone.distanceToClosest(new float[]{clampLightness(lab[OkLab.L] + shift), lab[OkLab.A], lab[OkLab.B]});
            }
        }
        return OreTemplate.median(distances, count);
    }

    private static float meanLightness(float[][] labs) {
        float sum = 0;
        int count = 0;
        for (float[] lab : labs) {
            if (lab != null) {
                sum += lab[OkLab.L];
                count++;
            }
        }
        return count == 0 ? 0 : sum / count;
    }

    private static float clampLightness(float lightness) {
        return Math.clamp(lightness, 0.0F, 1.0F);
    }
}
