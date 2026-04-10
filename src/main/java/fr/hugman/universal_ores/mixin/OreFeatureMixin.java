package fr.hugman.universal_ores.mixin;

import fr.hugman.universal_ores.util.OreUtil;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(OreConfiguration.class)
public class OreFeatureMixin {
    @ModifyVariable(method = "<init>(Ljava/util/List;IF)V", at = @At("HEAD"), argsOnly = true)
    private static List<OreConfiguration.TargetBlockState> modifyTargets(List<OreConfiguration.TargetBlockState> targetBlockStates) {
        var newTargets = OreUtil.getNewTargets(targetBlockStates);
        if (newTargets != null) {
            return newTargets;
        }
        return targetBlockStates;
    }
}
