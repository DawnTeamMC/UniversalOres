package fr.hugman.universal_ores.util;

import com.google.common.collect.ImmutableList;
import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class OreUtil {
    /**
     * Adds, to an ore feature placing some ore, the variants of that ore in their own stones.
     * This works for any ore feature, whether vanilla's, a mod's or a data pack's.
     */
    @Nullable
    public static ImmutableList<OreConfiguration.TargetBlockState> getNewTargets(List<OreConfiguration.TargetBlockState> targets) {
        if (!UniversalOres.isInitialized()) {
            return null;
        }
        List<OreConfiguration.TargetBlockState> newTargets = new ArrayList<>();
        for (var target : targets) {
            Block ore = target.state.getBlock();
            for (RegisteredOreVariant variant : OreVariantRegistrar.registered()) {
                if (variant.ore() == ore) {
                    newTargets.add(OreConfiguration.target(new BlockMatchTest(variant.stone()), variant.block().defaultBlockState()));
                }
            }
            if (!newTargets.isEmpty()) {
                break;
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
}
