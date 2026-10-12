package fr.hugman.universal_ores.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import fr.hugman.universal_ores.variant.OreVariantData;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @WrapOperation(
            method = "prepare",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/SimpleJsonResourceReloadListener;scanDirectory(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/FileToIdConverter;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V")
    )
    private void addOreVariantRecipes(ResourceManager manager, FileToIdConverter lister, DynamicOps<JsonElement> ops, Codec<Recipe<?>> codec, Map<Identifier, Recipe<?>> result, Operation<Void> original) {
        original.call(manager, lister, ops, codec, result);
        OreVariantData.addCookingRecipes(result, this.registries);
    }
}
