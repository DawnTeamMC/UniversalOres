package fr.hugman.universal_ores.gametest;

import com.mojang.logging.LogUtils;
import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Checks that every registered ore variant behaves like its original ore: drops, tags, cooking recipes and world
 * generation. With {@code -PcompatMods}, this covers the variants of the mods Universal Ores is compatible with.
 */
public class OreVariantGameTests {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<String> OVERWORLD_STONES = List.of("andesite", "diorite", "granite", "tuff", "calcite");
    private static final int DROP_ROLLS = 100;
    private static final List<String> OVERWORLD_ORES = List.of("coal", "iron", "gold", "copper", "lapis", "redstone", "emerald", "diamond");
    private static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GRANITE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.withDefaultNamespace("ore_granite"));

    @GameTest
    public void vanillaVariantsKeepTheirIds(GameTestHelper helper) {
        List<String> expected = new ArrayList<>();
        OVERWORLD_STONES.forEach(stone -> OVERWORLD_ORES.forEach(ore -> expected.add(stone + "_" + ore + "_ore")));
        Stream.of("blackstone", "basalt").forEach(stone -> Stream.of("gold", "quartz").forEach(ore -> expected.add(stone + "_" + ore + "_ore")));
        OVERWORLD_ORES.forEach(ore -> expected.add("smooth_basalt_" + ore + "_ore"));
        // Blackstone gold ore is the variant of nether gold ore
        OVERWORLD_ORES.stream().filter(ore -> !ore.equals("gold")).forEach(ore -> expected.add("blackstone_" + ore + "_ore"));
        List<String> missing = expected.stream().filter(path -> OreVariantRegistrar.get(UniversalOres.id(path)).isEmpty()).toList();
        LOGGER.info("{} ore variants are registered", OreVariantRegistrar.registered().size());
        helper.assertTrue(missing.isEmpty(), "Missing ore variants: " + missing);
        helper.assertTrue(OreVariantRegistrar.get(UniversalOres.id("blackstone_gold_ore")).orElseThrow().ore() == Blocks.NETHER_GOLD_ORE, "Blackstone gold ore should be the variant of nether gold ore");
        helper.succeed();
    }

    @GameTest
    public void variantsDropLikeTheirOre(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        BlockPos pos = helper.absolutePos(BlockPos.ZERO);
        ItemStack pickaxe = new ItemStack(Items.NETHERITE_PICKAXE);
        ItemStack silkTouchPickaxe = pickaxe.copy();
        silkTouchPickaxe.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), 1);

        List<String> problems = new ArrayList<>();
        for (RegisteredOreVariant variant : OreVariantRegistrar.registered()) {
            Set<Item> silkTouchDrops = drops(variant.block(), level, pos, silkTouchPickaxe);
            if (!silkTouchDrops.equals(Set.of(variant.block().asItem()))) {
                problems.add(variant.variant().id() + " drops " + silkTouchDrops + " with Silk Touch");
            }
            // Some ores drop random items (like Tech Reborn's extra dusts): what the variant drops must be among what
            // the ore can drop
            Set<Item> drops = drops(variant.block(), level, pos, pickaxe);
            Set<Item> oreDrops = new HashSet<>();
            for (int roll = 0; roll < DROP_ROLLS; roll++) {
                oreDrops.addAll(drops(variant.ore(), level, pos, pickaxe));
            }
            if (!oreDrops.containsAll(drops) || drops.isEmpty() != oreDrops.isEmpty()) {
                problems.add(variant.variant().id() + " drops " + drops + " instead of some of " + oreDrops);
            }
            if (variant.block().defaultBlockState().requiresCorrectToolForDrops() != variant.ore().defaultBlockState().requiresCorrectToolForDrops()) {
                problems.add(variant.variant().id() + " doesn't need the same tool as its ore");
            }
        }
        helper.assertTrue(problems.isEmpty(), String.join("\n", problems));
        helper.succeed();
    }

    @GameTest
    public void variantsShareTheTagsOfTheirOre(GameTestHelper helper) {
        List<String> problems = new ArrayList<>();
        for (RegisteredOreVariant variant : OreVariantRegistrar.registered()) {
            Set<TagKey<Block>> blockTags = missingTags(variant.ore().builtInRegistryHolder(), variant.block().builtInRegistryHolder());
            if (!blockTags.isEmpty()) {
                problems.add(variant.variant().id() + " isn't in the block tags " + blockTags);
            }
            Set<TagKey<Item>> itemTags = missingTags(variant.ore().asItem().builtInRegistryHolder(), variant.block().asItem().builtInRegistryHolder());
            if (!itemTags.isEmpty()) {
                problems.add(variant.variant().id() + " isn't in the item tags " + itemTags);
            }
        }
        helper.assertTrue(problems.isEmpty(), String.join("\n", problems));
        helper.succeed();
    }

    @GameTest
    public void variantsCookLikeTheirOre(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        var recipes = level.getServer().getRecipeManager();
        List<String> problems = new ArrayList<>();
        for (RegisteredOreVariant variant : OreVariantRegistrar.registered()) {
            for (RecipeType<?> type : List.of(RecipeType.SMELTING, RecipeType.BLASTING)) {
                Optional<Item> oreResult = cook(recipes, type, variant.ore().asItem(), level);
                Optional<Item> variantResult = cook(recipes, type, variant.block().asItem(), level);
                if (!oreResult.equals(variantResult)) {
                    problems.add(variant.variant().id() + " " + type + " gives " + variantResult + " instead of " + oreResult);
                }
            }
        }
        helper.assertTrue(problems.isEmpty(), String.join("\n", problems));
        helper.succeed();
    }

    @GameTest
    public void variantsGenerateWhereTheirOreDoes(GameTestHelper helper) {
        Set<Block> placed = new HashSet<>();
        int features = 0;
        for (ConfiguredFeature<?, ?> feature : helper.getLevel().registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE)) {
            if (feature.config() instanceof OreConfiguration ore) {
                features++;
                ore.targetStates.forEach(target -> placed.add(target.state.getBlock()));
            }
        }
        List<String> problems = OreVariantRegistrar.registered().stream()
                .filter(variant -> placed.contains(variant.ore()) && !placed.contains(variant.block()))
                .map(variant -> variant.variant().id() + " is never generated, while its ore is")
                .toList();
        LOGGER.info("{} ore features place {} blocks", features, placed.size());
        helper.assertTrue(problems.isEmpty(), String.join("\n", problems));
        helper.succeed();
    }

    /**
     * Mods add their stones after the ores, so a stone blob turns the ores it covers into their variants in that stone.
     */
    @GameTest
    public void stonesTurnTheOresTheyCoverIntoVariants(GameTestHelper helper) {
        Registry<ConfiguredFeature<?, ?>> features = helper.getLevel().registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE);
        Map<Block, List<RegisteredOreVariant>> variantsByStone = OreVariantRegistrar.registered().stream().collect(Collectors.groupingBy(RegisteredOreVariant::stone));
        RandomSource random = RandomSource.create(0);
        List<String> problems = new ArrayList<>();
        List<String> stoneFeatures = new ArrayList<>();
        for (var entry : features.entrySet()) {
            if (!(entry.getValue().config() instanceof OreConfiguration ore)) {
                continue;
            }
            Set<Block> stones = ore.targetStates.stream().map(target -> target.state.getBlock()).filter(variantsByStone::containsKey).collect(Collectors.toSet());
            if (stones.isEmpty()) {
                continue;
            }
            stoneFeatures.add(entry.getKey().identifier().toString());
            for (Block stone : stones) {
                for (RegisteredOreVariant variant : variantsByStone.get(stone)) {
                    Block placed = placedOver(ore.targetStates, variant.ore().defaultBlockState(), random).getBlock();
                    // A feature placing several stones turns an ore into its variant in the first one
                    if (OreVariantRegistrar.registered().stream().noneMatch(other -> other.block() == placed && other.ore() == variant.ore() && stones.contains(other.stone()))) {
                        problems.add(entry.getKey().identifier() + " turns " + BuiltInRegistries.BLOCK.getKey(variant.ore()) + " into " + BuiltInRegistries.BLOCK.getKey(placed) + " instead of " + variant.variant().id());
                    }
                }
            }
        }
        LOGGER.info("{} ore features place stones with variants: {}", stoneFeatures.size(), stoneFeatures);
        helper.assertTrue(stoneFeatures.contains("minecraft:ore_granite"), "minecraft:ore_granite should place a stone with variants");
        helper.assertTrue(problems.isEmpty(), String.join("\n", problems));
        helper.succeed();
    }

    /**
     * Runs granite blobs over ores, with the targets of the game's granite blobs but smaller, so that they fit in the test.
     */
    @GameTest
    public void graniteBlobsTurnTheOresTheyCoverIntoVariants(GameTestHelper helper) {
        OreConfiguration granite = (OreConfiguration) helper.getLevel().registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).getValueOrThrow(ORE_GRANITE).config();
        // Built again, like any ore configuration: the variants are already in the targets, so nothing is added twice
        OreConfiguration blob = new OreConfiguration(granite.targetStates, 8);
        helper.assertTrue(blob.targetStates.equals(granite.targetStates), "Building the granite blob again changed its targets");

        Map<BlockPos, BlockState> before = new HashMap<>();
        List<BlockState> pattern = List.of(
                Blocks.COAL_ORE.defaultBlockState(),
                Blocks.STONE.defaultBlockState(),
                Blocks.DEEPSLATE_COAL_ORE.defaultBlockState(),
                Blocks.REDSTONE_ORE.defaultBlockState().setValue(BlockStateProperties.LIT, true)
        );
        // A blob of size 8 places blocks at most 2 blocks away horizontally and 3 vertically
        BlockPos center = new BlockPos(3, 4, 3);
        BlockPos.betweenClosed(center.offset(-2, -3, -2), center.offset(2, 3, 2)).forEach(pos -> {
            BlockState state = pattern.get(Math.floorMod(pos.getX() + pos.getY() * 3 + pos.getZ() * 7, pattern.size()));
            helper.setBlock(pos, state);
            before.put(pos.immutable(), state);
        });
        // Small blobs are thin: a few of them cover more ores
        for (int seed = 0; seed < 10; seed++) {
            Feature.ORE.place(blob, helper.getLevel(), helper.getLevel().getChunkSource().getGenerator(), RandomSource.create(seed), helper.absolutePos(center));
        }

        Block graniteCoalOre = BuiltInRegistries.BLOCK.getValue(UniversalOres.id("granite_coal_ore"));
        Block graniteRedstoneOre = BuiltInRegistries.BLOCK.getValue(UniversalOres.id("granite_redstone_ore"));
        Map<Block, Set<Block>> allowed = Map.of(
                Blocks.STONE, Set.of(Blocks.STONE, Blocks.GRANITE),
                Blocks.COAL_ORE, Set.of(Blocks.COAL_ORE, graniteCoalOre),
                Blocks.REDSTONE_ORE, Set.of(Blocks.REDSTONE_ORE, graniteRedstoneOre),
                Blocks.DEEPSLATE_COAL_ORE, Set.of(Blocks.DEEPSLATE_COAL_ORE)
        );
        List<String> problems = new ArrayList<>();
        Map<Block, Integer> placed = new HashMap<>();
        before.forEach((pos, state) -> {
            Block after = helper.getBlockState(pos).getBlock();
            placed.merge(after, 1, Integer::sum);
            if (!allowed.get(state.getBlock()).contains(after)) {
                problems.add(BuiltInRegistries.BLOCK.getKey(state.getBlock()) + " became " + BuiltInRegistries.BLOCK.getKey(after) + " at " + pos);
            }
        });
        LOGGER.info("The granite blobs left {}", placed);
        helper.assertTrue(problems.isEmpty(), String.join("\n", problems));
        helper.assertTrue(placed.containsKey(Blocks.GRANITE), "The granite blobs placed no granite");
        helper.assertTrue(placed.containsKey(graniteCoalOre) && placed.containsKey(graniteRedstoneOre), "The granite blobs should turn both coal and redstone ores into variants");
        helper.succeed();
    }

    /**
     * The state an ore feature with these targets places over this one, like {@link net.minecraft.world.level.levelgen.feature.OreFeature} does.
     */
    private static BlockState placedOver(List<OreConfiguration.TargetBlockState> targets, BlockState state, RandomSource random) {
        for (var target : targets) {
            if (target.target.test(state, random)) {
                return target.state;
            }
        }
        return state;
    }

    private static Set<Item> drops(Block block, ServerLevel level, BlockPos pos, ItemStack tool) {
        return Block.getDrops(block.defaultBlockState(), level, pos, null, null, tool).stream().map(ItemStack::getItem).collect(Collectors.toSet());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Optional<Item> cook(net.minecraft.world.item.crafting.RecipeManager recipes, RecipeType<?> type, Item item, ServerLevel level) {
        SingleRecipeInput input = new SingleRecipeInput(new ItemStack(item));
        return recipes.getRecipeFor((RecipeType) type, input, level).map(holder -> ((net.minecraft.world.item.crafting.RecipeHolder<?>) holder).value()).map(recipe -> ((net.minecraft.world.item.crafting.Recipe) recipe).assemble(input).getItem());
    }

    /**
     * The tags of the ore that the variant lacks, ignoring those that say which stone an ore is in.
     */
    private static <T> Set<TagKey<T>> missingTags(Holder<T> ore, Holder<T> variant) {
        Set<TagKey<T>> variantTags = variant.tags().collect(Collectors.toSet());
        return ore.tags()
                .filter(tag -> !tag.location().toString().startsWith("c:ores_in_ground/"))
                .filter(tag -> !variantTags.contains(tag))
                .collect(Collectors.toSet());
    }
}
