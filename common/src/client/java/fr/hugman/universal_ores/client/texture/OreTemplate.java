package fr.hugman.universal_ores.client.texture;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * An ore texture taken apart: the ore itself, and the halo of shading it casts on the stone around it.
 * <p>
 * The halo is stored relative to the stone the ore was drawn on, so that it can be redrawn on any other stone:
 * each halo pixel keeps a lightness level (0 is the darker end of the stone, 1 its lighter end, and values outside
 * go beyond) and the tint it adds to the stone (its OKLab {@code a} and {@code b} offsets).
 * That tint is what makes redstone glow red, emerald cast green shadows and diamond sparkle in cyan.
 * Each halo pixel also keeps how much lighter or darker it is than the stone pixel under it, so that the pattern of
 * the new stone can show through.
 * <p>
 * Ore pixels that are only shades of their stone (like the black of coal on grey stone) are marked as such, so that
 * they can take the hue of a new stone.
 */
public final class OreTemplate {
    /**
     * Vanilla ores are shaded as if lit along this direction (x to the right, y downwards): the stone above an ore is
     * darker and the stone below it is lighter. Measured on the vanilla stone and deepslate ores.
     */
    private static final float LIGHT_X = 0.38F;
    private static final float LIGHT_Y = 0.92F;

    private final int width;
    private final int height;
    private final int[] ore;
    private final float[] haloLevel;
    private final float[] haloOffset;
    private final float[] haloA;
    private final float[] haloB;
    private final float[] shadeA;
    private final float[] shadeB;
    private final float hostLightness;
    private final float visibility;

    private OreTemplate(int width, int height, int[] ore, float[] haloLevel, float[] haloOffset, float[] haloA, float[] haloB, float[] shadeA, float[] shadeB, float hostLightness, float visibility) {
        this.width = width;
        this.height = height;
        this.ore = ore;
        this.haloLevel = haloLevel;
        this.haloOffset = haloOffset;
        this.haloA = haloA;
        this.haloB = haloB;
        this.shadeA = shadeA;
        this.shadeB = shadeB;
        this.hostLightness = hostLightness;
        this.visibility = visibility;
    }

