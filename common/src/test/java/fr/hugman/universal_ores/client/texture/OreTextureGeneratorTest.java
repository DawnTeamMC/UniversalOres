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
        double oreTotal = 0;
        double worst = 0;
        String worstName = "";
        for (String stoneName : OVERWORLD_STONES) {
            Image stone = vanilla(stoneName);
            List<Image> images = new ArrayList<>();
            List<Image> references = new ArrayList<>();
            for (String ore : OVERWORLD_ORES) {
                OreTemplate template = template(ore + "_ore", "stone");
                Image generated = generate(template, stone);
                Image reference = TestImages.resource("/reference/handmade/" + stoneName + "_" + ore + "_ore.png");
                double distance = TestImages.meanDistance(generated, reference);
                double oreDistance = TestImages.oreDistance(generated, reference, stone, template);
                System.out.printf("%-9s %-9s %.2f (ore and halo: %.2f)%n", stoneName, ore, distance, oreDistance);
                oreTotal += oreDistance;
                TestImages.write(generated, Path.of("build", "ore_textures", "generated", stoneName + "_" + ore + "_ore.png"));
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
        double oreMean = oreTotal / (OVERWORLD_STONES.size() * OVERWORLD_ORES.size());
        System.out.printf("Mean distance to the hand-made textures: %.2f (ore and halo: %.2f), worst: %.2f (%s)%n", mean, oreMean, worst, worstName);
        TestImages.write(TestImages.sheet(OVERWORLD_ORES, rows, 6), Path.of("build", "ore_textures", "hand_made_comparison.png"));
        assertTrue(mean < 2.45, "Mean distance " + mean);
        assertTrue(oreMean < 3.75, "Mean distance on the ores and their halos " + oreMean);
        assertTrue(worst < 4.2, "Worst distance " + worst + " for " + worstName);
    }

    @Test
    void matchesVanillaDeepslateOres() {
        Image deepslate = vanilla("deepslate");
        double total = 0;
        double oreTotal = 0;
        for (String ore : OVERWORLD_ORES) {
            OreTemplate template = template(ore + "_ore", "stone");
            Image generated = generate(template, deepslate);
            Image reference = vanilla("deepslate_" + ore + "_ore");
            double distance = TestImages.meanDistance(generated, reference);
            double oreDistance = TestImages.oreDistance(generated, reference, deepslate, template);
            System.out.printf("deepslate %-9s %.2f (ore and halo: %.2f)%n", ore, distance, oreDistance);
            total += distance;
            oreTotal += oreDistance;
        }
        double mean = total / OVERWORLD_ORES.size();
        double oreMean = oreTotal / OVERWORLD_ORES.size();
        System.out.printf("Mean distance to Mojang's deepslate ores: %.2f (ore and halo: %.2f)%n", mean, oreMean);
        assertTrue(mean < 2.6, "Mean distance " + mean);
        assertTrue(oreMean < 3.95, "Mean distance on the ores and their halos " + oreMean);
    }

    @Test
    void matchesHandMadeNetherOres() {
        double total = 0;
        double oreTotal = 0;
        int count = 0;
        for (String ore : List.of("gold", "quartz")) {
            OreTemplate template = template("nether_" + ore + "_ore", "netherrack");
            for (String[] face : new String[][]{{"blackstone_top", "blackstone_" + ore + "_ore"}, {"basalt_side", "basalt_" + ore + "_ore_side"}, {"basalt_top", "basalt_" + ore + "_ore_top"}}) {
                Image stone = vanilla(face[0]);
                Image generated = generate(template, stone);
                Image reference = TestImages.resource("/reference/handmade/" + face[1] + ".png");
                double distance = TestImages.meanDistance(generated, reference);
                double oreDistance = TestImages.oreDistance(generated, reference, stone, template);
                System.out.printf("%-26s %.2f (ore and halo: %.2f)%n", face[1], distance, oreDistance);
                total += distance;
                oreTotal += oreDistance;
                count++;
            }
        }
        double mean = total / count;
        double oreMean = oreTotal / count;
        System.out.printf("Mean distance to the hand-made nether textures: %.2f (ore and halo: %.2f)%n", mean, oreMean);
        assertTrue(mean < 2.45, "Mean distance " + mean);
        assertTrue(oreMean < 4.4, "Mean distance on the ores and their halos " + oreMean);
    }

    @Test
    void oreOverlaysAreDrawnOverTheirStone() {
        // Diamond ore drawn like some mods do: the diamonds alone, to be put over stone
        OreTemplate full = template("diamond_ore", "stone");
        int[] overlay = vanilla("diamond_ore").pixels().clone();
        for (int i = 0; i < overlay.length; i++) {
            if (full.ore(i) == 0) {
                overlay[i] = 0;
            }
        }
        OreTemplate template = OreTemplate.extract(16, 16, overlay, vanilla("stone").pixels(), null, null, SETTINGS);
        for (int i = 0; i < overlay.length; i++) {
            assertTrue((template.ore(i) != 0) == (overlay[i] != 0), "Pixel " + i + " should be ore only if the overlay has it");
        }
        assertTrue(template.hasHalo(), "The stone around the overlay should be shaded");
    }

    @Test
    void oresStayVisibleOnStonesOfTheirColor() {
        OreTemplate coal = template("coal_ore", "stone");
        Image blackstone = vanilla("blackstone");
        Palette stoneColors = Palette.of(blackstone.pixels()).significant(SETTINGS.minColorShare());
        double before = medianVisibility(coal, vanilla("coal_ore").pixels(), stoneColors);
        double after = medianVisibility(coal, generate(coal, blackstone).pixels(), stoneColors);
        System.out.printf("Coal on blackstone: %.3f before, %.3f after%n", before, after);
        assertTrue(after >= SETTINGS.minVisibility() - 0.005, "Coal should stand out from blackstone, but is only " + after + " away from its colors");
    }

    @Test
    void stoneShadesTakeTheHueOfTheNewStone() {
        OreTemplate coal = template("coal_ore", "stone");
        Image generated = generate(coal, vanilla("netherrack"));
        double original = 0;
        double shifted = 0;
        for (int i = 0; i < generated.pixels().length; i++) {
            if (coal.isStoneShade(i)) {
                original += OkLab.fromArgb(coal.ore(i))[OkLab.A];
                shifted += OkLab.fromArgb(generated.pixels()[i])[OkLab.A];
            }
        }
        assertTrue(shifted > original, "Coal on netherrack should be redder than on stone");
    }

    @Test
    void detectsOresLikeTheHandDrawnMasks() {
        double total = 0;
        List<String> ores = new ArrayList<>(OVERWORLD_ORES.stream().map(ore -> ore + "_ore").toList());
        ores.addAll(List.of("nether_gold_ore", "nether_quartz_ore"));
        for (String ore : ores) {
            String host = ore.startsWith("nether_") ? "netherrack" : "stone";
            OreTemplate detected = template(ore, host, false);
            boolean[] mask = optionalMask("/assets/universal_ores/textures/ore_hint/minecraft/" + ore + ".png");
            int both = 0;
            int either = 0;
            // Green where both agree, red where the ore was missed, blue where the stone was taken for ore
            int[] comparison = vanilla(ore).pixels().clone();
            for (int i = 0; i < mask.length; i++) {
                boolean found = detected.ore(i) != 0;
                both += found && mask[i] ? 1 : 0;
                either += found || mask[i] ? 1 : 0;
                if (found || mask[i]) {
                    comparison[i] = found && mask[i] ? 0xFF30C040 : mask[i] ? 0xFFE03030 : 0xFF3060E0;
                }
            }
            TestImages.write(new Image(16, 16, comparison), Path.of("build", "ore_textures", "detection", ore + ".png"));
            double overlap = (double) both / either;
            System.out.printf("%-18s detected ore matches the hand-drawn mask at %.0f%%%n", ore, 100 * overlap);
            total += overlap;
        }
        double mean = total / ores.size();
        System.out.printf("Mean overlap with the hand-drawn masks: %.0f%%%n", 100 * mean);
        assertTrue(mean > 0.95, "Mean overlap " + mean);
    }

    @Test
    void matchesHandMadeOresWithoutHints() {
        double total = 0;
        for (String stoneName : OVERWORLD_STONES) {
            Image stone = vanilla(stoneName);
            for (String ore : OVERWORLD_ORES) {
                Image generated = generate(template(ore + "_ore", "stone", false), stone);
                total += TestImages.meanDistance(generated, TestImages.resource("/reference/handmade/" + stoneName + "_" + ore + "_ore.png"));
            }
        }
        double mean = total / (OVERWORLD_STONES.size() * OVERWORLD_ORES.size());
        System.out.printf("Mean distance to the hand-made textures without ore masks: %.2f%n", mean);
        assertTrue(mean < 2.5, "Mean distance " + mean);
    }

    /**
     * The median OKLab distance between the ore pixels of a texture and the closest stone color.
     */
    private static double medianVisibility(OreTemplate template, int[] pixels, Palette stone) {
        float[] distances = new float[pixels.length];
        int count = 0;
        for (int i = 0; i < pixels.length; i++) {
            if (template.ore(i) != 0) {
                distances[count++] = stone.distanceToClosest(OkLab.fromArgb(pixels[i]));
            }
        }
        return OreTemplate.median(distances, count);
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
        return template(ore, host, true);
    }

    /**
     * @param hints whether to use the hand-drawn masks of Universal Ores, instead of finding the ore
     */
    static OreTemplate template(String ore, String host, boolean hints) {
        return template(ore, host, hints, SETTINGS);
    }

    static OreTemplate template(String ore, String host, boolean hints, OreTextureSettings settings) {
        Image oreTexture = vanilla(ore);
        Image hostTexture = vanilla(host).scaledTo(oreTexture.width(), oreTexture.height());
        String hint = "/assets/universal_ores/textures/ore_hint/minecraft/" + ore;
        boolean[] oreHint = hints ? optionalMask(hint + ".png") : null;
        boolean[] haloHint = hints ? optionalMask(hint + "_halo.png") : null;
        return OreTemplate.extract(oreTexture.width(), oreTexture.height(), oreTexture.pixels(), hostTexture.pixels(), oreHint, haloHint, settings);
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
