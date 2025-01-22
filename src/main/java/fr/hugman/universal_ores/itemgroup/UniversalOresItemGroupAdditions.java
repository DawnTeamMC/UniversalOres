package fr.hugman.universal_ores.itemgroup;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;

import static fr.hugman.universal_ores.block.UniversalOresBlocks.*;

public class UniversalOresItemGroupAdditions {
    public static void appendItemGroups() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.addAfter(Blocks.DEEPSLATE_COAL_ORE, GRANITE_ORES.coal(), DIORITE_ORES.coal(), ANDESITE_ORES.coal(), TUFF_ORES.coal(), CALCITE_ORES.coal());
            entries.addAfter(Blocks.DEEPSLATE_IRON_ORE, GRANITE_ORES.iron(), DIORITE_ORES.iron(), ANDESITE_ORES.iron(), TUFF_ORES.iron(), CALCITE_ORES.iron());
            entries.addAfter(Blocks.DEEPSLATE_GOLD_ORE, GRANITE_ORES.gold(), DIORITE_ORES.gold(), ANDESITE_ORES.gold(), TUFF_ORES.gold(), CALCITE_ORES.gold());
            entries.addAfter(Blocks.DEEPSLATE_REDSTONE_ORE, GRANITE_ORES.redstone(), DIORITE_ORES.redstone(), ANDESITE_ORES.redstone(), TUFF_ORES.redstone(), CALCITE_ORES.redstone());
            entries.addAfter(Blocks.DEEPSLATE_EMERALD_ORE, GRANITE_ORES.emerald(), DIORITE_ORES.emerald(), ANDESITE_ORES.emerald(), TUFF_ORES.emerald(), CALCITE_ORES.emerald());
            entries.addAfter(Blocks.DEEPSLATE_LAPIS_ORE, GRANITE_ORES.lapis(), DIORITE_ORES.lapis(), ANDESITE_ORES.lapis(), TUFF_ORES.lapis(), CALCITE_ORES.lapis());
            entries.addAfter(Blocks.DEEPSLATE_DIAMOND_ORE, GRANITE_ORES.diamond(), DIORITE_ORES.diamond(), ANDESITE_ORES.diamond(), TUFF_ORES.diamond(), CALCITE_ORES.diamond());

            entries.addAfter(Blocks.NETHER_GOLD_ORE, BLACKSTONE_ORES.gold(), BASALT_ORES.gold());
            entries.addAfter(Blocks.NETHER_QUARTZ_ORE, BLACKSTONE_ORES.quartz(), BASALT_ORES.quartz());
        });
    }
}
