package fr.hugman.universal_ores.client.texture;

/**
 * Conversions between packed ARGB colors and the OKLab perceptual color space.
 * <p>
 * In OKLab, euclidean distances match how different two colors look, {@code L} is the perceived lightness (0 to 1),
 * and {@code a}/{@code b} carry the hue and saturation (0 for greys).
 *
 * @see <a href="https://bottosson.github.io/posts/oklab/">A perceptual color space for image processing</a>
 */
public final class OkLab {
    public static final int L = 0;
    public static final int A = 1;
    public static final int B = 2;

    private static final float[] SRGB_TO_LINEAR = new float[256];

    static {
        for (int i = 0; i < 256; i++) {
            double c = i / 255.0;
            SRGB_TO_LINEAR[i] = (float) (c <= 0.04045 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4));
        }
    }

    private OkLab() {
    }

    public static float[] fromArgb(int argb) {
        double r = SRGB_TO_LINEAR[(argb >> 16) & 0xFF];
        double g = SRGB_TO_LINEAR[(argb >> 8) & 0xFF];
        double b = SRGB_TO_LINEAR[argb & 0xFF];

        double l = Math.cbrt(0.4122214708 * r + 0.5363325363 * g + 0.0514459929 * b);
        double m = Math.cbrt(0.2119034982 * r + 0.6806995451 * g + 0.1073969566 * b);
        double s = Math.cbrt(0.0883024619 * r + 0.2817188376 * g + 0.6299787005 * b);

        return new float[]{
                (float) (0.2104542553 * l + 0.7936177850 * m - 0.0040720468 * s),
                (float) (1.9779984951 * l - 2.4285922050 * m + 0.4505937099 * s),
                (float) (0.0259040371 * l + 0.7827717662 * m - 0.8086757660 * s)
        };
    }

    public static int toArgb(float lightness, float a, float b, int alpha) {
        double l = lightness + 0.3963377774 * a + 0.2158037573 * b;
        double m = lightness - 0.1055613458 * a - 0.0638541728 * b;
        double s = lightness - 0.0894841775 * a - 1.2914855480 * b;
        l = l * l * l;
        m = m * m * m;
        s = s * s * s;

        int red = linearToSrgb(4.0767416621 * l - 3.3077115913 * m + 0.2309699292 * s);
        int green = linearToSrgb(-1.2684380046 * l + 2.6097574011 * m - 0.3413193965 * s);
        int blue = linearToSrgb(-0.0041960863 * l - 0.7034186147 * m + 1.7076147010 * s);
        return (alpha & 0xFF) << 24 | red << 16 | green << 8 | blue;
    }

    public static float chroma(float[] lab) {
        return (float) Math.hypot(lab[A], lab[B]);
    }

    public static float distance(float[] first, float[] second) {
        float dl = first[L] - second[L];
        float da = first[A] - second[A];
        float db = first[B] - second[B];
        return (float) Math.sqrt(dl * dl + da * da + db * db);
    }

    private static int linearToSrgb(double c) {
        c = Math.clamp(c, 0.0, 1.0);
        double srgb = c <= 0.0031308 ? c * 12.92 : 1.055 * Math.pow(c, 1 / 2.4) - 0.055;
        return (int) Math.round(srgb * 255.0);
    }
}
