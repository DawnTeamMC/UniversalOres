package fr.hugman.universal_ores.client.texture;

import org.jetbrains.annotations.Nullable;

/**
 * Puts together the extra textures of an ore variant (PBR maps, emissive textures...) like its texture: the ore pixels
 * take the one of the original ore, and the rest (the halo included, since it is redrawn from the stone) takes the one
 * of the stone.
 */
public final class TextureLayers {
    private TextureLayers() {
    }

    /**
     * @param oreLayer   the extra texture of the original ore, at the template's size, if it has one
     * @param stoneLayer the extra texture of the stone, at the given size, if it has one
     * @param neutral    the value (ARGB) used where the ore or the stone has no such texture
     * @return the extra texture of the variant, at the given size
     */
    public static int[] compose(OreTemplate template, int width, int height, int @Nullable [] oreLayer, int @Nullable [] stoneLayer, int neutral) {
        if (oreLayer != null && oreLayer.length != template.width() * template.height()) {
            throw new IllegalArgumentException("The ore layer has " + oreLayer.length + " pixels instead of " + template.width() * template.height());
        }
        if (stoneLayer != null && stoneLayer.length != width * height) {
            throw new IllegalArgumentException("The stone layer has " + stoneLayer.length + " pixels instead of " + width * height);
        }
        int[] layer = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * width + x;
                // Nearest neighbor, like the texture itself
                int t = (y * template.height() / height) * template.width() + (x * template.width() / width);
                if (template.ore(t) != 0) {
                    layer[i] = oreLayer != null ? oreLayer[t] : neutral;
                } else {
                    layer[i] = stoneLayer != null ? stoneLayer[i] : neutral;
                }
            }
        }
        return layer;
    }
}
