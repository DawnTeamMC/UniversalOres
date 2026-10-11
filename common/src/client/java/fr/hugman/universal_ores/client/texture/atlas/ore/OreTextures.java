package fr.hugman.universal_ores.client.texture.atlas.ore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;

import org.jetbrains.annotations.Nullable;
import java.util.*;

public record OreTextures(
        Identifier ore,
        Optional<Identifier> shadow,
        Optional<Integer> glowColor
) {
    public static final Codec<OreTextures> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("ore").forGetter(OreTextures::ore),
                    Identifier.CODEC.optionalFieldOf("shadow").forGetter(OreTextures::shadow),
                    ExtraCodecs.STRING_RGB_COLOR.optionalFieldOf("glow_color").forGetter(OreTextures::glowColor)
            ).apply(instance, OreTextures::new));

    /**
     * @param overlayShadow shadow values. Between 0 and 255 (dark to bright)
     */
    public static int[] apply(
            int width,
            int height,
            int[] baseTexture,
            int[] overlay,
            int @Nullable [] overlayShadow
    ) {
        checkSize(width, height, overlay);
        if (overlayShadow == null) {
            return apply(width, height, baseTexture, overlay);
        }
        checkSize(width, height, overlayShadow);
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
                if (ARGB.alpha(shadowPixel) != 0) {
                    var i = shadowLightnessMap.indexOf(shadowPixel) / (float) shadowLightnessMap.size();
                    int index = Math.min((int) Math.rint(i * baseColorLightnessMap.size()), baseColorLightnessMap.size() - 1);
                    result[y * width + x] = baseColorLightnessMap.get(index);
                } else if (ARGB.alpha(overlayPixel) != 0) {
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
        checkSize(width, height, overlay);
        int[] result = new int[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int basePixel = baseTexture[y * width + x];
                int overlayPixel = overlay[y * width + x];
                if (ARGB.alpha(overlayPixel) != 0) {
                    result[y * width + x] = overlayPixel;
                } else {
                    result[y * width + x] = basePixel;
                }
            }
        }

        return result;
    }

    private static void checkSize(int width, int height, int[] pixels) {
        if (pixels.length != width * height) {
            throw new IllegalArgumentException("Overlay has " + pixels.length + " pixels but the texture is " + width + "x" + height);
        }
    }

    /**
     * Orders colors by their HSB brightness.
     */
    public static final Comparator<Integer> LIGHTNESS_COMPARATOR = Comparator.comparingInt(
            color -> Math.max(ARGB.red(color), Math.max(ARGB.green(color), ARGB.blue(color)))
    );

    public static List<Integer> sortColorsByLightness(int[] colors) {
        var list = new ArrayList<>(Arrays.stream(colors).distinct().boxed().toList());
        list.sort(LIGHTNESS_COMPARATOR);
        list.remove((Integer) 0); // remove the transparent color
        return list;
    }
}
