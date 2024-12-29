package net.universal_ores.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.universal_ores.UniversalOres;
import net.universal_ores.block.ModBlocks;
import net.universal_ores.util.ModTags;

import java.util.List;

public class ModConfiguredFeatures {



    public static final RegistryKey<ConfiguredFeature<?, ?>> GRANITE_DIAMOND_ORE_KEY = registerKey("ore_granite_diamond");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GRANITE_GOLD_ORE_KEY    = registerKey("ore_granite_gold");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GRANITE_IRON_ORE_KEY    = registerKey("ore_granite_iron");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GRANITE_COAL_ORE_KEY    = registerKey("ore_granite_coal");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GRANITE_LAPIS_ORE_KEY   = registerKey("ore_granite_lapis");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GRANITE_REDSTONE_ORE_KEY= registerKey("ore_granite_redstone");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GRANITE_COPPER_ORE_KEY  = registerKey("ore_granite_copper");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GRANITE_EMERALD_ORE_KEY = registerKey("ore_granite_emerald");

    public static final RegistryKey<ConfiguredFeature<?, ?>> ANDESITE_DIAMOND_ORE_KEY = registerKey("ore_andesite_diamond");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ANDESITE_GOLD_ORE_KEY    = registerKey("ore_andesite_gold");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ANDESITE_IRON_ORE_KEY    = registerKey("ore_andesite_iron");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ANDESITE_COAL_ORE_KEY    = registerKey("ore_andesite_coal");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ANDESITE_LAPIS_ORE_KEY   = registerKey("ore_andesite_lapis");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ANDESITE_REDSTONE_ORE_KEY= registerKey("ore_andesite_redstone");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ANDESITE_COPPER_ORE_KEY  = registerKey("ore_andesite_copper");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ANDESITE_EMERALD_ORE_KEY = registerKey("ore_andesite_emerald");

    public static final RegistryKey<ConfiguredFeature<?, ?>> DIORITE_DIAMOND_ORE_KEY = registerKey("ore_diorite_diamond");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DIORITE_GOLD_ORE_KEY    = registerKey("ore_diorite_gold");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DIORITE_IRON_ORE_KEY    = registerKey("ore_diorite_iron");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DIORITE_COAL_ORE_KEY    = registerKey("ore_diorite_coal");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DIORITE_LAPIS_ORE_KEY   = registerKey("ore_diorite_lapis");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DIORITE_REDSTONE_ORE_KEY= registerKey("ore_diorite_redstone");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DIORITE_COPPER_ORE_KEY  = registerKey("ore_diorite_copper");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DIORITE_EMERALD_ORE_KEY = registerKey("ore_diorite_emerald");



    public static final RegistryKey<ConfiguredFeature<?, ?>> TUFF_DIAMOND_ORE_KEY = registerKey("ore_tuff_diamond");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TUFF_GOLD_ORE_KEY    = registerKey("ore_tuff_gold");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TUFF_IRON_ORE_KEY    = registerKey("ore_tuff_iron");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TUFF_COAL_ORE_KEY    = registerKey("ore_tuff_coal");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TUFF_LAPIS_ORE_KEY   = registerKey("ore_tuff_lapis");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TUFF_REDSTONE_ORE_KEY = registerKey("ore_tuff_redstone");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TUFF_COPPER_ORE_KEY  = registerKey("ore_tuff_copper");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TUFF_EMERALD_ORE_KEY = registerKey("ore_tuff_emerald");

    public static final RegistryKey<ConfiguredFeature<?, ?>> CALCITE_DIAMOND_ORE_KEY = registerKey("ore_calcite_diamond");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CALCITE_GOLD_ORE_KEY    = registerKey("ore_calcite_gold");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CALCITE_IRON_ORE_KEY    = registerKey("ore_calcite_iron");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CALCITE_COAL_ORE_KEY    = registerKey("ore_calcite_coal");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CALCITE_LAPIS_ORE_KEY   = registerKey("ore_calcite_lapis");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CALCITE_REDSTONE_ORE_KEY = registerKey("ore_calcite_redstone");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CALCITE_COPPER_ORE_KEY  = registerKey("ore_calcite_copper");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CALCITE_EMERALD_ORE_KEY = registerKey("ore_calcite_emerald");


