package fr.hugman.universal_ores.itemgroup;

import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.CreativeModeTabs;

public class UniversalOresItemGroupAdditions {
    public static void appendItemGroups() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.insertAfter(Blocks.DEEPSLATE_COAL_ORE, UniversalOresBlocks.COAL_ORES);
            entries.insertAfter(Blocks.DEEPSLATE_IRON_ORE, UniversalOresBlocks.IRON_ORES);
            entries.insertAfter(Blocks.DEEPSLATE_GOLD_ORE, UniversalOresBlocks.GOLD_ORES);
            entries.insertAfter(Blocks.DEEPSLATE_COPPER_ORE, UniversalOresBlocks.COPPER_ORES);
            entries.insertAfter(Blocks.DEEPSLATE_REDSTONE_ORE, UniversalOresBlocks.REDSTONE_ORES);
            entries.insertAfter(Blocks.DEEPSLATE_EMERALD_ORE, UniversalOresBlocks.EMERALD_ORES);
            entries.insertAfter(Blocks.DEEPSLATE_LAPIS_ORE, UniversalOresBlocks.LAPIS_ORES);
            entries.insertAfter(Blocks.DEEPSLATE_DIAMOND_ORE, UniversalOresBlocks.DIAMOND_ORES);
            entries.insertAfter(Blocks.NETHER_GOLD_ORE, UniversalOresBlocks.NETHER_GOLD_ORES);
            entries.insertAfter(Blocks.NETHER_QUARTZ_ORE, UniversalOresBlocks.QUARTZ_ORES);
        });
    }
}
