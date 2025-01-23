package fr.hugman.universal_ores.util;

import com.google.common.collect.ImmutableList;
import fr.hugman.universal_ores.block.NetherOreBlocks;
import fr.hugman.universal_ores.block.OverworldOreBlocks;
import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class OreUtil {
    public static final RuleTest ANDESITE_RULE = new BlockMatchRuleTest(Blocks.ANDESITE);
    public static final RuleTest DIORITE_RULE = new BlockMatchRuleTest(Blocks.DIORITE);
    public static final RuleTest GRANITE_RULE = new BlockMatchRuleTest(Blocks.GRANITE);
    public static final RuleTest TUFF_RULE = new BlockMatchRuleTest(Blocks.TUFF);
    public static final RuleTest CALCITE_RULE = new BlockMatchRuleTest(Blocks.CALCITE);

    public static final RuleTest BLACKSTONE_RULE = new BlockMatchRuleTest(Blocks.BLACKSTONE);
    public static final RuleTest BASALT_RULE = new BlockMatchRuleTest(Blocks.BASALT);

    public static List<OreFeatureConfig.Target> getNewOverworldTargets(Function<OverworldOreBlocks, Block> state) {
        return List.of(
                OreFeatureConfig.createTarget(ANDESITE_RULE, state.apply(UniversalOresBlocks.ANDESITE_ORES).getDefaultState()),
                OreFeatureConfig.createTarget(DIORITE_RULE, state.apply(UniversalOresBlocks.DIORITE_ORES).getDefaultState()),
                OreFeatureConfig.createTarget(GRANITE_RULE, state.apply(UniversalOresBlocks.GRANITE_ORES).getDefaultState()),
                OreFeatureConfig.createTarget(TUFF_RULE, state.apply(UniversalOresBlocks.TUFF_ORES).getDefaultState()),
                OreFeatureConfig.createTarget(CALCITE_RULE, state.apply(UniversalOresBlocks.CALCITE_ORES).getDefaultState())
        );
    }

    public static List<OreFeatureConfig.Target> getNewNetherTargets(Function<NetherOreBlocks, Block> state) {
        return List.of(
                OreFeatureConfig.createTarget(BLACKSTONE_RULE, state.apply(UniversalOresBlocks.BLACKSTONE_ORES).getDefaultState()),
                OreFeatureConfig.createTarget(BASALT_RULE, state.apply(UniversalOresBlocks.BASALT_ORES).getDefaultState())
        );
    }

    @Nullable
    public static ImmutableList<OreFeatureConfig.Target> getNewTargets(List<OreFeatureConfig.Target> targets) {
        List<OreFeatureConfig.Target> newTargets = null;
        for (var target : targets) {
            newTargets = getNewTargetsByBlock(target.state.getBlock());
            if (newTargets != null) {
                break;
            }
        }
        if (newTargets == null) {
            return null;
        }

        var list = ImmutableList.<OreFeatureConfig.Target>builder()
                .addAll(newTargets)
                .addAll(targets);
        // old ones need to be added after the new ones, otherwise the new ones will be ignored
        // since the old ones are tags
        return list.build();
    }

    @Nullable
    private static List<OreFeatureConfig.Target> getNewTargetsByBlock(Block block) {
        if (block == Blocks.COAL_ORE) {
            return getNewOverworldTargets(OverworldOreBlocks::coal);
        }
        if (block == Blocks.IRON_ORE) {
            return getNewOverworldTargets(OverworldOreBlocks::iron);
        }
        if (block == Blocks.GOLD_ORE) {
            return getNewOverworldTargets(OverworldOreBlocks::gold);
        }
        if (block == Blocks.COPPER_ORE) {
            return getNewOverworldTargets(OverworldOreBlocks::copper);
        }
        if (block == Blocks.REDSTONE_ORE) {
            return getNewOverworldTargets(OverworldOreBlocks::redstone);
        }
        if (block == Blocks.LAPIS_ORE) {
            return getNewOverworldTargets(OverworldOreBlocks::lapis);
        }
        if (block == Blocks.DIAMOND_ORE) {
            return getNewOverworldTargets(OverworldOreBlocks::diamond);
        }
        if (block == Blocks.EMERALD_ORE) {
            return getNewOverworldTargets(OverworldOreBlocks::emerald);
        }
        if (block == Blocks.NETHER_GOLD_ORE) {
            return getNewNetherTargets(NetherOreBlocks::gold);
        }
        if (block == Blocks.NETHER_QUARTZ_ORE) {
            return getNewNetherTargets(NetherOreBlocks::quartz);
        }
        return null;
    }
}
