package net.universal_ores.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.universal_ores.block.ModBlocks;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GRANITE_COAL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GRANITE_COPPER_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GRANITE_LAPIS_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GRANITE_REDSTONE_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GRANITE_IRON_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GRANITE_GOLD_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GRANITE_EMERALD_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GRANITE_DIAMOND_ORE);


        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DIORITE_COAL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DIORITE_COPPER_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DIORITE_LAPIS_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DIORITE_REDSTONE_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DIORITE_IRON_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DIORITE_GOLD_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DIORITE_EMERALD_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DIORITE_DIAMOND_ORE);




        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ANDESITE_COAL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ANDESITE_COPPER_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ANDESITE_LAPIS_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ANDESITE_REDSTONE_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ANDESITE_IRON_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ANDESITE_GOLD_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ANDESITE_EMERALD_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ANDESITE_DIAMOND_ORE);




        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CALCITE_COAL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CALCITE_COPPER_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CALCITE_LAPIS_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CALCITE_REDSTONE_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CALCITE_IRON_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CALCITE_GOLD_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CALCITE_EMERALD_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CALCITE_DIAMOND_ORE);





        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TUFF_COAL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TUFF_COPPER_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TUFF_LAPIS_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TUFF_REDSTONE_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TUFF_IRON_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TUFF_GOLD_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TUFF_EMERALD_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TUFF_DIAMOND_ORE);



        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BLACKSTONE_QUARTZ_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BLACKSTONE_GOLD_ORE);


        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BASALT_QUARTZ_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BASALT_GOLD_ORE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
