package com.hugman.universal_ores.block;

import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class NetherOres {
	private final String name;
	private final ExperienceDroppingBlock gold;
	private final ExperienceDroppingBlock quartz;

	/**
	 * Creates a creator bundle containing all vanilla ores in a certain block.
	 *
	 * @param name         The name of the block in which the ores will be in. (example: "basalt")
	 *                     This name will be used as a prefix to register the blocks' names.
	 * @param baseSettings The base settings for all the ore blocks.
	 */
	public NetherOres(String name, AbstractBlock.Settings baseSettings) {
		this.name = name;
		this.gold = new ExperienceDroppingBlock(DawnBlockSettings.copyOf(baseSettings).sounds(BlockSoundGroup.NETHER_GOLD_ORE), UniformIntProvider.create(0, 1));
		this.quartz = new ExperienceDroppingBlock(DawnBlockSettings.copyOf(baseSettings).sounds(BlockSoundGroup.NETHER_ORE), UniformIntProvider.create(2, 5));
	}

	public void register(Registrar r) {
		r.add(this.name + "_gold_ore", this.gold);
		r.add(this.name + "_quartz_ore", this.quartz);
	}

	public ExperienceDroppingBlock gold() {
		return gold;
	}

	public ExperienceDroppingBlock quartz() {
		return quartz;
	}
}
