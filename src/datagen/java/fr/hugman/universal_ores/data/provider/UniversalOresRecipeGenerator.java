package fr.hugman.universal_ores.data.provider;

import com.google.common.collect.ImmutableList;
import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class UniversalOresRecipeGenerator extends FabricRecipeProvider {
    public UniversalOresRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override @NotNull
    protected RecipeProvider createRecipeProvider(@NotNull HolderLookup.Provider registries, @NotNull RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                offerOres(this, output, ImmutableList.copyOf(UniversalOresBlocks.COAL_ORES), RecipeCategory.MISC, Items.COAL, 0.1F, 200, "coal");
                offerOres(this, output, ImmutableList.copyOf(UniversalOresBlocks.IRON_ORES), RecipeCategory.MISC, Items.IRON_INGOT, 0.7F, 200, "iron_ingot");
                offerOres(this, output, ImmutableList.copyOf(
                        Stream.concat(Arrays.stream(UniversalOresBlocks.GOLD_ORES), Arrays.stream(UniversalOresBlocks.NETHER_GOLD_ORES))
                                .toArray(ItemLike[]::new)
                ), RecipeCategory.MISC, Items.GOLD_INGOT, 1.0F, 200, "gold_ingot");
                offerOres(this, output, ImmutableList.copyOf(UniversalOresBlocks.COPPER_ORES), RecipeCategory.MISC, Items.COPPER_INGOT, 0.7F, 200, "copper_ingot");
                offerOres(this, output, ImmutableList.copyOf(UniversalOresBlocks.LAPIS_ORES), RecipeCategory.MISC, Items.LAPIS_LAZULI, 0.2F, 200, "lapis_lazuli");
                offerOres(this, output, ImmutableList.copyOf(UniversalOresBlocks.REDSTONE_ORES), RecipeCategory.REDSTONE, Items.REDSTONE, 0.7F, 200, "redstone");
                offerOres(this, output, ImmutableList.copyOf(UniversalOresBlocks.EMERALD_ORES), RecipeCategory.MISC, Items.EMERALD, 1.0F, 200, "emerald");
                offerOres(this, output, ImmutableList.copyOf(UniversalOresBlocks.DIAMOND_ORES), RecipeCategory.MISC, Items.DIAMOND, 1.0F, 200, "diamond");
                offerOres(this, output, ImmutableList.copyOf(UniversalOresBlocks.QUARTZ_ORES), RecipeCategory.MISC, Items.QUARTZ, 0.2F, 200, "quartz");
            }
        };
    }

    public void offerOres(RecipeProvider provider, RecipeOutput output, List<ItemLike> ores, RecipeCategory category, ItemLike result, float experience, int cookingTime, String name) {
        offerMultipleOptionsFixed(provider, output, SmeltingRecipe::new, CookingBookCategory.BLOCKS, ores, category, result, experience, cookingTime, name, "_from_smelting");
        offerMultipleOptionsFixed(provider, output, BlastingRecipe::new, CookingBookCategory.BLOCKS, ores, category, result, experience, cookingTime / 2, name, "_from_blasting");
    }

    public <T extends AbstractCookingRecipe> void offerMultipleOptionsFixed(
            RecipeProvider provider,
            RecipeOutput output,
            AbstractCookingRecipe.Factory<T> recipeFactory,
            CookingBookCategory cookingCategory,
            List<ItemLike> inputs,
            RecipeCategory category,
            ItemLike outputItem,
            float experience,
            int cookingTime,
            String group,
            String suffix
    ) {
        for (ItemLike itemConvertible : inputs) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemConvertible), category, cookingCategory, outputItem, experience, cookingTime, recipeFactory)
                    .group(group)
                    .unlockedBy(RecipeProvider.getHasName(itemConvertible), provider.has(itemConvertible))
                    .save(output, ResourceKey.create(Registries.RECIPE, UniversalOres.id(RecipeProvider.getItemName(outputItem) + suffix + "_" + RecipeProvider.getItemName(itemConvertible))));
        }
    }

    @Override @NotNull
    public String getName() {
        return "Recipes";
    }
}
