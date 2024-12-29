package net.universal_ores.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.*;
import net.universal_ores.UniversalOres;

import java.util.List;

public class ModPlacedFeatures {




    public static final RegistryKey<PlacedFeature> GRANITE_DIAMOND_ORE_KEY  = registerKey("ore_granite_diamond");
    public static final RegistryKey<PlacedFeature> GRANITE_GOLD_ORE_KEY     = registerKey("ore_granite_gold");
    public static final RegistryKey<PlacedFeature> GRANITE_IRON_ORE_KEY     = registerKey("ore_granite_iron");
    public static final RegistryKey<PlacedFeature> GRANITE_COAL_ORE_KEY     = registerKey("ore_granite_coal");
    public static final RegistryKey<PlacedFeature> GRANITE_LAPIS_ORE_KEY    = registerKey("ore_granite_lapis");
    public static final RegistryKey<PlacedFeature> GRANITE_REDSTONE_ORE_KEY = registerKey("ore_granite_redstone");
    public static final RegistryKey<PlacedFeature> GRANITE_COPPER_ORE_KEY   = registerKey("ore_granite_copper");
    public static final RegistryKey<PlacedFeature> GRANITE_EMERALD_ORE_KEY  = registerKey("ore_granite_emerald");


    public static final RegistryKey<PlacedFeature> ANDESITE_DIAMOND_ORE_KEY  = registerKey("ore_andesite_diamond");
    public static final RegistryKey<PlacedFeature> ANDESITE_GOLD_ORE_KEY     = registerKey("ore_andesite_gold");
    public static final RegistryKey<PlacedFeature> ANDESITE_IRON_ORE_KEY     = registerKey("ore_andesite_iron");
    public static final RegistryKey<PlacedFeature> ANDESITE_COAL_ORE_KEY     = registerKey("ore_andesite_coal");
    public static final RegistryKey<PlacedFeature> ANDESITE_LAPIS_ORE_KEY    = registerKey("ore_andesite_lapis");
    public static final RegistryKey<PlacedFeature> ANDESITE_REDSTONE_ORE_KEY = registerKey("ore_andesite_redstone");
    public static final RegistryKey<PlacedFeature> ANDESITE_COPPER_ORE_KEY   = registerKey("ore_andesite_copper");
    public static final RegistryKey<PlacedFeature> ANDESITE_EMERALD_ORE_KEY  = registerKey("ore_andesite_emerald");

    public static final RegistryKey<PlacedFeature> DIORITE_DIAMOND_ORE_KEY  = registerKey("ore_diorite_diamond");
    public static final RegistryKey<PlacedFeature> DIORITE_GOLD_ORE_KEY     = registerKey("ore_diorite_gold");
    public static final RegistryKey<PlacedFeature> DIORITE_IRON_ORE_KEY     = registerKey("ore_diorite_iron");
    public static final RegistryKey<PlacedFeature> DIORITE_COAL_ORE_KEY     = registerKey("ore_diorite_coal");
    public static final RegistryKey<PlacedFeature> DIORITE_LAPIS_ORE_KEY    = registerKey("ore_diorite_lapis");
    public static final RegistryKey<PlacedFeature> DIORITE_REDSTONE_ORE_KEY = registerKey("ore_diorite_redstone");
    public static final RegistryKey<PlacedFeature> DIORITE_COPPER_ORE_KEY   = registerKey("ore_diorite_copper");
    public static final RegistryKey<PlacedFeature> DIORITE_EMERALD_ORE_KEY  = registerKey("ore_diorite_emerald");



    public static final RegistryKey<PlacedFeature> TUFF_DIAMOND_ORE_KEY  = registerKey("ore_tuff_diamond");
    public static final RegistryKey<PlacedFeature> TUFF_GOLD_ORE_KEY     = registerKey("ore_tuff_gold");
    public static final RegistryKey<PlacedFeature> TUFF_IRON_ORE_KEY     = registerKey("ore_tuff_iron");
    public static final RegistryKey<PlacedFeature> TUFF_COAL_ORE_KEY     = registerKey("ore_tuff_coal");
    public static final RegistryKey<PlacedFeature> TUFF_LAPIS_ORE_KEY    = registerKey("ore_tuff_lapis");
    public static final RegistryKey<PlacedFeature> TUFF_REDSTONE_ORE_KEY = registerKey("ore_tuff_redstone");
    public static final RegistryKey<PlacedFeature> TUFF_COPPER_ORE_KEY   = registerKey("ore_tuff_copper");
    public static final RegistryKey<PlacedFeature> TUFF_EMERALD_ORE_KEY  = registerKey("ore_tuff_emerald");

