package fr.hugman.universal_ores.data.provider;

import com.google.common.collect.ImmutableList;
import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class UniversalOresRecipeGenerator extends RecipeGenerator {
    public UniversalOresRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }

    @Override
    public void generate() {
        offerOres(ImmutableList.copyOf(UniversalOresBlocks.COAL_ORES), RecipeCategory.MISC, Items.COAL, 0.1F, 200, "coal");
        offerOres(ImmutableList.copyOf(UniversalOresBlocks.IRON_ORES), RecipeCategory.MISC, Items.IRON_INGOT, 0.7F, 200, "iron_ingot");
        offerOres(ImmutableList.copyOf(
                Stream.concat(Arrays.stream(UniversalOresBlocks.GOLD_ORES), Arrays.stream(UniversalOresBlocks.NETHER_GOLD_ORES))
                        .toArray(ItemConvertible[]::new)
        ), RecipeCategory.MISC, Items.GOLD_INGOT, 1.0F, 200, "gold_ingot");
        offerOres(ImmutableList.copyOf(UniversalOresBlocks.COPPER_ORES), RecipeCategory.MISC, Items.COPPER_INGOT, 0.7F, 200, "copper_ingot");
        offerOres(ImmutableList.copyOf(UniversalOresBlocks.LAPIS_ORES), RecipeCategory.MISC, Items.LAPIS_LAZULI, 0.2F, 200, "lapis_lazuli");
        offerOres(ImmutableList.copyOf(UniversalOresBlocks.REDSTONE_ORES), RecipeCategory.REDSTONE, Items.REDSTONE, 0.7F, 200, "redstone");
        offerOres(ImmutableList.copyOf(UniversalOresBlocks.EMERALD_ORES), RecipeCategory.MISC, Items.EMERALD, 1.0F, 200, "emerald");
        offerOres(ImmutableList.copyOf(UniversalOresBlocks.DIAMOND_ORES), RecipeCategory.MISC, Items.DIAMOND, 1.0F, 200, "diamond");
        offerOres(ImmutableList.copyOf(UniversalOresBlocks.QUARTZ_ORES), RecipeCategory.MISC, Items.QUARTZ, 0.2F, 200, "quartz");
    }

    public void offerOres(List<ItemConvertible> ores, RecipeCategory category, ItemConvertible result, float experience, int cookingTime, String name) {
        offerMultipleOptionsFixed(RecipeSerializer.SMELTING, SmeltingRecipe::new, ores, category, result, experience, cookingTime, name, "_from_smelting");
        offerMultipleOptionsFixed(RecipeSerializer.BLASTING, BlastingRecipe::new, ores, category, result, experience, cookingTime / 2, name, "_from_blasting");
    }

    public <T extends AbstractCookingRecipe> void offerMultipleOptionsFixed(
            RecipeSerializer<T> serializer,
            AbstractCookingRecipe.RecipeFactory<T> recipeFactory,
            List<ItemConvertible> inputs,
            RecipeCategory category,
            ItemConvertible output,
            float experience,
            int cookingTime,
            String group,
            String suffix
    ) {
        for (ItemConvertible itemConvertible : inputs) {
            CookingRecipeJsonBuilder.create(Ingredient.ofItems(itemConvertible), category, output, experience, cookingTime, serializer, recipeFactory)
                    .group(group)
                    .criterion(hasItem(itemConvertible), conditionsFromItem(itemConvertible))
                    .offerTo(this.exporter, RegistryKey.of(RegistryKeys.RECIPE, UniversalOres.id(getItemPath(output) + suffix + "_" + getItemPath(itemConvertible))));
        }
    }

    public static FabricRecipeProvider create(FabricDataOutput fabricDataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        return new FabricRecipeProvider(fabricDataOutput, completableFuture) {
            @Override
            protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
                return new UniversalOresRecipeGenerator(wrapperLookup, recipeExporter);
            }

            @Override
            public String getName() {
                return "Recipes";
            }
        };
    }
}
