package com.hugman.universal_ores.util;

import com.hugman.universal_ores.creator.bundle.block.OverworldOreBundle;
import com.hugman.universal_ores.init.UniversalOresBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OreUtil {
	public static final RuleTest ANDESITE_RULE = new BlockMatchRuleTest(Blocks.ANDESITE);
	public static final RuleTest DIORITE_RULE = new BlockMatchRuleTest(Blocks.DIORITE);
	public static final RuleTest GRANITE_RULE = new BlockMatchRuleTest(Blocks.GRANITE);
	public static final RuleTest TUFF_RULE = new BlockMatchRuleTest(Blocks.TUFF);
	public static final RuleTest CALCITE_RULE = new BlockMatchRuleTest(Blocks.CALCITE);

	public static List<OreFeatureConfig.Target> getNewOverworldTargets(Function<OverworldOreBundle, Block> state) {
		return List.of(
				OreFeatureConfig.createTarget(ANDESITE_RULE, state.apply(UniversalOresBlocks.ANDESITE_ORES).getDefaultState()),
				OreFeatureConfig.createTarget(DIORITE_RULE, state.apply(UniversalOresBlocks.DIORITE_ORES).getDefaultState()),
				OreFeatureConfig.createTarget(GRANITE_RULE, state.apply(UniversalOresBlocks.GRANITE_ORES).getDefaultState()),
				OreFeatureConfig.createTarget(TUFF_RULE, state.apply(UniversalOresBlocks.TUFF_ORES).getDefaultState()),
				OreFeatureConfig.createTarget(CALCITE_RULE, state.apply(UniversalOresBlocks.CALCITE_ORES).getDefaultState()));
	}
}
