package net.universal_ores.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.universal_ores.block.ModBlocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {





        List<ItemConvertible> DIAMOND = List.of(ModBlocks.ANDESITE_DIAMOND_ORE, ModBlocks.DIORITE_DIAMOND_ORE, ModBlocks.GRANITE_DIAMOND_ORE, ModBlocks.TUFF_DIAMOND_ORE, ModBlocks.CALCITE_DIAMOND_ORE);


        offerSmelting(exporter,DIAMOND, RecipeCategory.MISC, Items.DIAMOND, 1.0f, 200, "diamond");
        offerBlasting(exporter,DIAMOND, RecipeCategory.MISC, Items.DIAMOND, 1.0f, 100, "diamond");



        List<ItemConvertible> GOLD = List.of(
                ModBlocks.ANDESITE_GOLD_ORE,ModBlocks.DIORITE_GOLD_ORE,ModBlocks.GRANITE_GOLD_ORE,ModBlocks.TUFF_GOLD_ORE,ModBlocks.CALCITE_GOLD_ORE);

        offerSmelting(exporter,GOLD, RecipeCategory.MISC, Items.GOLD_INGOT, 1.0f, 200, "gold");
        offerBlasting(exporter,GOLD, RecipeCategory.MISC, Items.GOLD_INGOT, 1.0f, 100, "gold");




        List<ItemConvertible> IRON = List.of(
                ModBlocks.ANDESITE_IRON_ORE,
                ModBlocks.DIORITE_IRON_ORE,
                ModBlocks.GRANITE_IRON_ORE,
                ModBlocks.TUFF_IRON_ORE,
                ModBlocks.CALCITE_IRON_ORE
        );

        offerSmelting(exporter,IRON, RecipeCategory.MISC, Items.IRON_INGOT, 0.7f, 200, "iron");
        offerBlasting(exporter,IRON, RecipeCategory.MISC, Items.IRON_INGOT, 0.7f, 100, "iron");




        List<ItemConvertible> LAPIS = List.of(
                ModBlocks.ANDESITE_LAPIS_ORE,
                ModBlocks.DIORITE_LAPIS_ORE,
                ModBlocks.GRANITE_LAPIS_ORE,
                ModBlocks.TUFF_LAPIS_ORE,
                ModBlocks.CALCITE_LAPIS_ORE
        );

        offerSmelting(exporter,LAPIS, RecipeCategory.MISC, Items.LAPIS_LAZULI, 0.2f, 200, "lapis");
        offerBlasting(exporter,LAPIS, RecipeCategory.MISC, Items.LAPIS_LAZULI, 0.2f, 100, "lapis");

        List<ItemConvertible> REDSTONE = List.of(
                ModBlocks.ANDESITE_REDSTONE_ORE,
                ModBlocks.DIORITE_REDSTONE_ORE,
                ModBlocks.GRANITE_REDSTONE_ORE,
                ModBlocks.TUFF_REDSTONE_ORE,
                ModBlocks.CALCITE_REDSTONE_ORE
        );

        offerSmelting(exporter,REDSTONE, RecipeCategory.REDSTONE, Items.REDSTONE, 0.7f, 200, "redstone");
        offerBlasting(exporter,REDSTONE, RecipeCategory.REDSTONE, Items.REDSTONE, 0.7f, 100, "redstone");




        // Emerald
        List<ItemConvertible> EMERALD = List.of(
                ModBlocks.ANDESITE_EMERALD_ORE,
                ModBlocks.DIORITE_EMERALD_ORE,
                ModBlocks.GRANITE_EMERALD_ORE,
                ModBlocks.TUFF_EMERALD_ORE,
                ModBlocks.CALCITE_EMERALD_ORE
        );

        offerSmelting(exporter,EMERALD, RecipeCategory.MISC, Items.EMERALD, 1.0f, 200, "emerald");
        offerBlasting(exporter,EMERALD, RecipeCategory.MISC, Items.EMERALD, 1.0f, 100, "emerald");

// Coal
        List<ItemConvertible> COAL = List.of(
                ModBlocks.ANDESITE_COAL_ORE,
                ModBlocks.DIORITE_COAL_ORE,
                ModBlocks.GRANITE_COAL_ORE,
                ModBlocks.TUFF_COAL_ORE,
                ModBlocks.CALCITE_COAL_ORE
        );

        offerSmelting(exporter,COAL, RecipeCategory.MISC, Items.COAL, 0.1f, 200, "coal");
        offerBlasting(exporter,COAL, RecipeCategory.MISC, Items.COAL, 0.1f, 100, "coal");

// Copper
        List<ItemConvertible> COPPER = List.of(
                ModBlocks.ANDESITE_COPPER_ORE,
                ModBlocks.DIORITE_COPPER_ORE,
                ModBlocks.GRANITE_COPPER_ORE,
                ModBlocks.TUFF_COPPER_ORE,
                ModBlocks.CALCITE_COPPER_ORE
        );

        offerSmelting(exporter,COPPER, RecipeCategory.MISC, Items.COPPER_INGOT, 0.7f, 200, "copper");
        offerBlasting(exporter,COPPER, RecipeCategory.MISC, Items.COPPER_INGOT, 0.7f, 100, "copper");



        List<ItemConvertible> GOLD_NUGGET  = List.of(
                ModBlocks.BLACKSTONE_GOLD_ORE,
                ModBlocks.BASALT_GOLD_ORE
        );


        offerSmelting(exporter,GOLD_NUGGET, RecipeCategory.MISC, Items.GOLD_INGOT, 1f, 200, "gold_nugget");
        offerBlasting(exporter,GOLD_NUGGET, RecipeCategory.MISC, Items.GOLD_INGOT, 1f, 100, "gold_nugget");



        List<ItemConvertible> QUARTZ  = List.of(
                ModBlocks.BLACKSTONE_QUARTZ_ORE,
                ModBlocks.BASALT_QUARTZ_ORE
        );


        offerSmelting(exporter,QUARTZ, RecipeCategory.MISC, Items.QUARTZ, 0.2f, 200, "quartz");
        offerBlasting(exporter,QUARTZ, RecipeCategory.MISC, Items.QUARTZ, 0.2f, 100, "quartz");

    }
}
