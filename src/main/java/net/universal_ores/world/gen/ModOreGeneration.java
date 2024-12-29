package net.universal_ores.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.universal_ores.world.ModPlacedFeatures;

public class ModOreGeneration {
    public static void register() {


        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.GRANITE_DIAMOND_ORE_KEY);


        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.GRANITE_GOLD_ORE_KEY);


        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.GRANITE_IRON_ORE_KEY);


        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.GRANITE_REDSTONE_ORE_KEY);


        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.GRANITE_LAPIS_ORE_KEY);


        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.GRANITE_COPPER_ORE_KEY);


        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.GRANITE_COAL_ORE_KEY);


        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.SNOWY_SLOPES, BiomeKeys.GROVE, BiomeKeys.JAGGED_PEAKS, BiomeKeys.FROZEN_RIVER, BiomeKeys.ICE_SPIKES), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.GRANITE_EMERALD_ORE_KEY);



        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.DIORITE_DIAMOND_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.DIORITE_GOLD_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.DIORITE_IRON_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.DIORITE_REDSTONE_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.DIORITE_LAPIS_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.DIORITE_COPPER_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.DIORITE_COAL_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.SNOWY_SLOPES, BiomeKeys.GROVE, BiomeKeys.JAGGED_PEAKS, BiomeKeys.FROZEN_RIVER, BiomeKeys.ICE_SPIKES), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.DIORITE_EMERALD_ORE_KEY);



        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.ANDESITE_DIAMOND_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.ANDESITE_GOLD_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.ANDESITE_IRON_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.ANDESITE_REDSTONE_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.ANDESITE_LAPIS_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.ANDESITE_COPPER_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.ANDESITE_COAL_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.SNOWY_SLOPES, BiomeKeys.GROVE, BiomeKeys.JAGGED_PEAKS, BiomeKeys.FROZEN_RIVER, BiomeKeys.ICE_SPIKES), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.ANDESITE_EMERALD_ORE_KEY);





        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.TUFF_DIAMOND_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.TUFF_GOLD_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.TUFF_IRON_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.TUFF_REDSTONE_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.TUFF_LAPIS_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.TUFF_COPPER_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.TUFF_COAL_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.SNOWY_SLOPES, BiomeKeys.GROVE, BiomeKeys.JAGGED_PEAKS, BiomeKeys.FROZEN_RIVER, BiomeKeys.ICE_SPIKES), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.TUFF_EMERALD_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.CALCITE_DIAMOND_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.CALCITE_GOLD_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.CALCITE_IRON_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.CALCITE_REDSTONE_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.CALCITE_LAPIS_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.CALCITE_COPPER_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.CALCITE_COAL_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.SNOWY_SLOPES, BiomeKeys.GROVE, BiomeKeys.JAGGED_PEAKS, BiomeKeys.FROZEN_RIVER, BiomeKeys.ICE_SPIKES), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.CALCITE_EMERALD_ORE_KEY);




        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.BASALT_GOLD_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.BASALT_QUARTZ_ORE_KEY);



        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.BLACKSTONE_GOLD_ORE_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.BLACKSTONE_QUARTZ_ORE_KEY);
    }
}
