package fr.hugman.universal_ores.client.texture;

import fr.hugman.universal_ores.client.texture.TestImages.Image;

import java.awt.image.BufferedImage;
import java.lang.reflect.RecordComponent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Draws the images of the wiki page about textures, with the current generator, from the vanilla textures.
 * <p>
 * Argument: the output folder.
 */
public final class WikiImages {
    private static final int SCALE = 16;
    private static final int STRIP_SCALE = 8;
    private static final OreTextureSettings SETTINGS = OreTextureSettings.DEFAULT;
    private static final List<String> ORES = List.of("coal", "iron", "gold", "copper", "lapis", "redstone", "emerald", "diamond");

    public static void main(String[] args) {
        Path folder = Path.of(args[0]);

        // The inputs: an ore, the stone it is drawn on, and a new stone
        Image ore = vanilla("diamond_ore");
        Image host = vanilla("stone");
        Image granite = vanilla("granite");
        write(ore, folder, "ore");
        write(host, folder, "host");
        write(granite, folder, "new_stone");

        // Taking the ore apart
        OreTemplate template = OreTextureGeneratorTest.template("diamond_ore", "stone", false);
        write(highlight(ore, i -> ore.pixels()[i] != host.pixels()[i]), folder, "changed");
        write(highlight(ore, i -> template.ore(i) != 0), folder, "ore_pixels");
        write(halo(template, ore, false), folder, "halo_levels");
        write(halo(template, ore, true), folder, "halo_tints");
        write(palette(granite), folder, "new_stone_palette");
        write(OreTextureGeneratorTest.generate(template, granite), folder, "result");

        // What keeps an ore readable
        OreTemplate coal = OreTextureGeneratorTest.template("coal_ore", "stone", false);
        Image blackstone = vanilla("blackstone");
        write(generate(coal, blackstone, Map.of("minVisibility", 0.0F)), folder, "visibility_off");
        write(generate(coal, blackstone, Map.of()), folder, "visibility_on");
        Image netherrack = vanilla("netherrack");
        write(generate(coal, netherrack, Map.of("stoneHueTransfer", 0.0F)), folder, "stone_hue_off");
        write(generate(coal, netherrack, Map.of()), folder, "stone_hue_on");
        Image diorite = vanilla("diorite");
        OreTemplate everyChange = OreTextureGeneratorTest.template("diamond_ore", "stone", false, settings(Map.of("haloMinChange", 0.0F)));
        write(OreTextureGeneratorTest.generate(everyChange, diorite), folder, "halo_every_change");
        write(OreTextureGeneratorTest.generate(template, diorite), folder, "halo_shading_only");

        // Mojang's deepslate ores, and the ones generated from the stone ores
        Image deepslate = vanilla("deepslate");
        List<Image> mojang = new ArrayList<>();
        List<Image> generated = new ArrayList<>();
        for (String name : ORES) {
            mojang.add(vanilla("deepslate_" + name + "_ore"));
            generated.add(OreTextureGeneratorTest.generate(OreTextureGeneratorTest.template(name + "_ore", "stone"), deepslate));
        }
        write(strip(mojang), folder, "deepslate_mojang");
        write(strip(generated), folder, "deepslate_generated");

        // Every vanilla ore in every stone of Universal Ores
        List<List<Image>> rows = new ArrayList<>();
        for (String stone : List.of("andesite", "diorite", "granite", "tuff", "calcite")) {
            List<Image> row = new ArrayList<>();
            for (String name : ORES) {
                row.add(OreTextureGeneratorTest.generate(OreTextureGeneratorTest.template(name + "_ore", "stone"), vanilla(stone)));
            }
            rows.add(row);
        }
        TestImages.write(grid(rows), folder.resolve("gallery.png"));
        System.out.println("Wrote the wiki images to " + folder.toAbsolutePath());
    }

    private static Image vanilla(String name) {
        return OreTextureGeneratorTest.vanilla(name);
    }

    private static Image generate(OreTemplate template, Image stone, Map<String, Object> overrides) {
        OreTextureSettings settings = settings(overrides);
        return new Image(stone.width(), stone.height(), OreTextureGenerator.generate(template, stone.width(), stone.height(), stone.pixels(), settings));
    }

