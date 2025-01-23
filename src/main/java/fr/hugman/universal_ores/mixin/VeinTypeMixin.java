package fr.hugman.universal_ores.mixin;

import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.OreVeinSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(OreVeinSampler.VeinType.class)
public class VeinTypeMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static BlockState modifyOre(BlockState ore) {
        if (ore.isOf(Blocks.COPPER_ORE)) {
            return UniversalOresBlocks.GRANITE_ORES.copper().getDefaultState();
        }
        if (ore.isOf(Blocks.DEEPSLATE_IRON_ORE)) {
            return UniversalOresBlocks.TUFF_ORES.iron().getDefaultState();
        }
        return ore;
    }
}