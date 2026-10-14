package fr.hugman.universal_ores.gametest;

import com.mojang.blaze3d.platform.NativeImage;
import fr.hugman.universal_ores.client.texture.PbrMap;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.fabricmc.fabric.api.client.gametest.v1.screenshot.TestScreenshotOptions;
import net.minecraft.core.BlockPos;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.*;

/**
 * Builds a wall of every registered ore variant (one row per stone, sorted like their blocks are registered) and
 * takes a screenshot of it, then checks that every variant has a name.
 * <p>
 * This test mod animates the emerald ore and calcite textures, to check that their variants are animated too. It
 * also gives diamond ore a specular map and granite a normal map, like a PBR resource pack, and an emissive texture
 * to diamond ore, like an emissive resource pack, to check that their variants get them too.
 */
public class OreVariantClientGameTest implements FabricClientGameTest {
    private static final int WALL_Y = 200;

    @Override
    public void runTest(ClientGameTestContext context) {
        Map<Block, List<RegisteredOreVariant>> rows = new TreeMap<>(Comparator.comparingInt(BuiltInRegistries.BLOCK::getId));
        OreVariantRegistrar.registered().forEach(variant -> rows.computeIfAbsent(variant.stone(), stone -> new ArrayList<>()).add(variant));
        rows.values().forEach(variants -> variants.sort(Comparator.comparingInt(variant -> BuiltInRegistries.BLOCK.getId(variant.ore()))));
        int width = rows.values().stream().mapToInt(List::size).max().orElse(1);

        try (TestSingleplayerContext singleplayer = context.worldBuilder().create()) {
            singleplayer.getServer().runOnServer(server -> {
                var level = server.overworld();
                int row = 0;
                for (List<RegisteredOreVariant> variants : rows.values()) {
                    for (int column = 0; column < variants.size(); column++) {
                        // Facing south, east is on the left: columns go from east to west to read from left to right
                        level.setBlockAndUpdate(new BlockPos(width - 1 - column, WALL_Y + rows.size() - 1 - row, 0), variants.get(column).block().defaultBlockState());
                    }
                    row++;
                }
            });
            double distance = 0.75 * Math.max(width, rows.size()) + 1;
            // Spectators don't fall and don't show their hand
            singleplayer.getServer().runCommand("gamemode spectator @a");
            singleplayer.getServer().runCommand("time set noon");
            singleplayer.getServer().runCommand(String.format(Locale.ROOT, "tp @a %.2f %.2f %.2f 0 0", width / 2.0, WALL_Y + rows.size() / 2.0 - 1.6, -distance));
            singleplayer.getConnection().waitForChunksRender();
            // An unfocused window would show the pause menu over the world
            context.runOnClient(minecraft -> minecraft.options.pauseOnLostFocus = false);
            context.setScreen(() -> null);
            context.getInput().pressKey(options -> options.keyToggleGui);
            context.waitTicks(20);
            context.takeScreenshot(TestScreenshotOptions.of("universal_ores_variants").withSize(1920, 1080));
            context.getInput().pressKey(options -> options.keyToggleGui);

            List<String> unnamed = context.computeOnClient(minecraft -> OreVariantRegistrar.registered().stream()
                    .filter(variant -> {
                        String name = new ItemStack(variant.block()).getHoverName().getString();
                        return name.equals(variant.block().getDescriptionId()) || name.isBlank();
                    })
                    .map(variant -> variant.variant().id().toString())
                    .toList());
            if (!unnamed.isEmpty()) {
                throw new AssertionError("Ore variants without a name: " + unnamed);
            }

            Map<String, Boolean> animated = context.computeOnClient(minecraft -> {
                var blocks = minecraft.getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS);
                Map<String, Boolean> result = new LinkedHashMap<>();
                for (String sprite : List.of("granite_emerald_ore", "calcite_coal_ore", "calcite_emerald_ore", "granite_coal_ore")) {
                    result.put(sprite, blocks.getSprite(Identifier.fromNamespaceAndPath("universal_ores", "block/" + sprite)).contents().isAnimated());
                }
                return result;
            });
            Map<String, Boolean> expected = Map.of("granite_emerald_ore", true, "calcite_coal_ore", true, "calcite_emerald_ore", true, "granite_coal_ore", false);
            if (!animated.equals(expected)) {
                throw new AssertionError("Expected the variants of animated ores and stones to be animated, and only them: " + animated);
            }

            List<String> mapProblems = context.computeOnClient(minecraft -> checkMaps(minecraft.getResourceManager()));
            if (!mapProblems.isEmpty()) {
                throw new AssertionError("Wrong PBR maps: " + mapProblems);
            }

            List<String> emissiveProblems = context.computeOnClient(minecraft -> checkEmissiveTextures(minecraft.getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS)));
            if (!emissiveProblems.isEmpty()) {
                throw new AssertionError("Wrong emissive textures: " + emissiveProblems);
            }
        }
    }

    /**
     * Variants get the maps their ore or their stone has: a map of a variant takes the map of the ore on the ore, and
     * the map of the stone elsewhere.
     */
    private static List<String> checkMaps(ResourceManager resources) {
        List<String> problems = new ArrayList<>();
        Map<String, Boolean> expected = new LinkedHashMap<>();
        expected.put("granite_diamond_ore_s", true);
        expected.put("granite_diamond_ore_n", true);
        expected.put("andesite_diamond_ore_s", true);
        expected.put("andesite_diamond_ore_n", false);
        expected.put("granite_coal_ore_n", true);
        expected.put("granite_coal_ore_s", false);
        expected.forEach((map, present) -> {
            if (resources.getResource(variantMap(map)).isPresent() != present) {
                problems.add(map + (present ? " is missing" : " shouldn't exist"));
            }
        });
        if (!problems.isEmpty()) {
            return problems;
        }

        int[] specular = pixels(resources, variantMap("granite_diamond_ore_s"));
        int[] diamondSpecular = pixels(resources, Identifier.withDefaultNamespace("textures/block/diamond_ore_s.png"));
        checkMap(problems, "granite_diamond_ore_s", specular, diamondSpecular, PbrMap.SPECULAR.neutral());
        int[] normal = pixels(resources, variantMap("granite_diamond_ore_n"));
        int[] graniteNormal = pixels(resources, Identifier.withDefaultNamespace("textures/block/granite_n.png"));
        checkMap(problems, "granite_diamond_ore_n", normal, graniteNormal, PbrMap.NORMAL.neutral());
        return problems;
    }

    /**
     * Every pixel of the map should either come from the given map, or be neutral, and both should be found.
     */
    private static void checkMap(List<String> problems, String name, int[] map, int[] source, int neutral) {
        int fromSource = 0;
        int neutrals = 0;
        for (int i = 0; i < map.length; i++) {
            if (map[i] == source[i]) {
                fromSource++;
            } else if (map[i] == neutral) {
                neutrals++;
            } else {
                problems.add(String.format(Locale.ROOT, "%s has %08X at pixel %d, neither %08X nor neutral", name, map[i], i, source[i]));
                return;
            }
        }
        if (fromSource == 0 || neutrals == 0) {
            problems.add(name + " has " + fromSource + " pixels from its source map and " + neutrals + " neutral pixels");
        }
    }

    /**
     * Variants of an ore with an emissive texture get one, and only them. With Continuity, it draws them.
     */
    private static List<String> checkEmissiveTextures(net.minecraft.client.renderer.texture.TextureAtlas blocks) {
        List<String> problems = new ArrayList<>();
        TextureAtlasSprite texture = blocks.getSprite(Identifier.fromNamespaceAndPath("universal_ores", "block/granite_diamond_ore"));
        Identifier emissiveId = Identifier.fromNamespaceAndPath("universal_ores", "block/granite_diamond_ore_e");
        TextureAtlasSprite emissive = blocks.getSprite(emissiveId);
        if (!emissive.contents().name().equals(emissiveId)) {
            problems.add(emissiveId + " is missing");
        }
        Identifier noEmissiveId = Identifier.fromNamespaceAndPath("universal_ores", "block/granite_coal_ore_e");
        if (blocks.getSprite(noEmissiveId).contents().name().equals(noEmissiveId)) {
            problems.add(noEmissiveId + " shouldn't exist");
        }
        if (FabricLoader.getInstance().isModLoaded("continuity")) {
            try {
                Class<?> api = Class.forName("me.pepperbell.continuity.api.client.EmissiveSpriteApi");
                Object instance = api.getMethod("get").invoke(null);
                Object found = api.getMethod("getEmissiveSprite", TextureAtlasSprite.class).invoke(instance, texture);
                if (found != emissive) {
                    problems.add("Continuity doesn't draw " + emissiveId + " over granite diamond ore, but " + found);
                }
            } catch (ReflectiveOperationException e) {
                problems.add("Couldn't ask Continuity: " + e);
            }
        }
        return problems;
    }

    private static Identifier variantMap(String name) {
        return Identifier.fromNamespaceAndPath("universal_ores", "textures/block/" + name + ".png");
    }

    private static int[] pixels(ResourceManager resources, Identifier texture) {
        try (InputStream stream = resources.open(texture); NativeImage image = NativeImage.read(stream)) {
            return image.getPixels();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