    /**
     * The default settings, with some of them changed.
     */
    private static OreTextureSettings settings(Map<String, Object> overrides) {
        try {
            RecordComponent[] components = OreTextureSettings.class.getRecordComponents();
            Object[] values = new Object[components.length];
            Class<?>[] types = new Class<?>[components.length];
            for (int i = 0; i < components.length; i++) {
                values[i] = overrides.containsKey(components[i].getName()) ? overrides.get(components[i].getName()) : components[i].getAccessor().invoke(SETTINGS);
                types[i] = components[i].getType();
            }
            return OreTextureSettings.class.getDeclaredConstructor(types).newInstance(values);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * The texture, faded except for the given pixels.
     */
    private static Image highlight(Image image, java.util.function.IntPredicate shown) {
        int[] pixels = image.pixels().clone();
        for (int i = 0; i < pixels.length; i++) {
            if (!shown.test(i)) {
                pixels[i] = faded(pixels[i]);
            }
        }
        return new Image(image.width(), image.height(), pixels);
    }

    /**
     * The halo of a template, around its ore and over the faded stone: its lightness levels in grey (from black for
     * the stone's dark end and beyond, to white for its light end and beyond), or its tints.
     */
    private static Image halo(OreTemplate template, Image ore, boolean tints) {
        int[] pixels = new int[template.width() * template.height()];
        for (int i = 0; i < pixels.length; i++) {
            if (template.ore(i) != 0) {
                pixels[i] = template.ore(i);
            } else if (template.isHalo(i)) {
                float level = Math.clamp(template.haloLevel(i), -0.25F, 1.25F);
                pixels[i] = tints
                        ? OkLab.toArgb(0.75F, 3 * template.haloTintA(i), 3 * template.haloTintB(i), 0xFF)
                        : OkLab.toArgb(0.15F + 0.8F * (level + 0.25F) / 1.5F, 0, 0, 0xFF);
            } else {
                pixels[i] = faded(ore.pixels()[i]);
            }
        }
        return new Image(template.width(), template.height(), pixels);
    }

    /**
     * A dark blue shade of a pixel, for the parts of an image that don't matter.
     */
    private static int faded(int pixel) {
        return OkLab.toArgb(0.22F + 0.15F * OkLab.fromArgb(pixel)[OkLab.L], -0.01F, -0.05F, 0xFF);
    }

    /**
     * The colors of a texture from darkest to lightest, as tall as they are common: the colors the halo is redrawn
     * with are the ones reaching the top half.
     */
    private static BufferedImage palette(Image texture) {
        Palette palette = Palette.of(texture.pixels());
        Palette common = palette.significant(SETTINGS.minColorShare());
        int barWidth = 32;
        int height = 96;
        BufferedImage image = new BufferedImage(palette.size() * barWidth, height, BufferedImage.TYPE_INT_ARGB);
        for (int color = 0; color < palette.size(); color++) {
            float[] lab = palette.lab(color);
            boolean isCommon = false;
            for (int j = 0; j < common.size(); j++) {
                isCommon |= OkLab.distance(common.lab(j), lab) < 1.0E-6F;
            }
            for (int y = isCommon ? 0 : height / 2; y < height; y++) {
                for (int x = 0; x < barWidth; x++) {
                    image.setRGB(color * barWidth + x, y, OkLab.toArgb(lab[OkLab.L], lab[OkLab.A], lab[OkLab.B], 0xFF));
                }
            }
        }
        return image;
    }

    private static BufferedImage strip(List<Image> images) {
        return grid(List.of(images));
    }

    private static BufferedImage grid(List<List<Image>> rows) {
        int cell = 16 * STRIP_SCALE;
        int gap = 8;
        int columns = rows.stream().mapToInt(List::size).max().orElse(0);
        BufferedImage grid = new BufferedImage(columns * (cell + gap) - gap, rows.size() * (cell + gap) - gap, BufferedImage.TYPE_INT_ARGB);
        for (int row = 0; row < rows.size(); row++) {
            for (int column = 0; column < rows.get(row).size(); column++) {
                Image image = rows.get(row).get(column);
                for (int y = 0; y < cell; y++) {
                    for (int x = 0; x < cell; x++) {
                        grid.setRGB(column * (cell + gap) + x, row * (cell + gap) + y, image.pixels()[(y * image.height() / cell) * image.width() + x * image.width() / cell]);
                    }
                }
            }
        }
        return grid;
    }

    private static void write(Image image, Path folder, String name) {
        int width = image.width() * SCALE;
        int height = image.height() * (image.height() == 16 ? SCALE : 4);
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                scaled.setRGB(x, y, image.pixels()[(y * image.height() / height) * image.width() + x * image.width() / width]);
            }
        }
        TestImages.write(scaled, folder.resolve(name + ".png"));
    }

    private static void write(BufferedImage image, Path folder, String name) {
        TestImages.write(image, folder.resolve(name + ".png"));
    }
}
