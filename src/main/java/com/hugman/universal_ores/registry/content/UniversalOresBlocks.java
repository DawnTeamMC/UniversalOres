package com.hugman.universal_ores.registry.content;

import com.hugman.universal_ores.block.NetherOres;
import com.hugman.universal_ores.block.OverworldOres;
import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;

public class UniversalOresBlocks {
	public static final OverworldOres GRANITE_ORES = new OverworldOres("granite", DawnBlockSettings.copyOf(Blocks.GRANITE).item().strength(3.0F, 3.0F));
	public static final OverworldOres DIORITE_ORES = new OverworldOres("diorite", DawnBlockSettings.copyOf(Blocks.DIORITE).item().strength(3.0F, 3.0F));
	public static final OverworldOres ANDESITE_ORES = new OverworldOres("andesite", DawnBlockSettings.copyOf(Blocks.ANDESITE).item().strength(3.0F, 3.0F));
	public static final OverworldOres TUFF_ORES = new OverworldOres("tuff", DawnBlockSettings.copyOf(Blocks.TUFF).item().strength(3.0F, 3.0F));
	public static final OverworldOres CALCITE_ORES = new OverworldOres("calcite", DawnBlockSettings.copyOf(Blocks.CALCITE).item().strength(0.5f, 0.5F));

	public static final NetherOres BLACKSTONE_ORES = new NetherOres("blackstone", DawnBlockSettings.copyOf(Blocks.BLACKSTONE).item().strength(3.0F, 3.0F));
	public static final NetherOres BASALT_ORES = new NetherOres("basalt", DawnBlockSettings.copyOf(Blocks.BASALT).item().strength(3.0F, 3.0F));

	public static void init(Registrar r) {
		GRANITE_ORES.register(r);
		DIORITE_ORES.register(r);
		ANDESITE_ORES.register(r);
		TUFF_ORES.register(r);
		CALCITE_ORES.register(r);

		BLACKSTONE_ORES.register(r);
		BASALT_ORES.register(r);

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
