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
    private final float[] haloA;
    private final float[] haloB;
    private final float hostLightness;

    private OreTemplate(int width, int height, int[] ore, float[] haloLevel, float[] haloA, float[] haloB, float hostLightness) {
        this.width = width;
        this.height = height;
        this.ore = ore;
        this.haloLevel = haloLevel;
        this.haloA = haloA;
        this.haloB = haloB;
        this.hostLightness = hostLightness;
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

        boolean[] changed = new boolean[size];
        for (int i = 0; i < size; i++) {
            changed[i] = (orePixels[i] >>> 24) != 0 && (orePixels[i] & 0xFFFFFF) != (hostPixels[i] & 0xFFFFFF);
        }

        boolean[] ore = fits(oreHint, changed, size) ? oreHint : detectOre(width, height, orePixels, changed, host, settings);
        boolean[] halo;
        if (fits(haloHint, changed, size)) {
            halo = new boolean[size];
            for (int i = 0; i < size; i++) {
                halo[i] = haloHint[i] && !ore[i];
            }
        } else {
            halo = nearOre(width, height, ore, changed, settings.haloRadius());
        }

        float low = host.lightnessPercentile(settings.lowPercentile());
        float high = host.lightnessPercentile(settings.highPercentile());
        float range = Math.max(high - low, 1.0E-3F);

        int[] orePart = new int[size];
        float[] level = new float[size];
        float[] tintA = new float[size];
        float[] tintB = new float[size];
        Arrays.fill(level, Float.NaN);
        for (int i = 0; i < size; i++) {
            if (ore[i]) {
                orePart[i] = orePixels[i];
            } else if (halo[i]) {
                float[] lab = OkLab.fromArgb(orePixels[i]);
                level[i] = (lab[OkLab.L] - low) / range;
                float[] hostAb = host.abAt(lab[OkLab.L]);
                float da = lab[OkLab.A] - hostAb[0];
                float db = lab[OkLab.B] - hostAb[1];
                if (Math.hypot(da, db) >= settings.minTint()) {
                    tintA[i] = da;
                    tintB[i] = db;
                }
            }
        }
        return new OreTemplate(width, height, orePart, level, tintA, tintB, host.meanLightness());
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
        float[] level = new float[size];
        Arrays.fill(level, Float.NaN);
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
        return new OreTemplate(width, height, orePart, level, new float[size], new float[size], hostLightness);
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

    public float haloTintA(int index) {
        return this.haloA[index];
    }

    public float haloTintB(int index) {
        return this.haloB[index];
    }

    /**
     * The mean lightness of the stone the ore was drawn on, or {@code NaN} when unknown.
     */
    public float hostLightness() {
        return this.hostLightness;
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
     * hue or beyond its lightness range. Pixels mostly surrounded by ore are counted as ore too.
     */
    private static boolean[] detectOre(int width, int height, int[] pixels, boolean[] changed, Palette host, OreTextureSettings settings) {
        int size = width * height;
        float step = host.meanStep();
        float lowest = host.darkest() - settings.rampExtension() * step;
        float highest = host.lightest() + settings.rampExtension() * step;

        boolean[] ore = new boolean[size];
        for (int i = 0; i < size; i++) {
            if (!changed[i]) {
                continue;
            }
            float[] lab = OkLab.fromArgb(pixels[i]);
            boolean outside = lab[OkLab.L] < lowest || lab[OkLab.L] > highest;
            // A different hue than the stone at that lightness: more saturated on grey stone, or grey on red netherrack
            float[] hostAb = host.abAt(lab[OkLab.L]);
            boolean colorful = Math.hypot(lab[OkLab.A] - hostAb[0], lab[OkLab.B] - hostAb[1]) > settings.oreMinChroma();
            ore[i] = (outside || colorful) && host.distanceToRamp(lab, settings.rampExtension()) > settings.oreMinDistance();
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
        return ore;
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
