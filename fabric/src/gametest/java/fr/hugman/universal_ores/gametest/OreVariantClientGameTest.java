package fr.hugman.universal_ores.gametest;

import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.fabricmc.fabric.api.client.gametest.v1.screenshot.TestScreenshotOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.*;

/**
 * Builds a wall of every registered ore variant (one row per stone, sorted like their blocks are registered) and
 * takes a screenshot of it, then checks that every variant has a name.
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
        }
    }
}