    public static final RegistryKey<ConfiguredFeature<?, ?>> BLACKSTONE_QUARTZ_ORE_KEY = registerKey("ore_blackstone_quartz");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BLACKSTONE_GOLD_ORE_KEY   = registerKey("ore_blackstone_gold");


    public static final RegistryKey<ConfiguredFeature<?, ?>> BASALT_QUARTZ_ORE_KEY     = registerKey("ore_basalt_quartz");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BASALT_GOLD_ORE_KEY       = registerKey("ore_basalt_gold");




    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {


        RuleTest GraniteReplaceables = new TagMatchRuleTest(ModTags.GRANITE_ORE_REPLACEABLES);

        List<OreFeatureConfig.Target> DiamondGraniteOres =
                List.of(
                        OreFeatureConfig.createTarget(GraniteReplaceables, ModBlocks.GRANITE_DIAMOND_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> EmeraldGraniteOres =
                List.of(
                        OreFeatureConfig.createTarget(GraniteReplaceables, ModBlocks.GRANITE_EMERALD_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> GoldGraniteOres =
                List.of(
                        OreFeatureConfig.createTarget(GraniteReplaceables, ModBlocks.GRANITE_GOLD_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> IronGraniteOres =
                List.of(
                        OreFeatureConfig.createTarget(GraniteReplaceables, ModBlocks.GRANITE_IRON_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> CoalGraniteOres =
                List.of(
                        OreFeatureConfig.createTarget(GraniteReplaceables, ModBlocks.GRANITE_COAL_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> CopperGraniteOres =
                List.of(
                        OreFeatureConfig.createTarget(GraniteReplaceables, ModBlocks.GRANITE_COPPER_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> LapisGraniteOres =
                List.of(
                        OreFeatureConfig.createTarget(GraniteReplaceables, ModBlocks.GRANITE_LAPIS_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> RedstoneGraniteOres =
                List.of(
                        OreFeatureConfig.createTarget(GraniteReplaceables, ModBlocks.GRANITE_REDSTONE_ORE.getDefaultState())
                );

        register(context, GRANITE_DIAMOND_ORE_KEY, Feature.ORE, new OreFeatureConfig(DiamondGraniteOres, 5));
        register(context, GRANITE_GOLD_ORE_KEY, Feature.ORE, new OreFeatureConfig(GoldGraniteOres, 12));
        register(context, GRANITE_IRON_ORE_KEY, Feature.ORE, new OreFeatureConfig(IronGraniteOres, 12));
        register(context, GRANITE_COAL_ORE_KEY, Feature.ORE, new OreFeatureConfig(CoalGraniteOres, 20));
        register(context, GRANITE_LAPIS_ORE_KEY, Feature.ORE, new OreFeatureConfig(LapisGraniteOres, 20));
        register(context, GRANITE_REDSTONE_ORE_KEY, Feature.ORE, new OreFeatureConfig(RedstoneGraniteOres, 16));
        register(context, GRANITE_COPPER_ORE_KEY, Feature.ORE, new OreFeatureConfig(CopperGraniteOres, 17));
        register(context, GRANITE_EMERALD_ORE_KEY, Feature.ORE, new OreFeatureConfig(EmeraldGraniteOres, 12));

        RuleTest AndesiteReplaceables = new TagMatchRuleTest(ModTags.ANDESITE_ORE_REPLACEABLES);

        List<OreFeatureConfig.Target> DiamondAndesiteOres =
                List.of(
                        OreFeatureConfig.createTarget(AndesiteReplaceables, ModBlocks.ANDESITE_DIAMOND_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> EmeraldAndesiteOres =
                List.of(
                        OreFeatureConfig.createTarget(AndesiteReplaceables, ModBlocks.ANDESITE_EMERALD_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> GoldAndesiteOres =
                List.of(
                        OreFeatureConfig.createTarget(AndesiteReplaceables, ModBlocks.ANDESITE_GOLD_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> IronAndesiteOres =
                List.of(
                        OreFeatureConfig.createTarget(AndesiteReplaceables, ModBlocks.ANDESITE_IRON_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> CoalAndesiteOres =
                List.of(
                        OreFeatureConfig.createTarget(AndesiteReplaceables, ModBlocks.ANDESITE_COAL_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> CopperAndesiteOres =
                List.of(
                        OreFeatureConfig.createTarget(AndesiteReplaceables, ModBlocks.ANDESITE_COPPER_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> LapisAndesiteOres =
                List.of(
                        OreFeatureConfig.createTarget(AndesiteReplaceables, ModBlocks.ANDESITE_LAPIS_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> RedstoneAndesiteOres =
                List.of(
                        OreFeatureConfig.createTarget(AndesiteReplaceables, ModBlocks.ANDESITE_REDSTONE_ORE.getDefaultState())
                );

        register(context, ANDESITE_DIAMOND_ORE_KEY, Feature.ORE, new OreFeatureConfig(DiamondAndesiteOres, 5));
        register(context, ANDESITE_GOLD_ORE_KEY, Feature.ORE, new OreFeatureConfig(GoldAndesiteOres, 12));
        register(context, ANDESITE_IRON_ORE_KEY, Feature.ORE, new OreFeatureConfig(IronAndesiteOres, 18));
        register(context, ANDESITE_COAL_ORE_KEY, Feature.ORE, new OreFeatureConfig(CoalAndesiteOres, 25));
        register(context, ANDESITE_LAPIS_ORE_KEY, Feature.ORE, new OreFeatureConfig(LapisAndesiteOres, 20));
        register(context, ANDESITE_REDSTONE_ORE_KEY, Feature.ORE, new OreFeatureConfig(RedstoneAndesiteOres, 16));
        register(context, ANDESITE_COPPER_ORE_KEY, Feature.ORE, new OreFeatureConfig(CopperAndesiteOres, 26));
        register(context, ANDESITE_EMERALD_ORE_KEY, Feature.ORE, new OreFeatureConfig(EmeraldAndesiteOres, 12));

        RuleTest DioriteReplaceables = new TagMatchRuleTest(ModTags.DIORITE_ORE_REPLACEABLES);

        List<OreFeatureConfig.Target> DiamondDioriteOres =
                List.of(
                        OreFeatureConfig.createTarget(DioriteReplaceables, ModBlocks.DIORITE_DIAMOND_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> EmeraldDioriteOres =
                List.of(
                        OreFeatureConfig.createTarget(DioriteReplaceables, ModBlocks.DIORITE_EMERALD_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> GoldDioriteOres =
                List.of(
                        OreFeatureConfig.createTarget(DioriteReplaceables, ModBlocks.DIORITE_GOLD_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> IronDioriteOres =
                List.of(
                        OreFeatureConfig.createTarget(DioriteReplaceables, ModBlocks.DIORITE_IRON_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> CoalDioriteOres =
                List.of(
                        OreFeatureConfig.createTarget(DioriteReplaceables, ModBlocks.DIORITE_COAL_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> CopperDioriteOres =
                List.of(
                        OreFeatureConfig.createTarget(DioriteReplaceables, ModBlocks.DIORITE_COPPER_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> LapisDioriteOres =
                List.of(
                        OreFeatureConfig.createTarget(DioriteReplaceables, ModBlocks.DIORITE_LAPIS_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> RedstoneDioriteOres =
                List.of(
                        OreFeatureConfig.createTarget(DioriteReplaceables, ModBlocks.DIORITE_REDSTONE_ORE.getDefaultState())
                );

        register(context, DIORITE_DIAMOND_ORE_KEY, Feature.ORE, new OreFeatureConfig(DiamondDioriteOres, 5));
        register(context, DIORITE_GOLD_ORE_KEY, Feature.ORE, new OreFeatureConfig(GoldDioriteOres, 12));
        register(context, DIORITE_IRON_ORE_KEY, Feature.ORE, new OreFeatureConfig(IronDioriteOres, 20));
        register(context, DIORITE_COAL_ORE_KEY, Feature.ORE, new OreFeatureConfig(CoalDioriteOres, 25));
        register(context, DIORITE_LAPIS_ORE_KEY, Feature.ORE, new OreFeatureConfig(LapisDioriteOres, 20));
        register(context, DIORITE_REDSTONE_ORE_KEY, Feature.ORE, new OreFeatureConfig(RedstoneDioriteOres, 16));
        register(context, DIORITE_COPPER_ORE_KEY, Feature.ORE, new OreFeatureConfig(CopperDioriteOres, 26));
        register(context, DIORITE_EMERALD_ORE_KEY, Feature.ORE, new OreFeatureConfig(EmeraldDioriteOres, 12));

        RuleTest TuffReplaceables = new TagMatchRuleTest(ModTags.TUFF_ORE_REPLACEABLES);
        RuleTest CalciteReplaceables = new TagMatchRuleTest(ModTags.CALCITE_ORE_REPLACEABLES);

        List<OreFeatureConfig.Target> DiamondTuffOres =
                List.of(
                        OreFeatureConfig.createTarget(TuffReplaceables, ModBlocks.TUFF_DIAMOND_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> EmeraldTuffOres =
                List.of(
                        OreFeatureConfig.createTarget(TuffReplaceables, ModBlocks.TUFF_EMERALD_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> GoldTuffOres =
                List.of(
                        OreFeatureConfig.createTarget(TuffReplaceables, ModBlocks.TUFF_GOLD_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> IronTuffOres =
                List.of(
                        OreFeatureConfig.createTarget(TuffReplaceables, ModBlocks.TUFF_IRON_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> CoalTuffOres =
                List.of(
                        OreFeatureConfig.createTarget(TuffReplaceables, ModBlocks.TUFF_COAL_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> CopperTuffOres =
                List.of(
                        OreFeatureConfig.createTarget(TuffReplaceables, ModBlocks.TUFF_COPPER_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> LapisTuffOres =
                List.of(
                        OreFeatureConfig.createTarget(TuffReplaceables, ModBlocks.TUFF_LAPIS_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> RedstoneTuffOres =
                List.of(
                        OreFeatureConfig.createTarget(TuffReplaceables, ModBlocks.TUFF_REDSTONE_ORE.getDefaultState())
                );

        register(context, TUFF_DIAMOND_ORE_KEY, Feature.ORE, new OreFeatureConfig(DiamondTuffOres, 10));
        register(context, TUFF_GOLD_ORE_KEY, Feature.ORE, new OreFeatureConfig(GoldTuffOres, 7));
        register(context, TUFF_IRON_ORE_KEY, Feature.ORE, new OreFeatureConfig(IronTuffOres, 10));
        register(context, TUFF_COAL_ORE_KEY, Feature.ORE, new OreFeatureConfig(CoalTuffOres, 4));
        register(context, TUFF_LAPIS_ORE_KEY, Feature.ORE, new OreFeatureConfig(LapisTuffOres, 15));
        register(context, TUFF_REDSTONE_ORE_KEY, Feature.ORE, new OreFeatureConfig(RedstoneTuffOres, 12));
        register(context, TUFF_COPPER_ORE_KEY, Feature.ORE, new OreFeatureConfig(CopperTuffOres, 7));
        register(context, TUFF_EMERALD_ORE_KEY, Feature.ORE, new OreFeatureConfig(EmeraldTuffOres, 1));

        List<OreFeatureConfig.Target> DiamondCalciteOres =
                List.of(
                        OreFeatureConfig.createTarget(CalciteReplaceables, ModBlocks.CALCITE_DIAMOND_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> EmeraldCalciteOres =
                List.of(
                        OreFeatureConfig.createTarget(CalciteReplaceables, ModBlocks.CALCITE_EMERALD_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> GoldCalciteOres =
                List.of(
                        OreFeatureConfig.createTarget(CalciteReplaceables, ModBlocks.CALCITE_GOLD_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> IronCalciteOres =
                List.of(
                        OreFeatureConfig.createTarget(CalciteReplaceables, ModBlocks.CALCITE_IRON_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> CoalCalciteOres =
                List.of(
                        OreFeatureConfig.createTarget(CalciteReplaceables, ModBlocks.CALCITE_COAL_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> CopperCalciteOres =
                List.of(
                        OreFeatureConfig.createTarget(CalciteReplaceables, ModBlocks.CALCITE_COPPER_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> LapisCalciteOres =
                List.of(
                        OreFeatureConfig.createTarget(CalciteReplaceables, ModBlocks.CALCITE_LAPIS_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> RedstoneCalciteOres =
                List.of(
                        OreFeatureConfig.createTarget(CalciteReplaceables, ModBlocks.CALCITE_REDSTONE_ORE.getDefaultState())
                );

        register(context, CALCITE_DIAMOND_ORE_KEY, Feature.ORE, new OreFeatureConfig(DiamondCalciteOres, 10));
        register(context, CALCITE_GOLD_ORE_KEY, Feature.ORE, new OreFeatureConfig(GoldCalciteOres, 20));
        register(context, CALCITE_IRON_ORE_KEY, Feature.ORE, new OreFeatureConfig(IronCalciteOres, 15));
        register(context, CALCITE_COAL_ORE_KEY, Feature.ORE, new OreFeatureConfig(CoalCalciteOres, 4));
        register(context, CALCITE_LAPIS_ORE_KEY, Feature.ORE, new OreFeatureConfig(LapisCalciteOres, 30));
        register(context, CALCITE_REDSTONE_ORE_KEY, Feature.ORE, new OreFeatureConfig(RedstoneCalciteOres, 28));
        register(context, CALCITE_COPPER_ORE_KEY, Feature.ORE, new OreFeatureConfig(CopperCalciteOres, 25));
        register(context, CALCITE_EMERALD_ORE_KEY, Feature.ORE, new OreFeatureConfig(EmeraldCalciteOres, 4));




        RuleTest BlackstoneReplaceables = new TagMatchRuleTest(ModTags.BLACKSTONE_ORE_REPLACEABLES);


        List<OreFeatureConfig.Target> GoldBlackstoneOres =
                List.of(
                        OreFeatureConfig.createTarget(BlackstoneReplaceables, ModBlocks.BLACKSTONE_GOLD_ORE.getDefaultState())
                );




        List<OreFeatureConfig.Target> QuartzBlackstoneOres =
                List.of(
                        OreFeatureConfig.createTarget(BlackstoneReplaceables, ModBlocks.BLACKSTONE_QUARTZ_ORE.getDefaultState())
                );



        register(context, BLACKSTONE_GOLD_ORE_KEY, Feature.ORE, new OreFeatureConfig(GoldBlackstoneOres, 16));
        register(context, BLACKSTONE_QUARTZ_ORE_KEY, Feature.ORE, new OreFeatureConfig(QuartzBlackstoneOres, 23));




        RuleTest BasaltReplaceables = new TagMatchRuleTest(ModTags.BASALT_ORE_REPLACEABLES);

        List<OreFeatureConfig.Target> GoldBasaltOres =
                List.of(
                        OreFeatureConfig.createTarget(BasaltReplaceables, ModBlocks.BASALT_GOLD_ORE.getDefaultState())
                );

        List<OreFeatureConfig.Target> QuartzBasaltOres =
                List.of(
                        OreFeatureConfig.createTarget(BasaltReplaceables, ModBlocks.BASALT_QUARTZ_ORE.getDefaultState())
                );

        register(context, BASALT_GOLD_ORE_KEY, Feature.ORE, new OreFeatureConfig(GoldBasaltOres, 16));
        register(context, BASALT_QUARTZ_ORE_KEY, Feature.ORE, new OreFeatureConfig(QuartzBasaltOres, 23));


    }




        public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(UniversalOres.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
