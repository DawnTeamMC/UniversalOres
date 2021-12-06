package com.hugman.universal_ores.init;

import com.hugman.universal_ores.creator.bundle.block.OverworldOreBundle;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class UniversalOresBlocks extends UniversalOresBundle {
	public static final OverworldOreBundle GRANITE_ORES = bundle(new OverworldOreBundle("granite", FabricBlockSettings.of(Material.STONE, MapColor.DIRT_BROWN).requiresTool().strength(3.0F, 3.0F)));
	public static final OverworldOreBundle DIORITE_ORES = bundle(new OverworldOreBundle("diorite", FabricBlockSettings.of(Material.STONE, MapColor.OFF_WHITE).requiresTool().strength(3.0F, 3.0F)));
	public static final OverworldOreBundle ANDESITE_ORES = bundle(new OverworldOreBundle("andesite", FabricBlockSettings.of(Material.STONE, MapColor.STONE_GRAY).requiresTool().strength(3.0F, 3.0F)));
	public static final OverworldOreBundle TUFF_ORES = bundle(new OverworldOreBundle("tuff", FabricBlockSettings.of(Material.STONE, MapColor.TERRACOTTA_GRAY).sounds(BlockSoundGroup.TUFF).requiresTool().strength(3.0F, 3.0F)));
}