    public static final RegistryKey<PlacedFeature> CALCITE_DIAMOND_ORE_KEY  = registerKey("ore_calcite_diamond");
    public static final RegistryKey<PlacedFeature> CALCITE_GOLD_ORE_KEY     = registerKey("ore_calcite_gold");
    public static final RegistryKey<PlacedFeature> CALCITE_IRON_ORE_KEY     = registerKey("ore_calcite_iron");
    public static final RegistryKey<PlacedFeature> CALCITE_COAL_ORE_KEY     = registerKey("ore_calcite_coal");
    public static final RegistryKey<PlacedFeature> CALCITE_LAPIS_ORE_KEY    = registerKey("ore_calcite_lapis");
    public static final RegistryKey<PlacedFeature> CALCITE_REDSTONE_ORE_KEY = registerKey("ore_calcite_redstone");
    public static final RegistryKey<PlacedFeature> CALCITE_COPPER_ORE_KEY   = registerKey("ore_calcite_copper");
    public static final RegistryKey<PlacedFeature> CALCITE_EMERALD_ORE_KEY  = registerKey("ore_calcite_emerald");




    public static final RegistryKey<PlacedFeature> BLACKSTONE_QUARTZ_ORE_KEY = registerKey("ore_blackstone_quartz");
    public static final RegistryKey<PlacedFeature> BLACKSTONE_GOLD_ORE_KEY   = registerKey("ore_blackstone_gold");
    public static final RegistryKey<PlacedFeature> BASALT_QUARTZ_ORE_KEY     = registerKey("ore_basalt_quartz");
    public static final RegistryKey<PlacedFeature> BASALT_GOLD_ORE_KEY       = registerKey("ore_basalt_gold");