    /**
     * Takes apart an ore texture by comparing it with the stone texture it was drawn on.
     *
     * @param orePixels  the ore texture (ARGB)
     * @param hostPixels the stone texture the ore was drawn on, of the same size
     * @param oreHint    optional mask of the ore pixels, used when it fits the texture
     * @param haloHint   optional mask of the halo pixels, used when it fits the texture
     */
    public static OreTemplate extract(int width, int height, int[] orePixels, int[] hostPixels, boolean @Nullable [] oreHint, boolean @Nullable [] haloHint, OreTextureSettings settings) {
        int size = width * height;
        checkSize(size, orePixels, "ore");
        checkSize(size, hostPixels, "host");
        Palette host = Palette.of(hostPixels);
        // Some mods draw their ores as an overlay over their stone: its opaque pixels are the ore
        if (isOverlay(orePixels, hostPixels)) {
            return fromOverlay(width, height, orePixels, host.meanLightness(), settings);
        }

        boolean[] changed = new boolean[size];
        for (int i = 0; i < size; i++) {
            changed[i] = (orePixels[i] >>> 24) != 0 && (orePixels[i] & 0xFFFFFF) != (hostPixels[i] & 0xFFFFFF);
        }

        boolean[] ore;
        if (fits(oreHint, changed, size)) {
            ore = oreHint;
        } else {
            ore = detectOre(width, height, orePixels, changed, host, settings.oreMinChroma(), settings.oreMinDistance(), settings);
            // An ore of a faint hue (like a greenish grey on grey stone) is mostly taken for its stone: look for it again
            // with lower thresholds
            if (count(ore) < settings.faintOreShare() * count(changed)) {
                boolean[] faint = detectOre(width, height, orePixels, changed, host, settings.faintOreChroma(), settings.faintOreDistance(), settings);
                if (count(faint) > count(ore)) {
                    ore = faint;
                }
            }
        }
        boolean[] halo;
        if (fits(haloHint, changed, size)) {
            halo = new boolean[size];
            for (int i = 0; i < size; i++) {
                halo[i] = haloHint[i] && !ore[i];
            }
        } else {
            halo = nearOre(width, height, ore, changed, settings.haloRadius());
            for (int i = 0; i < size; i++) {
                halo[i] = halo[i] && isShaded(OkLab.fromArgb(orePixels[i]), OkLab.fromArgb(hostPixels[i]), settings.haloMinChange());
            }
        }

        float low = host.lightnessPercentile(settings.lowPercentile());
        float high = host.lightnessPercentile(settings.highPercentile());
        float range = Math.max(high - low, 1.0E-3F);
        Palette hostColors = host.significant(settings.minColorShare());

        int[] orePart = new int[size];
        float[] level = nanArray(size);
        float[] offset = nanArray(size);
        float[] tintA = new float[size];
        float[] tintB = new float[size];
        float[] shadeA = nanArray(size);
        float[] shadeB = nanArray(size);
        float[] distances = new float[size];
        int oreCount = 0;
        for (int i = 0; i < size; i++) {
            float[] lab = OkLab.fromArgb(orePixels[i]);
            float[] hostAb = host.abAt(lab[OkLab.L]);
            float da = lab[OkLab.A] - hostAb[0];
            float db = lab[OkLab.B] - hostAb[1];
            if (ore[i]) {
                orePart[i] = orePixels[i];
                distances[oreCount++] = hostColors.distanceToClosest(lab);
                if (Math.hypot(da, db) < settings.stoneLikeHue()) {
                    shadeA[i] = hostAb[0];
                    shadeB[i] = hostAb[1];
                }
            } else if (halo[i]) {
                level[i] = (lab[OkLab.L] - low) / range;
                offset[i] = (lab[OkLab.L] - OkLab.fromArgb(hostPixels[i])[OkLab.L]) / range;
                if (Math.hypot(da, db) >= settings.minTint()) {
                    tintA[i] = da;
                    tintB[i] = db;
                }
            }
        }
        return new OreTemplate(width, height, orePart, level, offset, tintA, tintB, shadeA, shadeB, host.meanLightness(), median(distances, oreCount));
    }

    /**
     * Builds a template from the ore pixels alone (for instance a transparent overlay), shading the stone around
     * them along the vanilla light direction.
     */
    public static OreTemplate fromOverlay(int width, int height, int[] overlayPixels, OreTextureSettings settings) {
        return fromOverlay(width, height, overlayPixels, Float.NaN, settings);
    }

