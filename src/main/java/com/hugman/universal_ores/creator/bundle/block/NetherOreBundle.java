package com.hugman.universal_ores.creator.bundle.block;

import com.hugman.dawn.api.creator.BlockCreator;
import com.hugman.dawn.api.creator.bundle.Bundle;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class NetherOreBundle extends Bundle {
	private final BlockCreator GOLD_ORE, QUARTZ_ORE;

	/**
	 * Creates a creator bundle containing all vanilla ores in a certain block.
	 *
	 * @param name     The name of the block in which the ores will be in. (example: "granite")
	 * @param settings The block settings for all the ores.
	 */
	public NetherOreBundle(String name, AbstractBlock.Settings settings) {
		BlockCreator.Builder builder = new BlockCreator.Builder().settings(settings).itemGroup(ItemGroup.BUILDING_BLOCKS);
		GOLD_ORE = put(builder.copy().name(name + "_gold_ore").provider(s -> new OreBlock(s, UniformIntProvider.create(0, 1))).settings(FabricBlockSettings.copyOf(settings).sounds(BlockSoundGroup.NETHER_GOLD_ORE)).build());
		QUARTZ_ORE = put(builder.copy().name(name + "_quartz_ore").provider(s -> new OreBlock(s, UniformIntProvider.create(2, 5))).settings(FabricBlockSettings.copyOf(settings).sounds(BlockSoundGroup.NETHER_ORE)).build());
	}

	public Block gold() {
		return GOLD_ORE.getValue();
	}

	public Block quartz() {
		return QUARTZ_ORE.getValue();
	}
}