    private static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacementModifier.of(count), heightModifier);
    }




    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);



        register(context, GRANITE_DIAMOND_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GRANITE_DIAMOND_ORE_KEY),
                modifiersWithCount(7, // 7 blobs per chunk
                        HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-63), YOffset.aboveBottom(14)))); // من Y=-63 إلى Y=14

        register(context, GRANITE_GOLD_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GRANITE_GOLD_ORE_KEY),
                modifiersWithCount(4, // 4 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(32)))); // من Y=-64 إلى Y=32

        register(context, GRANITE_IRON_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GRANITE_IRON_ORE_KEY),
                modifiersWithCount(25, // 25 blobs per chunk (عدد معقول)
                        HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(64)))); // من Y=0 إلى Y=64

        register(context, GRANITE_COAL_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GRANITE_COAL_ORE_KEY),
                modifiersWithCount(25, // 25 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(25), YOffset.fixed(59)))); // من Y=25 إلى Y=59

        register(context, GRANITE_LAPIS_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GRANITE_LAPIS_ORE_KEY),
                modifiersWithCount(8, // 8 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(10), YOffset.fixed(21)))); // من Y=10 إلى Y=21

        register(context, GRANITE_REDSTONE_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GRANITE_REDSTONE_ORE_KEY),
                modifiersWithCount(7, // 7 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(10), YOffset.fixed(40)))); // من Y=10 إلى Y=40

        register(context, GRANITE_COPPER_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GRANITE_COPPER_ORE_KEY),
                modifiersWithCount(7, // 7 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(9), YOffset.fixed(55)))); // من Y=9 إلى Y=55

        register(context, GRANITE_EMERALD_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GRANITE_EMERALD_ORE_KEY),
                modifiersWithCount(9, // 9 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-16), YOffset.fixed(320)))); // من Y=-16 إلى Y=320

        register(context, ANDESITE_DIAMOND_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ANDESITE_DIAMOND_ORE_KEY),
                modifiersWithCount(7, // 7 blobs per chunk
                        HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-63), YOffset.aboveBottom(14)))); // من Y=-63 إلى Y=14

        register(context, ANDESITE_GOLD_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ANDESITE_GOLD_ORE_KEY),
                modifiersWithCount(4, // 4 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(32)))); // من Y=-64 إلى Y=32

        register(context, ANDESITE_IRON_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ANDESITE_IRON_ORE_KEY),
                modifiersWithCount(25, // 25 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(64)))); // من Y=0 إلى Y=64

        register(context, ANDESITE_COAL_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ANDESITE_COAL_ORE_KEY),
                modifiersWithCount(25, // 25 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(64)))); // من Y=0 إلى Y=64

        register(context, ANDESITE_LAPIS_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ANDESITE_LAPIS_ORE_KEY),
                modifiersWithCount(8, // 8 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(10), YOffset.fixed(21)))); // من Y=10 إلى Y=21

        register(context, ANDESITE_REDSTONE_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ANDESITE_REDSTONE_ORE_KEY),
                modifiersWithCount(7, // 7 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(10), YOffset.fixed(40)))); // من Y=10 إلى Y=40

        register(context, ANDESITE_COPPER_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ANDESITE_COPPER_ORE_KEY),
                modifiersWithCount(7, // 7 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(9), YOffset.fixed(55)))); // من Y=9 إلى Y=55

        register(context, ANDESITE_EMERALD_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ANDESITE_EMERALD_ORE_KEY),
                modifiersWithCount(9, // 9 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-16), YOffset.fixed(320)))); // من Y=-16 إلى Y=320

        register(context, DIORITE_DIAMOND_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DIORITE_DIAMOND_ORE_KEY),
                modifiersWithCount(7, // 7 blobs per chunk
                        HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-63), YOffset.aboveBottom(14)))); // من Y=-63 إلى Y=14

        register(context, DIORITE_GOLD_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DIORITE_GOLD_ORE_KEY),
                modifiersWithCount(4, // 4 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(32)))); // من Y=-64 إلى Y=32

        register(context, DIORITE_IRON_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DIORITE_IRON_ORE_KEY),
                modifiersWithCount(25, // 25 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(64)))); // من Y=0 إلى Y=64

        register(context, DIORITE_COAL_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DIORITE_COAL_ORE_KEY),
                modifiersWithCount(25, // 25 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(25), YOffset.fixed(59)))); // من Y=25 إلى Y=59

        register(context, DIORITE_LAPIS_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DIORITE_LAPIS_ORE_KEY),
                modifiersWithCount(8, // 8 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(10), YOffset.fixed(21)))); // من Y=10 إلى Y=21

        register(context, DIORITE_REDSTONE_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DIORITE_REDSTONE_ORE_KEY),
                modifiersWithCount(7, // 7 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(10), YOffset.fixed(40)))); // من Y=10 إلى Y=40

        register(context, DIORITE_COPPER_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DIORITE_COPPER_ORE_KEY),
                modifiersWithCount(7, // 7 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(9), YOffset.fixed(55)))); // من Y=9 إلى Y=55

        register(context, DIORITE_EMERALD_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DIORITE_EMERALD_ORE_KEY),
                modifiersWithCount(9, // 9 blobs per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-16), YOffset.fixed(320)))); // من Y=-16 إلى Y=320


        register(context, TUFF_DIAMOND_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TUFF_DIAMOND_ORE_KEY),
                modifiersWithCount(12,
                        HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-80), YOffset.aboveBottom(-10))));

        register(context, TUFF_GOLD_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TUFF_GOLD_ORE_KEY),
                modifiersWithCount(4,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-16), YOffset.fixed(16))));

        register(context, TUFF_IRON_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TUFF_IRON_ORE_KEY),
                modifiersWithCount(90,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(10), YOffset.fixed(26))));

        register(context, TUFF_COAL_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TUFF_COAL_ORE_KEY),
                modifiersWithCount(3,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-8), YOffset.fixed(0))));

        register(context, TUFF_LAPIS_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TUFF_LAPIS_ORE_KEY),
                modifiersWithCount(8,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-56), YOffset.fixed(16))));

        register(context, TUFF_REDSTONE_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TUFF_REDSTONE_ORE_KEY),
                modifiersWithCount(7,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-56), YOffset.fixed(16))));

        register(context, TUFF_COPPER_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TUFF_COPPER_ORE_KEY),
                modifiersWithCount(7,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-30), YOffset.fixed(16))));

        register(context, TUFF_EMERALD_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TUFF_EMERALD_ORE_KEY),
                modifiersWithCount(9,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(15), YOffset.fixed(320))));






        register(context, CALCITE_DIAMOND_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CALCITE_DIAMOND_ORE_KEY),
                modifiersWithCount(9,
                        HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-80), YOffset.aboveBottom(-10))));

        register(context, CALCITE_GOLD_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CALCITE_GOLD_ORE_KEY),
                modifiersWithCount(4,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(32))));

        register(context, CALCITE_IRON_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CALCITE_IRON_ORE_KEY),
                modifiersWithCount(10,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(64))));

        register(context, CALCITE_COAL_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CALCITE_COAL_ORE_KEY),
                modifiersWithCount(13,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(128))));

        register(context, CALCITE_LAPIS_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CALCITE_LAPIS_ORE_KEY),
                modifiersWithCount(8,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(32))));

        register(context, CALCITE_REDSTONE_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CALCITE_REDSTONE_ORE_KEY),
                modifiersWithCount(7,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(16))));

        register(context, CALCITE_COPPER_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CALCITE_COPPER_ORE_KEY),
                modifiersWithCount(7,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(48))));

        register(context, CALCITE_EMERALD_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CALCITE_EMERALD_ORE_KEY),
                modifiersWithCount(1,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(4), YOffset.fixed(32))));







        register(context, BLACKSTONE_GOLD_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BLACKSTONE_GOLD_ORE_KEY),
                modifiersWithCount(17,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-128), YOffset.fixed(128))));

        register(context, BLACKSTONE_QUARTZ_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BLACKSTONE_QUARTZ_ORE_KEY),
                modifiersWithCount(17,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-128), YOffset.fixed(128))));


        register(context, BASALT_GOLD_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BASALT_GOLD_ORE_KEY),
                modifiersWithCount(17,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-128), YOffset.fixed(128))));

        register(context, BASALT_QUARTZ_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BASALT_QUARTZ_ORE_KEY),
                modifiersWithCount(17,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-128), YOffset.fixed(128))));






    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(UniversalOres.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key,
                                                                                   RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                                                                   PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }
}