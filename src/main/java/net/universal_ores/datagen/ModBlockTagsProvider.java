package net.universal_ores.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.universal_ores.block.ModBlocks;
import net.universal_ores.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {



        getOrCreateTagBuilder(ModTags.BLACKSTONE_ORE_REPLACEABLES)
                .add(Blocks.BLACKSTONE);
        getOrCreateTagBuilder(ModTags.BASALT_ORE_REPLACEABLES)
                .add(Blocks.BASALT);

        getOrCreateTagBuilder(ModTags.DIORITE_ORE_REPLACEABLES)
                .add(Blocks.DIORITE);

        getOrCreateTagBuilder(ModTags.ANDESITE_ORE_REPLACEABLES)
                .add(Blocks.ANDESITE);

        getOrCreateTagBuilder(ModTags.GRANITE_ORE_REPLACEABLES)
                .add(Blocks.GRANITE);

        getOrCreateTagBuilder(ModTags.CALCITE_ORE_REPLACEABLES)
                .add(Blocks.CALCITE);

        getOrCreateTagBuilder(ModTags.TUFF_ORE_REPLACEABLES)
                .add(Blocks.TUFF);




        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.GRANITE_DIAMOND_ORE)
                .add(ModBlocks.GRANITE_GOLD_ORE)
                .add(ModBlocks.GRANITE_COAL_ORE)
                .add(ModBlocks.GRANITE_COPPER_ORE)
                .add(ModBlocks.GRANITE_EMERALD_ORE)
                .add(ModBlocks.GRANITE_LAPIS_ORE)
                .add(ModBlocks.GRANITE_IRON_ORE)
                .add(ModBlocks.GRANITE_REDSTONE_ORE)


                .add(ModBlocks.DIORITE_DIAMOND_ORE)
                .add(ModBlocks.DIORITE_GOLD_ORE)
                .add(ModBlocks.DIORITE_COAL_ORE)
                .add(ModBlocks.DIORITE_COPPER_ORE)
                .add(ModBlocks.DIORITE_EMERALD_ORE)
                .add(ModBlocks.DIORITE_LAPIS_ORE)
                .add(ModBlocks.DIORITE_IRON_ORE)
                .add(ModBlocks.DIORITE_REDSTONE_ORE)



                .add(ModBlocks.ANDESITE_DIAMOND_ORE)
                .add(ModBlocks.ANDESITE_GOLD_ORE)
                .add(ModBlocks.ANDESITE_COAL_ORE)
                .add(ModBlocks.ANDESITE_COPPER_ORE)
                .add(ModBlocks.ANDESITE_EMERALD_ORE)
                .add(ModBlocks.ANDESITE_LAPIS_ORE)
                .add(ModBlocks.ANDESITE_IRON_ORE)
                .add(ModBlocks.ANDESITE_REDSTONE_ORE)



                .add(ModBlocks.CALCITE_DIAMOND_ORE)
                .add(ModBlocks.CALCITE_GOLD_ORE)
                .add(ModBlocks.CALCITE_COAL_ORE)
                .add(ModBlocks.CALCITE_COPPER_ORE)
                .add(ModBlocks.CALCITE_EMERALD_ORE)
                .add(ModBlocks.CALCITE_LAPIS_ORE)
                .add(ModBlocks.CALCITE_IRON_ORE)
                .add(ModBlocks.CALCITE_REDSTONE_ORE)




                .add(ModBlocks.TUFF_DIAMOND_ORE)
                .add(ModBlocks.TUFF_GOLD_ORE)
                .add(ModBlocks.TUFF_COAL_ORE)
                .add(ModBlocks.TUFF_COPPER_ORE)
                .add(ModBlocks.TUFF_EMERALD_ORE)
                .add(ModBlocks.TUFF_LAPIS_ORE)
                .add(ModBlocks.TUFF_IRON_ORE)
                .add(ModBlocks.TUFF_REDSTONE_ORE)




                .add(ModBlocks.BLACKSTONE_QUARTZ_ORE)
                .add(ModBlocks.BLACKSTONE_GOLD_ORE)



                .add(ModBlocks.BASALT_QUARTZ_ORE)
                .add(ModBlocks.BASALT_GOLD_ORE);




        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.GRANITE_LAPIS_ORE)
                .add(ModBlocks.GRANITE_IRON_ORE)
                .add(ModBlocks.GRANITE_COPPER_ORE)


                .add(ModBlocks.DIORITE_LAPIS_ORE)
                .add(ModBlocks.DIORITE_IRON_ORE)
                .add(ModBlocks.DIORITE_COPPER_ORE)


                .add(ModBlocks.ANDESITE_LAPIS_ORE)
                .add(ModBlocks.ANDESITE_IRON_ORE)
                .add(ModBlocks.ANDESITE_COPPER_ORE)



                .add(ModBlocks.CALCITE_LAPIS_ORE)
                .add(ModBlocks.CALCITE_IRON_ORE)
                .add(ModBlocks.CALCITE_COPPER_ORE)


                .add(ModBlocks.TUFF_LAPIS_ORE)
                .add(ModBlocks.TUFF_IRON_ORE)
                .add(ModBlocks.TUFF_COPPER_ORE);


        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.GRANITE_EMERALD_ORE)
                .add(ModBlocks.GRANITE_DIAMOND_ORE)
                .add(ModBlocks.GRANITE_REDSTONE_ORE)
                .add(ModBlocks.GRANITE_GOLD_ORE)


                .add(ModBlocks.ANDESITE_EMERALD_ORE)
                .add(ModBlocks.ANDESITE_DIAMOND_ORE)
                .add(ModBlocks.ANDESITE_REDSTONE_ORE)
                .add(ModBlocks.ANDESITE_GOLD_ORE)


                .add(ModBlocks.DIORITE_EMERALD_ORE)
                .add(ModBlocks.DIORITE_DIAMOND_ORE)
                .add(ModBlocks.DIORITE_REDSTONE_ORE)
                .add(ModBlocks.DIORITE_GOLD_ORE)


                .add(ModBlocks.CALCITE_EMERALD_ORE)
                .add(ModBlocks.CALCITE_DIAMOND_ORE)
                .add(ModBlocks.CALCITE_REDSTONE_ORE)
                .add(ModBlocks.CALCITE_GOLD_ORE)


                .add(ModBlocks.TUFF_EMERALD_ORE)
                .add(ModBlocks.TUFF_DIAMOND_ORE)
                .add(ModBlocks.TUFF_REDSTONE_ORE)
                .add(ModBlocks.TUFF_GOLD_ORE);
    }
}
