package fr.hugman.universal_ores.mixin;

import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.OreVeinifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Large ore veins use granite copper ore and tuff iron ore, like their filler stones.
 */
@Mixin(OreVeinifier.VeinType.class)
public class VeinTypeMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static BlockState modifyOre(BlockState ore) {
        if (!UniversalOres.isInitialized()) {
            return ore;
        }
        if (ore.is(Blocks.COPPER_ORE)) {
            return variant("granite_copper_ore", ore);
        }
        if (ore.is(Blocks.DEEPSLATE_IRON_ORE)) {
            return variant("tuff_iron_ore", ore);
        }
        return ore;
    }

    private static BlockState variant(String path, BlockState fallback) {
        return OreVariantRegistrar.get(UniversalOres.id(path)).map(RegisteredOreVariant::block).map(block -> block.defaultBlockState()).orElse(fallback);
    }
}
