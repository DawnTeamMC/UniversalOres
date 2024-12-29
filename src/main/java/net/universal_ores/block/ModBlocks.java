package net.universal_ores.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.universal_ores.UniversalOres;

import static net.minecraft.block.Blocks.createLightLevelFromLitBlockState;

public class ModBlocks {



    public static final Block DIORITE_COAL_ORE = registerBlock("diorite_coal_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 2),
                    AbstractBlock.Settings.create().mapColor(MapColor.OFF_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block DIORITE_COPPER_ORE = registerBlock("diorite_copper_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 0),
                    AbstractBlock.Settings.create().mapColor(MapColor.OFF_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block DIORITE_REDSTONE_ORE = registerBlock("diorite_redstone_ore",
            new RedstoneOreBlock(AbstractBlock.Settings.create().mapColor(MapColor.OFF_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().ticksRandomly().luminance(createLightLevelFromLitBlockState(9)).strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block DIORITE_LAPIS_ORE = registerBlock("diorite_lapis_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(2, 5),
                    AbstractBlock.Settings.create().mapColor(MapColor.OFF_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block DIORITE_IRON_ORE = registerBlock("diorite_iron_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 0),
                    AbstractBlock.Settings.create().mapColor(MapColor.OFF_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block DIORITE_GOLD_ORE = registerBlock("diorite_gold_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.OFF_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block DIORITE_EMERALD_ORE = registerBlock("diorite_emerald_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.OFF_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block DIORITE_DIAMOND_ORE = registerBlock("diorite_diamond_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.OFF_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));



    public static final Block GRANITE_COAL_ORE = registerBlock("granite_coal_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 2),
                    AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block GRANITE_COPPER_ORE = registerBlock("granite_copper_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 0),
                    AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block GRANITE_REDSTONE_ORE = registerBlock("granite_redstone_ore",
            new RedstoneOreBlock(AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().ticksRandomly().luminance(createLightLevelFromLitBlockState(9)).strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block GRANITE_LAPIS_ORE = registerBlock("granite_lapis_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(2, 5),
                    AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block GRANITE_IRON_ORE = registerBlock("granite_iron_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 0),
                    AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block GRANITE_GOLD_ORE = registerBlock("granite_gold_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block GRANITE_EMERALD_ORE = registerBlock("granite_emerald_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block GRANITE_DIAMOND_ORE = registerBlock("granite_diamond_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));


    public static final Block ANDESITE_COAL_ORE = registerBlock("andesite_coal_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 2),
                    AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block ANDESITE_COPPER_ORE = registerBlock("andesite_copper_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 0),
                    AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block ANDESITE_REDSTONE_ORE = registerBlock("andesite_redstone_ore",
            new RedstoneOreBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().ticksRandomly().luminance(createLightLevelFromLitBlockState(9)).strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block ANDESITE_LAPIS_ORE = registerBlock("andesite_lapis_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(2, 5),
                    AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block ANDESITE_IRON_ORE = registerBlock("andesite_iron_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 0),
                    AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block ANDESITE_GOLD_ORE = registerBlock("andesite_gold_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block ANDESITE_EMERALD_ORE = registerBlock("andesite_emerald_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block ANDESITE_DIAMOND_ORE = registerBlock("andesite_diamond_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));



    public static final Block TUFF_COAL_ORE = registerBlock("tuff_coal_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 2),
                    AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.TUFF)));
    public static final Block TUFF_COPPER_ORE = registerBlock("tuff_copper_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 0),
                    AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.TUFF)));
    public static final Block TUFF_REDSTONE_ORE = registerBlock("tuff_redstone_ore",
            new RedstoneOreBlock(AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().ticksRandomly().luminance(createLightLevelFromLitBlockState(9)).strength(3.0F, 3.0F).sounds(BlockSoundGroup.TUFF)));
    public static final Block TUFF_LAPIS_ORE = registerBlock("tuff_lapis_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(2, 5),
                    AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.TUFF)));
    public static final Block TUFF_IRON_ORE = registerBlock("tuff_iron_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 0),
                    AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.TUFF)));
    public static final Block TUFF_GOLD_ORE = registerBlock("tuff_gold_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.TUFF)));
    public static final Block TUFF_EMERALD_ORE = registerBlock("tuff_emerald_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.TUFF)));
    public static final Block TUFF_DIAMOND_ORE = registerBlock("tuff_diamond_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.TUFF)));




    public static final Block CALCITE_COAL_ORE = registerBlock("calcite_coal_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 2),
                    AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(0.5F, 0.5F).sounds(BlockSoundGroup.CALCITE)));
    public static final Block CALCITE_COPPER_ORE = registerBlock("calcite_copper_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 0),
                    AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(0.5F, 0.5F).sounds(BlockSoundGroup.CALCITE)));
    public static final Block CALCITE_REDSTONE_ORE = registerBlock("calcite_redstone_ore",
            new RedstoneOreBlock(AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().ticksRandomly().luminance(createLightLevelFromLitBlockState(9)).strength(0.5F, 0.5F).sounds(BlockSoundGroup.CALCITE)));
    public static final Block CALCITE_LAPIS_ORE = registerBlock("calcite_lapis_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(2, 5),
                    AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(0.5F, 0.5F).sounds(BlockSoundGroup.CALCITE)));
    public static final Block CALCITE_IRON_ORE = registerBlock("calcite_iron_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 0),
                    AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(0.5F, 0.5F).sounds(BlockSoundGroup.CALCITE)));
    public static final Block CALCITE_GOLD_ORE = registerBlock("calcite_gold_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(0.5F, 0.5F).sounds(BlockSoundGroup.CALCITE)));
    public static final Block CALCITE_EMERALD_ORE = registerBlock("calcite_emerald_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(0.5F, 0.5F).sounds(BlockSoundGroup.CALCITE)));
    public static final Block CALCITE_DIAMOND_ORE = registerBlock("calcite_diamond_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(0.5F, 0.5F).sounds(BlockSoundGroup.CALCITE)));




    public static final Block BASALT_GOLD_ORE = registerBlock("basalt_gold_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 1),
                    AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.BASALT)));
    public static final Block BASALT_QUARTZ_ORE = registerBlock("basalt_quartz_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(2, 5),
                    AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.BASALT)));





    public static final Block BLACKSTONE_GOLD_ORE = registerBlock("blackstone_gold_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(0, 1),
                    AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));
    public static final Block BLACKSTONE_QUARTZ_ORE = registerBlock("blackstone_quartz_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(2, 5),
                    AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE)));










    public static void register() {
        UniversalOres.LOGGER.info("Reg ModBlocks" + UniversalOres.MOD_ID);
    }
    
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(UniversalOres.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(UniversalOres.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }
}
