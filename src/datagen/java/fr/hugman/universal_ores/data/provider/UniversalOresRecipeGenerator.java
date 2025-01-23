package fr.hugman.universal_ores.data.provider;

import com.google.common.collect.ImmutableList;
import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class UniversalOresRecipeGenerator extends FabricRecipeProvider {
    public UniversalOresRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        offerOres(exporter, ImmutableList.copyOf(UniversalOresBlocks.COAL_ORES), RecipeCategory.MISC, Items.COAL, 0.1F, 200, "coal");
        offerOres(exporter, ImmutableList.copyOf(UniversalOresBlocks.IRON_ORES), RecipeCategory.MISC, Items.IRON_INGOT, 0.7F, 200, "iron_ingot");
        offerOres(exporter, ImmutableList.copyOf(
                Stream.concat(Arrays.stream(UniversalOresBlocks.GOLD_ORES), Arrays.stream(UniversalOresBlocks.NETHER_GOLD_ORES))
                        .toArray(ItemConvertible[]::new)
        ), RecipeCategory.MISC, Items.GOLD_INGOT, 1.0F, 200, "gold_ingot");
        offerOres(exporter, ImmutableList.copyOf(UniversalOresBlocks.COPPER_ORES), RecipeCategory.MISC, Items.COPPER_INGOT, 0.7F, 200, "copper_ingot");
        offerOres(exporter, ImmutableList.copyOf(UniversalOresBlocks.LAPIS_ORES), RecipeCategory.MISC, Items.LAPIS_LAZULI, 0.2F, 200, "lapis_lazuli");
        offerOres(exporter, ImmutableList.copyOf(UniversalOresBlocks.REDSTONE_ORES), RecipeCategory.REDSTONE, Items.REDSTONE, 0.7F, 200, "redstone");
        offerOres(exporter, ImmutableList.copyOf(UniversalOresBlocks.EMERALD_ORES), RecipeCategory.MISC, Items.EMERALD, 1.0F, 200, "emerald");
        offerOres(exporter, ImmutableList.copyOf(UniversalOresBlocks.DIAMOND_ORES), RecipeCategory.MISC, Items.DIAMOND, 1.0F, 200, "diamond");
        offerOres(exporter, ImmutableList.copyOf(UniversalOresBlocks.QUARTZ_ORES), RecipeCategory.MISC, Items.QUARTZ, 0.2F, 200, "quartz");
    }

    public void offerOres(RecipeExporter exporter, List<ItemConvertible> ores, RecipeCategory category, ItemConvertible result, float experience, int cookingTime, String name) {
        offerMultipleOptions(exporter, RecipeSerializer.SMELTING, SmeltingRecipe::new, ores, category, result, experience, cookingTime, name, "_from_smelting");
        offerMultipleOptions(exporter, RecipeSerializer.BLASTING, BlastingRecipe::new, ores, category, result, experience, cookingTime / 2, name, "_from_blasting");
    }

    public static <T extends AbstractCookingRecipe> void offerMultipleOptions(
            RecipeExporter exporter,
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
                    .offerTo(exporter, UniversalOres.id(getItemPath(output) + suffix + "_" + getItemPath(itemConvertible)));
        }
    }
}
