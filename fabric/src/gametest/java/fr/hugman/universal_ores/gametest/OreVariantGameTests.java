package fr.hugman.universal_ores.gametest;

import com.mojang.logging.LogUtils;
import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
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
    private static final List<String> OVERWORLD_ORES = List.of("coal", "iron", "gold", "copper", "lapis", "redstone", "emerald", "diamond");

    @GameTest
    public void vanillaVariantsKeepTheirIds(GameTestHelper helper) {
        List<String> expected = new ArrayList<>();
        OVERWORLD_STONES.forEach(stone -> OVERWORLD_ORES.forEach(ore -> expected.add(stone + "_" + ore + "_ore")));
        Stream.of("blackstone", "basalt").forEach(stone -> Stream.of("gold", "quartz").forEach(ore -> expected.add(stone + "_" + ore + "_ore")));
        List<String> missing = expected.stream().filter(path -> OreVariantRegistrar.get(UniversalOres.id(path)).isEmpty()).toList();
        LOGGER.info("{} ore variants are registered", OreVariantRegistrar.registered().size());
        helper.assertTrue(missing.isEmpty(), "Missing ore variants: " + missing);
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
            Set<Item> drops = drops(variant.block(), level, pos, pickaxe);
            Set<Item> oreDrops = drops(variant.ore(), level, pos, pickaxe);
            if (!drops.equals(oreDrops)) {
                problems.add(variant.variant().id() + " drops " + drops + " instead of " + oreDrops);
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
