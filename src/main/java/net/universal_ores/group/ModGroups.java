package net.universal_ores.group;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;
import net.universal_ores.UniversalOres;
import net.universal_ores.block.ModBlocks;

public class ModGroups {




    public static void register() {
        UniversalOres.LOGGER.info("Reg ModGroups" + UniversalOres.MOD_ID);



        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.addAfter(Blocks.DEEPSLATE_COAL_ORE, ModBlocks.ANDESITE_COAL_ORE, ModBlocks.DIORITE_COAL_ORE,ModBlocks.GRANITE_COAL_ORE,ModBlocks.CALCITE_COAL_ORE,ModBlocks.TUFF_COAL_ORE);
        });


        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.addAfter(Blocks.DEEPSLATE_DIAMOND_ORE, ModBlocks.ANDESITE_DIAMOND_ORE, ModBlocks.DIORITE_DIAMOND_ORE,ModBlocks.GRANITE_DIAMOND_ORE,ModBlocks.CALCITE_DIAMOND_ORE,ModBlocks.TUFF_DIAMOND_ORE);
        });


        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.addAfter(Blocks.DEEPSLATE_COPPER_ORE, ModBlocks.ANDESITE_COPPER_ORE, ModBlocks.DIORITE_COPPER_ORE,ModBlocks.GRANITE_COPPER_ORE,ModBlocks.CALCITE_COPPER_ORE,ModBlocks.TUFF_COPPER_ORE);
        });


        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.addAfter(Blocks.DEEPSLATE_IRON_ORE, ModBlocks.ANDESITE_IRON_ORE, ModBlocks.DIORITE_IRON_ORE,ModBlocks.GRANITE_IRON_ORE,ModBlocks.CALCITE_IRON_ORE,ModBlocks.TUFF_IRON_ORE);
        });



        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.addAfter(Blocks.DEEPSLATE_LAPIS_ORE, ModBlocks.ANDESITE_LAPIS_ORE, ModBlocks.DIORITE_LAPIS_ORE,ModBlocks.GRANITE_LAPIS_ORE,ModBlocks.CALCITE_LAPIS_ORE,ModBlocks.TUFF_LAPIS_ORE);
        });


        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.addAfter(Blocks.DEEPSLATE_REDSTONE_ORE, ModBlocks.ANDESITE_REDSTONE_ORE, ModBlocks.DIORITE_REDSTONE_ORE,ModBlocks.GRANITE_REDSTONE_ORE,ModBlocks.CALCITE_REDSTONE_ORE,ModBlocks.TUFF_REDSTONE_ORE);
        });


        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.addAfter(Blocks.DEEPSLATE_GOLD_ORE, ModBlocks.ANDESITE_GOLD_ORE, ModBlocks.DIORITE_GOLD_ORE,ModBlocks.GRANITE_GOLD_ORE,ModBlocks.CALCITE_GOLD_ORE,ModBlocks.TUFF_GOLD_ORE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.addAfter(Blocks.DEEPSLATE_EMERALD_ORE, ModBlocks.ANDESITE_EMERALD_ORE, ModBlocks.DIORITE_EMERALD_ORE,ModBlocks.GRANITE_EMERALD_ORE,ModBlocks.CALCITE_EMERALD_ORE,ModBlocks.TUFF_EMERALD_ORE);
        });


        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.addAfter(Blocks.NETHER_GOLD_ORE, ModBlocks.BLACKSTONE_GOLD_ORE, ModBlocks.BASALT_GOLD_ORE);
        });
    }
}
