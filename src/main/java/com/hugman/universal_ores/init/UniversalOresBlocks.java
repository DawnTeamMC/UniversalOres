package com.hugman.universal_ores.init;

import com.hugman.universal_ores.creator.bundle.block.OverworldOreBundle;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class UniversalOresBlocks extends UniversalOresBundle {
	public static final OverworldOreBundle GRANITE_ORES = creator(new OverworldOreBundle("granite", FabricBlockSettings.copyOf(Blocks.GRANITE).strength(3.0F, 3.0F)));
	public static final OverworldOreBundle DIORITE_ORES = creator(new OverworldOreBundle("diorite", FabricBlockSettings.copyOf(Blocks.DIORITE).strength(3.0F, 3.0F)));
	public static final OverworldOreBundle ANDESITE_ORES = creator(new OverworldOreBundle("andesite", FabricBlockSettings.copyOf(Blocks.ANDESITE).strength(3.0F, 3.0F)));
	public static final OverworldOreBundle TUFF_ORES = creator(new OverworldOreBundle("tuff", FabricBlockSettings.copyOf(Blocks.TUFF).strength(3.0F, 3.0F)));
	public static final OverworldOreBundle CALCITE_ORES = creator(new OverworldOreBundle("calcite", FabricBlockSettings.copyOf(Blocks.CALCITE).strength(0.5f, 0.5F)));
}
