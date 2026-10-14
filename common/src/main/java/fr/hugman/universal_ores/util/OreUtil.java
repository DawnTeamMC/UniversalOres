package fr.hugman.universal_ores.util;

import com.google.common.collect.ImmutableList;
import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;

import javax.annotation.Nullable;
import java.util.*;

public class OreUtil {
    /**
     * Adds the ore variants to an ore feature, so that they generate whichever of the ore and the stone is placed
     * first:
     * <ul>
     *     <li>to a feature placing some ore, the variants of that ore in their own stones;</li>
     *     <li>to a feature placing a stone (like the blobs of granite, or of a mod's marble), the variants in that
     *     stone of the ores it covers.</li>
     * </ul>
     * This works for any ore feature, whether vanilla's, a mod's or a data pack's.
     */
    @Nullable
    public static ImmutableList<OreConfiguration.TargetBlockState> getNewTargets(List<OreConfiguration.TargetBlockState> targets) {
        if (!UniversalOres.isInitialized()) {
            return null;
        }
        return getNewTargets(targets, OreVariantRegistrar.registered());
    }

    @Nullable
    public static ImmutableList<OreConfiguration.TargetBlockState> getNewTargets(List<OreConfiguration.TargetBlockState> targets, Collection<RegisteredOreVariant> variants) {
        // A variant the feature already places (like when it is built again from a configuration that has ours) isn't added twice
        Set<Block> placed = new HashSet<>();
        targets.forEach(target -> placed.add(target.state.getBlock()));
        List<OreConfiguration.TargetBlockState> newTargets = new ArrayList<>();

        for (var target : targets) {
            Block ore = target.state.getBlock();
            boolean isOre = false;
            for (RegisteredOreVariant variant : variants) {
                if (variant.ore() == ore) {
                    isOre = true;
                    if (placed.add(variant.block())) {
                        newTargets.add(OreConfiguration.target(new BlockMatchTest(variant.stone()), variant.block().defaultBlockState()));
                    }
                }
            }
            if (isOre) {
                break;
            }
        }

        // Mods add their stones at the end of the ore step, after the ores of vanilla (and often of other mods):
        // their blobs would otherwise cover those ores with ores of plain stone
        for (var target : targets) {
            Block stone = target.state.getBlock();
            for (RegisteredOreVariant variant : variants) {
                if (variant.stone() == stone && placed.add(variant.block())) {
                    newTargets.add(OreConfiguration.target(new BlockMatchTest(variant.ore()), variantIn(variant, target.state)));
                }
            }
        }

        if (newTargets.isEmpty()) {
            return null;
        }
        // The original targets come last: they often match a whole tag of stones, which would take over ours
        return ImmutableList.<OreConfiguration.TargetBlockState>builder()
                .addAll(newTargets)
                .addAll(targets)
                .build();
    }

    /**
     * The variant replacing an ore covered by its stone, lying like the stone does for pillar stones.
     */
    private static BlockState variantIn(RegisteredOreVariant variant, BlockState stone) {
        BlockState state = variant.block().defaultBlockState();
        if (state.hasProperty(BlockStateProperties.AXIS) && stone.hasProperty(BlockStateProperties.AXIS)) {
            return state.setValue(BlockStateProperties.AXIS, stone.getValue(BlockStateProperties.AXIS));
        }
        return state;
    }
}
