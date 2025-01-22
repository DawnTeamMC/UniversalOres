package fr.hugman.universal_ores.itemgroup;

import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;

public class UniversalOresItemGroupAdditions {
    public static void appendItemGroups() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.addAfter(Blocks.DEEPSLATE_COAL_ORE, UniversalOresBlocks.COAL_ORES);
            entries.addAfter(Blocks.DEEPSLATE_IRON_ORE, UniversalOresBlocks.IRON_ORES);
            entries.addAfter(Blocks.DEEPSLATE_GOLD_ORE, UniversalOresBlocks.GOLD_ORES);
            entries.addAfter(Blocks.DEEPSLATE_COPPER_ORE, UniversalOresBlocks.COPPER_ORES);
            entries.addAfter(Blocks.DEEPSLATE_REDSTONE_ORE, UniversalOresBlocks.REDSTONE_ORES);
            entries.addAfter(Blocks.DEEPSLATE_EMERALD_ORE, UniversalOresBlocks.EMERALD_ORES);
            entries.addAfter(Blocks.DEEPSLATE_LAPIS_ORE, UniversalOresBlocks.LAPIS_ORES);
            entries.addAfter(Blocks.DEEPSLATE_DIAMOND_ORE, UniversalOresBlocks.DIAMOND_ORES);
            entries.addAfter(Blocks.NETHER_GOLD_ORE, UniversalOresBlocks.NETHER_GOLD_ORES);
            entries.addAfter(Blocks.NETHER_QUARTZ_ORE, UniversalOresBlocks.QUARTZ_ORES);
        });
    }
}
