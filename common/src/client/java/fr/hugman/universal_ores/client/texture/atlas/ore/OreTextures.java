package fr.hugman.universal_ores.client.texture.atlas.ore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ColorCode;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import java.awt.*;
import java.util.List;
import java.util.*;

public record OreTextures(
        Identifier ore,
        Optional<Identifier> shadow,
        Optional<ColorCode> glowColor
) {
    public static final Codec<OreTextures> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("ore").forGetter(source -> source.ore),
                    Identifier.CODEC.optionalFieldOf("shadow").forGetter(source -> source.shadow),
                    ColorCode.CODEC.optionalFieldOf("glow_color").forGetter(source -> source.glowColor)
            ).apply(instance, OreTextures::new));

    /**
     * @param overlayShadow shadow values. Between 0 and 255 (dark to bright)
     */
    public static int[] apply(
            int width,
            int height,
            int[] baseTexture,
            int[] overlay,
            int[] overlayShadow
    ) {
        if (overlayShadow == null) {
            return apply(width, height, baseTexture, overlay);
        }
        // get the color palette of the base texture, ordered by lightness
        var baseColorLightnessMap = sortColorsByLightness(baseTexture);
        var shadowLightnessMap = sortColorsByLightness(overlayShadow);
        // these two arrays of integers will essentially act as gradients
        // we will use them to determine the color of the shadow
        // THEY ARE NOT OF THE SAME LENGTH

        int[] result = new int[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int basePixel = baseTexture[y * width + x];
                int overlayPixel = overlay[y * width + x];
                int shadowPixel = overlayShadow[y * width + x];
                if (ColorHelper.getAlpha(shadowPixel) != 0) {
                    var i = shadowLightnessMap.indexOf(shadowPixel) / (float) shadowLightnessMap.size();
                    int index = (int) Math.rint(i * baseColorLightnessMap.size());
                    result[y * width + x] = baseColorLightnessMap.get(index);
                } else if (ColorHelper.getAlpha(overlayPixel) != 0) {
                    result[y * width + x] = overlayPixel;
                } else {
                    result[y * width + x] = basePixel;
                }
            }
        }

        return result;
    }

    public static int[] apply(
            int width,
            int height,
            int[] baseTexture,
            int[] overlay
    ) {
        int[] result = new int[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int basePixel = baseTexture[y * width + x];
                int overlayPixel = overlay[y * width + x];
                if (ColorHelper.getAlpha(overlayPixel) != 0) {
                    result[y * width + x] = overlayPixel;
                } else {
                    result[y * width + x] = basePixel;
                }
            }
        }

        return result;
    }

    public static final Comparator<Integer> LIGHTNESS_COMPARATOR = (a, b) -> {
        float[] hslA = rgbToHSL(a);
        float[] hslB = rgbToHSL(b);
        return Float.compare(hslA[2], hslB[2]);
    };

    private static float[] rgbToHSL(int rgb) {
        Color color = new Color(rgb);
        float[] hsl = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsl);
        return hsl;
    }

    public static List<Integer> sortColorsByLightness(int[] colors) {
        var list = new ArrayList<>(Arrays.stream(colors).distinct().boxed().toList());
        list.sort(LIGHTNESS_COMPARATOR);
        list.remove((Integer) 0); // remove the transparent color
        return list;
    }
}
