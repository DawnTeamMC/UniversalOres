package fr.hugman.universal_ores.mixin;

import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.material.rule.OreVeinRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(OreVeinRule.VeinType.class)
public class VeinTypeMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static BlockState modifyOre(BlockState oreBlock) {
        if (oreBlock.is(Blocks.COPPER_ORE)) {
            return UniversalOresBlocks.GRANITE_ORES.copper().defaultBlockState();
        }
        if (oreBlock.is(Blocks.DEEPSLATE_IRON_ORE)) {
            return UniversalOresBlocks.TUFF_ORES.iron().defaultBlockState();
        }
        return oreBlock;
    }
}