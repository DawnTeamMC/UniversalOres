package fr.hugman.universal_ores.data.provider;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.data.BuiltInOreVariants;
import fr.hugman.universal_ores.variant.OreVariantData;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.triggers.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * The same cooking recipes as the ones made at load time for the variants of other mods: a copy of each vanilla
 * cooking recipe of an ore, for its variants. They are unlocked in the recipe book like the original ones.
 */
public class UniversalOresRecipeProvider extends FabricRecipeProvider {
    public UniversalOresRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider registries, @NotNull RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                DynamicOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);
                List<RecipeHolder<?>> vanilla = new ArrayList<>();
                BuiltInOreVariants.forEachVanillaFile(Registries.elementsDirPath(Registries.RECIPE), (id, json) ->
                        Recipe.CODEC.parse(ops, json).ifSuccess(recipe -> vanilla.add(new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, id), recipe))));
                // The recipe book folder of each vanilla recipe, from where its advancement is
                Map<Identifier, String> folders = new HashMap<>();
                BuiltInOreVariants.forEachVanillaFile("advancement/recipes", (id, json) -> {
                    int slash = id.getPath().indexOf('/');
                    if (slash >= 0) {
                        folders.put(id.withPath(id.getPath().substring(slash + 1)), id.getPath().substring(0, slash));
                    }
                });

                Map<Identifier, RecipeHolder<?>> originals = new HashMap<>();
                vanilla.forEach(holder -> originals.put(UniversalOres.id(holder.id().identifier().getNamespace() + "/" + holder.id().identifier().getPath()), holder));
                List<RegisteredOreVariant> variants = BuiltInOreVariants.all();
                for (RecipeHolder<?> holder : OreVariantData.withCookingRecipes(vanilla, registries, variants)) {
                    Identifier id = holder.id().identifier();
                    if (!id.getNamespace().equals(UniversalOres.MOD_ID) || !(holder.value() instanceof AbstractCookingRecipe cooking)) {
                        continue;
                    }
                    String folder = folders.getOrDefault(originals.get(id).id().identifier(), "misc");
                    Advancement.Builder advancement = output.advancement()
                            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(holder.id()))
                            .rewards(AdvancementRewards.Builder.recipe(holder.id()))
                            .requirements(AdvancementRequirements.Strategy.OR);
                    for (RegisteredOreVariant variant : variants) {
                        if (cooking.input().acceptsItem(variant.block().asItem().builtInRegistryHolder())) {
                            advancement.addCriterion(getHasName(variant.block()), this.has(variant.block()));
                        }
                    }
                    output.accept(holder.id(), holder.value(), advancement.build(id.withPrefix("recipes/" + folder + "/")));
                }
            }
        };
    }

    @Override
    public @NotNull String getName() {
        return "Recipes";
    }
}
