package fr.hugman.universal_ores.client.texture;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

import java.util.Arrays;
import java.util.Comparator;

/**
 * The colors of a texture, sorted from darkest to lightest, with how many pixels use each of them.
 */
public final class Palette {
    private final int[] colors;
    private final float[][] labs;
    private final int[] counts;
    private final int total;

    private Palette(int[] colors, float[][] labs, int[] counts) {
        this.colors = colors;
        this.labs = labs;
        this.counts = counts;
        this.total = Arrays.stream(counts).sum();
    }

    /**
     * Fully transparent pixels are ignored.
     */
    public static Palette of(int[] pixels) {
        Int2IntOpenHashMap counts = new Int2IntOpenHashMap();
        for (int pixel : pixels) {
            if ((pixel >>> 24) != 0) {
                counts.addTo(pixel | 0xFF000000, 1);
            }
        }
        int[] colors = counts.keySet().toIntArray();
        float[][] labs = new float[colors.length][];
        Integer[] order = new Integer[colors.length];
        for (int i = 0; i < colors.length; i++) {
            labs[i] = OkLab.fromArgb(colors[i]);
            order[i] = i;
        }
        Arrays.sort(order, Comparator.comparingDouble(i -> labs[i][OkLab.L]));

        int[] sortedColors = new int[colors.length];
        float[][] sortedLabs = new float[colors.length][];
        int[] sortedCounts = new int[colors.length];
        for (int i = 0; i < order.length; i++) {
            sortedColors[i] = colors[order[i]];
            sortedLabs[i] = labs[order[i]];
            sortedCounts[i] = counts.get(colors[order[i]]);
        }
        return new Palette(sortedColors, sortedLabs, sortedCounts);
    }

    public int size() {
        return this.colors.length;
    }

    public boolean isEmpty() {
        return this.colors.length == 0;
    }

    public float[] lab(int index) {
        return this.labs[index];
    }

    /**
     * The colors used by at least {@code minShare} of the pixels, so that rare specks don't stand for the whole texture.
     * Falls back to every color when none is that common.
     */
    public Palette significant(float minShare) {
        int kept = 0;
        for (int count : this.counts) {
            if (count >= minShare * this.total) {
                kept++;
            }
        }
        if (kept == 0 || kept == this.colors.length) {
            return this;
        }
        int[] colors = new int[kept];
        float[][] labs = new float[kept][];
        int[] counts = new int[kept];
        int j = 0;
        for (int i = 0; i < this.colors.length; i++) {
            if (this.counts[i] >= minShare * this.total) {
                colors[j] = this.colors[i];
                labs[j] = this.labs[i];
                counts[j] = this.counts[i];
                j++;
            }
        }
        return new Palette(colors, labs, counts);
    }

    /**
     * The lightness below which {@code fraction} of the pixels are, interpolated between the colors.
     */
    public float lightnessPercentile(float fraction) {
        if (this.isEmpty()) {
            return 0.5F;
        }
        float cumulative = 0;
        float previousMid = Float.NaN;
        for (int i = 0; i < this.colors.length; i++) {
            float share = (float) this.counts[i] / this.total;
            float mid = cumulative + share / 2;
            if (fraction <= mid) {
                if (i == 0) {
                    return this.labs[0][OkLab.L];
                }
                float t = (fraction - previousMid) / (mid - previousMid);
                return lerp(t, this.labs[i - 1][OkLab.L], this.labs[i][OkLab.L]);
            }
            cumulative += share;
            previousMid = mid;
        }
        return this.labs[this.colors.length - 1][OkLab.L];
    }

    public float meanLightness() {
        double sum = 0;
        for (int i = 0; i < this.colors.length; i++) {
            sum += (double) this.labs[i][OkLab.L] * this.counts[i];
        }
        return this.total == 0 ? 0.5F : (float) (sum / this.total);
    }

    public float meanChroma() {
        double sum = 0;
        for (int i = 0; i < this.colors.length; i++) {
            sum += (double) OkLab.chroma(this.labs[i]) * this.counts[i];
        }
        return this.total == 0 ? 0 : (float) (sum / this.total);
    }

    /**
     * The mean lightness gap between two successive colors.
     */
    public float meanStep() {
        if (this.colors.length < 2) {
            return 0.05F;
        }
        return (this.labs[this.colors.length - 1][OkLab.L] - this.labs[0][OkLab.L]) / (this.colors.length - 1);
    }

    public float darkest() {
        return this.labs[0][OkLab.L];
    }

    public float lightest() {
        return this.labs[this.colors.length - 1][OkLab.L];
    }

    /**
     * The hue of the texture at a given lightness: its {@code a} and {@code b}, interpolated between the colors.
     */
    public float[] abAt(float lightness) {
        if (this.isEmpty()) {
            return new float[]{0, 0};
        }
        if (lightness <= this.labs[0][OkLab.L]) {
            return new float[]{this.labs[0][OkLab.A], this.labs[0][OkLab.B]};
        }
        for (int i = 1; i < this.colors.length; i++) {
            float[] high = this.labs[i];
            if (lightness <= high[OkLab.L]) {
                float[] low = this.labs[i - 1];
                float span = high[OkLab.L] - low[OkLab.L];
                float t = span <= 1.0E-6F ? 0 : (lightness - low[OkLab.L]) / span;
                return new float[]{lerp(t, low[OkLab.A], high[OkLab.A]), lerp(t, low[OkLab.B], high[OkLab.B])};
            }
        }
        float[] last = this.labs[this.colors.length - 1];
        return new float[]{last[OkLab.A], last[OkLab.B]};
    }

    /**
     * The color whose lightness is the closest to {@code lightness}.
     */
    public int closestIndex(float lightness) {
        int best = 0;
        float bestDistance = Float.MAX_VALUE;
        for (int i = 0; i < this.colors.length; i++) {
            float distance = Math.abs(this.labs[i][OkLab.L] - lightness);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = i;
            }
        }
        return best;
    }

    /**
     * How far a color is from the texture's lightness ramp: the polyline joining its colors from darkest to lightest,
     * stretched by {@code extension} steps at both ends.
     */
    public float distanceToRamp(float[] lab, float extension) {
        if (this.isEmpty()) {
            return Float.MAX_VALUE;
        }
        float stretch = extension * this.meanStep();
        float[] start = this.labs[0].clone();
        start[OkLab.L] -= stretch;
        float[] end = this.labs[this.colors.length - 1].clone();
        end[OkLab.L] += stretch;

        float best = distanceToSegment(lab, start, this.labs[0]);
        for (int i = 1; i < this.colors.length; i++) {
            best = Math.min(best, distanceToSegment(lab, this.labs[i - 1], this.labs[i]));
        }
        return Math.min(best, distanceToSegment(lab, this.labs[this.colors.length - 1], end));
    }

    private static float distanceToSegment(float[] point, float[] from, float[] to) {
        float dl = to[0] - from[0], da = to[1] - from[1], db = to[2] - from[2];
        float lengthSquared = dl * dl + da * da + db * db;
        float t = 0;
        if (lengthSquared > 1.0E-9F) {
            t = ((point[0] - from[0]) * dl + (point[1] - from[1]) * da + (point[2] - from[2]) * db) / lengthSquared;
            t = Math.clamp(t, 0, 1);
        }
        float pl = point[0] - (from[0] + t * dl);
        float pa = point[1] - (from[1] + t * da);
        float pb = point[2] - (from[2] + t * db);
        return (float) Math.sqrt(pl * pl + pa * pa + pb * pb);
    }

    private static float lerp(float t, float from, float to) {
        return from + t * (to - from);
    }
}
