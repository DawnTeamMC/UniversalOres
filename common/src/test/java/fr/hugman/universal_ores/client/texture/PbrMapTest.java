package fr.hugman.universal_ores.client.texture;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PbrMapTest {
    private static final int ORE_MAP = 0x80C8E600;
    private static final int STONE_MAP = 0xFF4060C0;

    @Test
    void orePixelsTakeTheMapOfTheOreAndTheRestTheMapOfTheStone() {
        OreTemplate diamond = OreTextureGeneratorTest.template("diamond_ore", "stone");
        int[] oreMap = filled(ORE_MAP);
        int[] stoneMap = filled(STONE_MAP);
        int[] map = PbrMap.SPECULAR.compose(diamond, 16, 16, oreMap, stoneMap);
        for (int i = 0; i < map.length; i++) {
            assertEquals(diamond.ore(i) != 0 ? ORE_MAP : STONE_MAP, map[i], "Pixel " + i);
        }
    }

    @Test
    void missingMapsAreNeutral() {
        OreTemplate diamond = OreTextureGeneratorTest.template("diamond_ore", "stone");
        int[] map = PbrMap.NORMAL.compose(diamond, 16, 16, null, filled(STONE_MAP));
        for (int i = 0; i < map.length; i++) {
            assertEquals(diamond.ore(i) != 0 ? PbrMap.NORMAL.neutral() : STONE_MAP, map[i], "Pixel " + i);
        }
    }

    @Test
    void mapsFollowTheSizeOfTheStone() {
        OreTemplate diamond = OreTextureGeneratorTest.template("diamond_ore", "stone");
        int[] map = PbrMap.SPECULAR.compose(diamond, 32, 32, filled(ORE_MAP), null);
        assertEquals(32 * 32, map.length);
        // Each pixel of the 16x16 ore covers 2x2 pixels of the 32x32 stone
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                assertEquals(diamond.ore((y / 2) * 16 + x / 2) != 0 ? ORE_MAP : PbrMap.SPECULAR.neutral(), map[y * 32 + x]);
            }
        }
    }

    @Test
    void pngsKeepEveryChannel() throws IOException {
        int[] pixels = {0x00000000, 0xFFFFFFFF, 0x80C8E600, 0x12345678, 0xFF8080FF, 0x7F010203};
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(Png.encode(3, 2, pixels)));
        assertEquals(3, image.getWidth());
        assertEquals(2, image.getHeight());
        for (int i = 0; i < pixels.length; i++) {
            assertEquals(pixels[i], image.getRGB(i % 3, i / 3), "Pixel " + i);
        }
    }

    private static int[] filled(int value) {
        int[] pixels = new int[16 * 16];
        java.util.Arrays.fill(pixels, value);
        return pixels;
    }
}
