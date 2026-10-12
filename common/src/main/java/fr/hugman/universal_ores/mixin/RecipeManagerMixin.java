package fr.hugman.universal_ores.mixin;

import fr.hugman.universal_ores.variant.OreVariantData;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * NeoForge reads recipes its own way, so the recipes are only extended once they are all read.
 */
@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @ModifyArg(method = "prepare", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeMap;create(Ljava/lang/Iterable;)Lnet/minecraft/world/item/crafting/RecipeMap;"))
    private Iterable<RecipeHolder<?>> addOreVariantRecipes(Iterable<RecipeHolder<?>> recipes) {
        return OreVariantData.withCookingRecipes(recipes, this.registries);
    }
}
