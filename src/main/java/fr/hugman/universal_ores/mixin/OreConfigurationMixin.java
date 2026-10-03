package fr.hugman.universal_ores.mixin;

import fr.hugman.universal_ores.util.OreUtil;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(OreFeature.class)
public class OreConfigurationMixin {
    @ModifyVariable(method = "<init>(Ljava/util/List;IF)V", at = @At("HEAD"), argsOnly = true)
    private static List<BlockReplacement> modifyTargets(List<BlockReplacement> targetStates) {
        var newTargets = OreUtil.getNewTargets(targetStates);
        if (newTargets != null) {
            return newTargets;
        }
        return targetStates;
    }
}