    /**
     * @param hostLightness the mean lightness of the stone the overlay was meant for, if known
     */
    public static OreTemplate fromOverlay(int width, int height, int[] overlayPixels, float hostLightness, OreTextureSettings settings) {
        int size = width * height;
        checkSize(size, overlayPixels, "overlay");
        boolean[] ore = new boolean[size];
        int[] orePart = new int[size];
        for (int i = 0; i < size; i++) {
            if ((overlayPixels[i] >>> 24) != 0) {
                ore[i] = true;
                orePart[i] = overlayPixels[i];
            }
        }
        float[] level = nanArray(size);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * width + x;
                if (ore[i]) {
                    continue;
                }
                // Positive when the ore lies along the light direction, so this pixel is in its shadow
                float emboss = 0;
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if ((dx != 0 || dy != 0) && isSet(ore, width, height, x + dx, y + dy)) {
                            float weight = dx != 0 && dy != 0 ? 0.7F : 1.0F;
                            emboss += weight * (dx * LIGHT_X + dy * LIGHT_Y);
                        }
                    }
                }
                if (emboss > settings.embossThreshold()) {
                    level[i] = -settings.embossDepth();
                } else if (emboss < -settings.embossThreshold()) {
                    level[i] = 1 + settings.embossDepth();
                }
            }
        }
        return new OreTemplate(width, height, orePart, level, nanArray(size), new float[size], new float[size], nanArray(size), nanArray(size), hostLightness, Float.NaN);
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    /**
     * @return the ore color at that pixel, or 0 when the pixel isn't part of the ore
     */
    public int ore(int index) {
        return this.ore[index];
    }

    public boolean isHalo(int index) {
        return !Float.isNaN(this.haloLevel[index]);
    }

    public float haloLevel(int index) {
        return this.haloLevel[index];
    }

    /**
     * How much lighter (positive) or darker the halo pixel is than the stone pixel under it, in the same unit as its
     * level, or {@code NaN} when unknown.
     */
    public float haloOffset(int index) {
        return this.haloOffset[index];
    }

    public float haloTintA(int index) {
        return this.haloA[index];
    }

    public float haloTintB(int index) {
        return this.haloB[index];
    }

    /**
     * Whether that ore pixel is only a shade of its stone (a grey on grey stone), which can take the hue of another stone.
     */
    public boolean isStoneShade(int index) {
        return !Float.isNaN(this.shadeA[index]);
    }

    /**
     * The hue ({@code a} and {@code b}) of the ore's stone at the lightness of that shade.
     */
    public float[] stoneShadeHue(int index) {
        return new float[]{this.shadeA[index], this.shadeB[index]};
    }

    /**
     * The mean lightness of the stone the ore was drawn on, or {@code NaN} when unknown.
     */
    public float hostLightness() {
        return this.hostLightness;
    }

    /**
     * How well the ore stood out on its own stone: the median OKLab distance between its pixels and the closest common
     * color of that stone, or {@code NaN} when unknown.
     */
    public float visibility() {
        return this.visibility;
    }

    /**
     * Whether the ore has any pixel: an ore texture taken for its stone gives variants without ore.
     */
    public boolean hasOre() {
        for (int pixel : this.ore) {
            if (pixel != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean hasHalo() {
        for (float level : this.haloLevel) {
            if (!Float.isNaN(level)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tells the ore apart from its halo by color: ore pixels are far from the stone's own colors, either of another
     * hue or beyond its lightness range. Pixels mostly surrounded by ore are counted as ore too, and the faintly
     * tinted edge of a colorful ore is left to the halo.
     */
    private static boolean[] detectOre(int width, int height, int[] pixels, boolean[] changed, Palette host, float minChroma, float minDistance, OreTextureSettings settings) {
        int size = width * height;
        float step = host.meanStep();
        float lowest = host.darkest() - settings.rampExtension() * step;
        float highest = host.lightest() + settings.rampExtension() * step;

        boolean[] ore = new boolean[size];
        float[] hue = new float[size];
        float[] outside = new float[size];
        for (int i = 0; i < size; i++) {
            if (!changed[i]) {
                continue;
            }
            float[] lab = OkLab.fromArgb(pixels[i]);
            outside[i] = Math.max(Math.max(host.darkest() - lab[OkLab.L], lab[OkLab.L] - host.lightest()), 0);
            // A different hue than the stone at that lightness: more saturated on grey stone, or grey on red netherrack
            float[] hostAb = host.abAt(lab[OkLab.L]);
            hue[i] = (float) Math.hypot(lab[OkLab.A] - hostAb[0], lab[OkLab.B] - hostAb[1]);
            boolean beyond = lab[OkLab.L] < lowest || lab[OkLab.L] > highest;
            ore[i] = (beyond || hue[i] > minChroma) && host.distanceToRamp(lab, settings.rampExtension()) > minDistance;
        }
        for (int pass = 0; pass < 2; pass++) {
            boolean[] grown = ore.clone();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int i = y * width + x;
                    if (ore[i] || !changed[i]) {
                        continue;
                    }
                    int neighbors = 0;
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            if ((dx != 0 || dy != 0) && isSet(ore, width, height, x + dx, y + dy)) {
                                neighbors++;
                            }
                        }
                    }
                    grown[i] = neighbors >= settings.oreFillNeighbors();
                }
            }
            ore = grown;
        }

        // The edge of a colorful ore only tinted by it is the glow or shadow it casts on the stone, like the greens
        // around emerald or the reds around redstone: the halo carries it
        float maxHue = 0;
        for (int i = 0; i < size; i++) {
            if (ore[i]) {
                maxHue = Math.max(maxHue, hue[i]);
            }
        }
        if (maxHue >= minChroma) {
            boolean[] trimmed = ore.clone();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int i = y * width + x;
                    if (ore[i] && isEdge(ore, width, height, x, y) && hue[i] < settings.glowHueShare() * maxHue && outside[i] < settings.glowMaxOutside()) {
                        trimmed[i] = false;
                    }
                }
            }
            ore = trimmed;
        }
        return ore;
    }

    private static int count(boolean[] mask) {
        int count = 0;
        for (boolean set : mask) {
            count += set ? 1 : 0;
        }
        return count;
    }

    private static boolean isEdge(boolean[] mask, int width, int height, int x, int y) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if ((dx != 0 || dy != 0) && !isSet(mask, width, height, x + dx, y + dy)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Whether a pixel differs enough from the stone pixel under it to be shading, rather than the stone redrawn.
     */
    private static boolean isShaded(float[] lab, float[] hostLab, float minChange) {
        return Math.abs(lab[OkLab.L] - hostLab[OkLab.L]) >= minChange
                || Math.hypot(lab[OkLab.A] - hostLab[OkLab.A], lab[OkLab.B] - hostLab[OkLab.B]) >= minChange;
    }

    /**
     * The median of the first {@code count} values, or {@code NaN} when there are none.
     */
    static float median(float[] values, int count) {
        if (count == 0) {
            return Float.NaN;
        }
        float[] sorted = Arrays.copyOf(values, count);
        Arrays.sort(sorted);
        return count % 2 == 1 ? sorted[count / 2] : (sorted[count / 2 - 1] + sorted[count / 2]) / 2;
    }

    private static float[] nanArray(int size) {
        float[] array = new float[size];
        Arrays.fill(array, Float.NaN);
        return array;
    }

    /**
     * Whether the ore texture is only the ore, over a transparent background (at least a tenth of it) where its
     * stone is opaque.
     */
    private static boolean isOverlay(int[] orePixels, int[] hostPixels) {
        int transparent = 0;
        for (int i = 0; i < orePixels.length; i++) {
            if ((orePixels[i] >>> 24) == 0 && (hostPixels[i] >>> 24) != 0) {
                transparent++;
            }
        }
        return transparent >= orePixels.length / 10;
    }

    private static boolean[] nearOre(int width, int height, boolean[] ore, boolean[] changed, int radius) {
        boolean[] halo = new boolean[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * width + x;
                if (ore[i] || !changed[i]) {
                    continue;
                }
                search:
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        if (isSet(ore, width, height, x + dx, y + dy)) {
                            halo[i] = true;
                            break search;
                        }
                    }
                }
            }
        }
        return halo;
    }

    /**
     * A hint fits when it has the texture's size and nearly all of its pixels differ from the stone, so a hint drawn
     * for a texture that a resource pack has since replaced is ignored.
     */
    private static boolean fits(boolean @Nullable [] hint, boolean[] changed, int size) {
        if (hint == null || hint.length != size) {
            return false;
        }
        int set = 0;
        int matching = 0;
        for (int i = 0; i < size; i++) {
            if (hint[i]) {
                set++;
                if (changed[i]) {
                    matching++;
                }
            }
        }
        return set > 0 && matching >= 0.9F * set;
    }

    private static boolean isSet(boolean[] mask, int width, int height, int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height && mask[y * width + x];
    }

    private static void checkSize(int size, int[] pixels, String name) {
        if (pixels.length != size) {
            throw new IllegalArgumentException("The " + name + " texture has " + pixels.length + " pixels instead of " + size);
        }
    }
}
