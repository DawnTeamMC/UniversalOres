package fr.hugman.universal_ores.client.texture;

import fr.hugman.universal_ores.client.texture.TestImages.Image;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Compares the generated ore textures with the ones they replace: the hand-made Universal Ores textures, and
 * Mojang's deepslate ores (generated here from the stone ores). The thresholds are mean OKLab distances in hundredths:
 * around 2 is "the same texture with a few pixels shaded differently".
 */
class OreTextureGeneratorTest {
    private static final List<String> OVERWORLD_ORES = List.of("coal", "iron", "gold", "copper", "lapis", "redstone", "emerald", "diamond");
    private static final List<String> OVERWORLD_STONES = List.of("andesite", "diorite", "granite", "tuff", "calcite");
    private static final OreTextureSettings SETTINGS = OreTextureSettings.DEFAULT;

    @Test
    void matchesHandMadeOverworldOres() {
        List<TestImages.Row> rows = new ArrayList<>();
        double total = 0;
        double worst = 0;
        String worstName = "";
        for (String stoneName : OVERWORLD_STONES) {
            Image stone = vanilla(stoneName);
            List<Image> images = new ArrayList<>();
            List<Image> references = new ArrayList<>();
            for (String ore : OVERWORLD_ORES) {
                Image generated = generate(template(ore + "_ore", "stone"), stone);
                Image reference = TestImages.resource("/reference/handmade/" + stoneName + "_" + ore + "_ore.png");
                double distance = TestImages.meanDistance(generated, reference);
                System.out.printf("%-9s %-9s %.2f%n", stoneName, ore, distance);
                total += distance;
                if (distance > worst) {
                    worst = distance;
                    worstName = stoneName + " " + ore;
                }
                images.add(generated);
                references.add(reference);
            }
            rows.add(new TestImages.Row(stoneName + " (hand-made)", references));
            rows.add(new TestImages.Row(stoneName + " (generated)", images));
        }
        double mean = total / (OVERWORLD_STONES.size() * OVERWORLD_ORES.size());
        System.out.printf("Mean distance to the hand-made textures: %.2f, worst: %.2f (%s)%n", mean, worst, worstName);
        TestImages.write(TestImages.sheet(OVERWORLD_ORES, rows, 6), Path.of("build", "ore_textures", "hand_made_comparison.png"));
        assertTrue(mean < 2.55, "Mean distance " + mean);
        assertTrue(worst < 4.5, "Worst distance " + worst + " for " + worstName);
    }

    @Test
    void matchesVanillaDeepslateOres() {
        Image deepslate = vanilla("deepslate");
        double total = 0;
        for (String ore : OVERWORLD_ORES) {
            Image generated = generate(template(ore + "_ore", "stone"), deepslate);
            double distance = TestImages.meanDistance(generated, vanilla("deepslate_" + ore + "_ore"));
            System.out.printf("deepslate %-9s %.2f%n", ore, distance);
            total += distance;
        }
        double mean = total / OVERWORLD_ORES.size();
        System.out.printf("Mean distance to Mojang's deepslate ores: %.2f%n", mean);
        assertTrue(mean < 3.0, "Mean distance " + mean);
    }

    @Test
    void matchesHandMadeNetherOres() {
        double total = 0;
        int count = 0;
        for (String ore : List.of("gold", "quartz")) {
            OreTemplate template = template("nether_" + ore + "_ore", "netherrack");
            for (String[] face : new String[][]{{"blackstone_top", "blackstone_" + ore + "_ore"}, {"basalt_side", "basalt_" + ore + "_ore_side"}, {"basalt_top", "basalt_" + ore + "_ore_top"}}) {
                Image generated = generate(template, vanilla(face[0]));
                double distance = TestImages.meanDistance(generated, TestImages.resource("/reference/handmade/" + face[1] + ".png"));
                System.out.printf("%-26s %.2f%n", face[1], distance);
                total += distance;
                count++;
            }
        }
        double mean = total / count;
        System.out.printf("Mean distance to the hand-made nether textures: %.2f%n", mean);
        assertTrue(mean < 4.0, "Mean distance " + mean);
    }

    @Test
    void overlaysAreShadedLikeVanillaOres() {
        // A single ore pixel in the middle of a transparent overlay
        int[] overlay = new int[16 * 16];
        overlay[8 * 16 + 8] = 0xFFFF0000;
        OreTemplate template = OreTemplate.fromOverlay(16, 16, overlay, SETTINGS);
        assertTrue(template.haloLevel(7 * 16 + 8) < 0, "The stone above the ore should be in its shadow");
        assertTrue(template.haloLevel(9 * 16 + 8) > 1, "The stone below the ore should be lit");
    }

    static OreTemplate template(String ore, String host) {
        Image oreTexture = vanilla(ore);
        Image hostTexture = vanilla(host).scaledTo(oreTexture.width(), oreTexture.height());
        String hint = "/assets/universal_ores/textures/ore_hint/minecraft/" + ore;
        boolean[] oreHint = optionalMask(hint + ".png");
        boolean[] haloHint = optionalMask(hint + "_halo.png");
        return OreTemplate.extract(oreTexture.width(), oreTexture.height(), oreTexture.pixels(), hostTexture.pixels(), oreHint, haloHint, SETTINGS);
    }

    static Image generate(OreTemplate template, Image stone) {
        return new Image(stone.width(), stone.height(), OreTextureGenerator.generate(template, stone.width(), stone.height(), stone.pixels(), SETTINGS));
    }

    static Image vanilla(String name) {
        return TestImages.resource("/assets/minecraft/textures/block/" + name + ".png");
    }

    private static boolean[] optionalMask(String path) {
        return OreTextureGeneratorTest.class.getResource(path) == null ? null : TestImages.resource(path).mask();
    }
}
