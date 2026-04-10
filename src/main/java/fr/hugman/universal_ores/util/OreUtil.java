package fr.hugman.universal_ores.util;

import com.google.common.collect.ImmutableList;
import fr.hugman.universal_ores.block.NetherOreBlocks;
import fr.hugman.universal_ores.block.OverworldOreBlocks;
import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class OreUtil {
    public static final BlockMatchTest ANDESITE_RULE = new BlockMatchTest(Blocks.ANDESITE);
    public static final BlockMatchTest DIORITE_RULE = new BlockMatchTest(Blocks.DIORITE);
    public static final BlockMatchTest GRANITE_RULE = new BlockMatchTest(Blocks.GRANITE);
    public static final BlockMatchTest TUFF_RULE = new BlockMatchTest(Blocks.TUFF);
    public static final BlockMatchTest CALCITE_RULE = new BlockMatchTest(Blocks.CALCITE);

    public static final BlockMatchTest BLACKSTONE_RULE = new BlockMatchTest(Blocks.BLACKSTONE);
    public static final BlockMatchTest BASALT_RULE = new BlockMatchTest(Blocks.BASALT);

    public static List<OreConfiguration.TargetBlockState> getNewOverworldTargets(Function<OverworldOreBlocks, Block> state) {
        return List.of(
                OreConfiguration.target(ANDESITE_RULE, state.apply(UniversalOresBlocks.ANDESITE_ORES).defaultBlockState()),
                OreConfiguration.target(DIORITE_RULE, state.apply(UniversalOresBlocks.DIORITE_ORES).defaultBlockState()),
                OreConfiguration.target(GRANITE_RULE, state.apply(UniversalOresBlocks.GRANITE_ORES).defaultBlockState()),
                OreConfiguration.target(TUFF_RULE, state.apply(UniversalOresBlocks.TUFF_ORES).defaultBlockState()),
                OreConfiguration.target(CALCITE_RULE, state.apply(UniversalOresBlocks.CALCITE_ORES).defaultBlockState())
        );
    }

    public static List<OreConfiguration.TargetBlockState> getNewNetherTargets(Function<NetherOreBlocks, Block> state) {
        return List.of(
                OreConfiguration.target(BLACKSTONE_RULE, state.apply(UniversalOresBlocks.BLACKSTONE_ORES).defaultBlockState()),
                OreConfiguration.target(BASALT_RULE, state.apply(UniversalOresBlocks.BASALT_ORES).defaultBlockState())
        );
    }

    @Nullable
    public static ImmutableList<OreConfiguration.TargetBlockState> getNewTargets(List<OreConfiguration.TargetBlockState> targets) {
        List<OreConfiguration.TargetBlockState> newTargets = null;
        for (var target : targets) {
            newTargets = getNewTargetsByBlock(target.state.getBlock());
            if (newTargets != null) {
                break;
            }
        }
        if (newTargets == null) {
            return null;
        }

        var list = ImmutableList.<OreConfiguration.TargetBlockState>builder()
                .addAll(newTargets)
                .addAll(targets);
        // old ones need to be added after the new ones, otherwise the new ones will be ignored
        // since the old ones are tags
        return list.build();
    }

    @Nullable
    private static List<OreConfiguration.TargetBlockState> getNewTargetsByBlock(Block block) {
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
