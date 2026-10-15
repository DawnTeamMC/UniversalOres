package fr.hugman.universal_ores.fabric.mixin;

import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fabric freezes the registries once every mod is initialized: the ore variants are registered right before, when
 * every stone and ore is there. NeoForge does this at the lowest priority of the block registration instead.
 */
@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {
    @Inject(method = "freeze", at = @At("HEAD"))
    private static void registerOreVariants(CallbackInfo ci) {
        OreVariantRegistrar.registerAvailable();
    }
}
