package com.hugman.universal_ores.block;

import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public final class OverworldOres {
	private final String name;
	private final ExperienceDroppingBlock coal;
	private final ExperienceDroppingBlock iron;
	private final ExperienceDroppingBlock gold;
	private final ExperienceDroppingBlock copper;
	private final ExperienceDroppingBlock lapis;
	private final RedstoneOreBlock redstone;
	private final ExperienceDroppingBlock emerald;
	private final ExperienceDroppingBlock diamond;

	/**
	 * Object containing all vanilla ores that are inside a certain block.
	 *
	 * @param name         The name of the block in which the ores will be in. (example: "granite")
	 *                     This name will be used as a prefix to register the blocks' names.
	 * @param baseSettings The base settings for all the ore blocks.
	 */
	public OverworldOres(String name, AbstractBlock.Settings baseSettings) {
		this.name = name;
		this.coal = new ExperienceDroppingBlock(DawnBlockSettings.copyOf(baseSettings), UniformIntProvider.create(0, 2));
		this.iron = new ExperienceDroppingBlock(DawnBlockSettings.copyOf(baseSettings));
		this.copper = new ExperienceDroppingBlock(DawnBlockSettings.copyOf(baseSettings));
		this.gold = new ExperienceDroppingBlock(DawnBlockSettings.copyOf(baseSettings));
		this.redstone = new RedstoneOreBlock(DawnBlockSettings.copyOf(baseSettings).ticksRandomly().luminance((state) -> (Boolean) state.get(Properties.LIT) ? 9 : 0));
		this.emerald = new ExperienceDroppingBlock(DawnBlockSettings.copyOf(baseSettings), UniformIntProvider.create(3, 7));
		this.lapis = new ExperienceDroppingBlock(DawnBlockSettings.copyOf(baseSettings), UniformIntProvider.create(2, 5));
		this.diamond = new ExperienceDroppingBlock(DawnBlockSettings.copyOf(baseSettings), UniformIntProvider.create(3, 7));
	}

	public void register(Registrar r) {
		r.add(this.name + "_coal_ore", this.coal);
		r.add(this.name + "_iron_ore", this.iron);
		r.add(this.name + "_copper_ore", this.copper);
		r.add(this.name + "_gold_ore", this.gold);
		r.add(this.name + "_redstone_ore", this.redstone);
		r.add(this.name + "_emerald_ore", this.emerald);
		r.add(this.name + "_lapis_ore", this.lapis);
		r.add(this.name + "_diamond_ore", this.diamond);
	}

	public ExperienceDroppingBlock coal() {
		return coal;
	}

	public ExperienceDroppingBlock iron() {
		return iron;
	}

	public ExperienceDroppingBlock gold() {
		return gold;
	}

	public ExperienceDroppingBlock copper() {
		return copper;
	}

	public ExperienceDroppingBlock lapis() {
		return lapis;
	}

	public RedstoneOreBlock redstone() {
		return redstone;
	}

	public ExperienceDroppingBlock emerald() {
		return emerald;
	}

	public ExperienceDroppingBlock diamond() {
		return diamond;
	}
}
