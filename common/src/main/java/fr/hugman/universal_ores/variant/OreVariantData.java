package fr.hugman.universal_ores.variant;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.mixin.TagEntryAccessor;
import fr.hugman.universal_ores.pack.GeneratedPacks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.storage.loot.LootTable;
import org.slf4j.Logger;

import java.util.*;

/**
 * The data of the ore variants, all derived from their original ores so that data packs changing an ore change its
 * variants too:
 * <ul>
 *     <li>loot tables, written in the generated data pack: the original ore's loot table, with the original ore
 *     item (dropped with Silk Touch) swapped for the variant;</li>
 *     <li>tags: a variant is added to every block and item tag that lists its original ore;</li>
 *     <li>cooking recipes: each smelting, blasting... recipe of an original ore gets a copy for its variants.</li>
 * </ul>
 */
public final class OreVariantData {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String BLOCK_TAGS = Registries.tagsDirPath(Registries.BLOCK);
    private static final String ITEM_TAGS = Registries.tagsDirPath(Registries.ITEM);
    /**
     * Tags saying which stone an ore is found in: a variant is in another stone.
     */
    private static final List<String> EXCLUDED_TAG_PREFIXES = List.of("c:ores_in_ground/");

    private OreVariantData() {
    }

    public static void init() {
        GeneratedPacks.addGenerator(PackType.SERVER_DATA, output -> {
            int count = 0;
            for (RegisteredOreVariant variant : OreVariantRegistrar.registered()) {
                Optional<ResourceKey<LootTable>> lootTable = variant.block().getLootTable();
                if (lootTable.isEmpty()) {
                    continue;
                }
                Identifier id = lootTable.get().identifier();
                output.accept(id.withPath(path -> Registries.elementsDirPath(Registries.LOOT_TABLE) + "/" + path + ".json"), lootTable(variant));
                count++;
            }
            LOGGER.info("Generated the loot tables of {} ore variants", count);
        });
    }

    /**
     * The original ore's loot table, with the original ore item (dropped with Silk Touch) swapped for the variant.
     */
    public static JsonElement lootTable(RegisteredOreVariant variant) {
        Identifier id = variant.block().getLootTable().orElseThrow().identifier();
        JsonObject swap = new JsonObject();
        swap.addProperty("function", "minecraft:set_item");
        swap.addProperty("item", variant.variant().id().toString());

        JsonObject filter = new JsonObject();
        filter.addProperty("items", BuiltInRegistries.ITEM.getKey(variant.ore().asItem()).toString());

        JsonObject filtered = new JsonObject();
        filtered.addProperty("function", "minecraft:filtered");
        filtered.add("item_filter", filter);
        filtered.add("on_pass", swap);

        JsonArray functions = new JsonArray();
        functions.add(filtered);

        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:loot_table");
        entry.addProperty("value", variant.ore().getLootTable().map(key -> key.identifier().toString()).orElse("minecraft:empty"));
        entry.add("functions", functions);

        JsonArray entries = new JsonArray();
        entries.add(entry);
        JsonObject pool = new JsonObject();
        pool.addProperty("rolls", 1.0F);
        pool.add("entries", entries);
        JsonArray pools = new JsonArray();
        pools.add(pool);

        JsonObject table = new JsonObject();
        table.addProperty("type", "minecraft:block");
        table.add("pools", pools);
        table.addProperty("random_sequence", id.toString());
        return table;
    }

    /**
     * The block and item tags, with the variants added to those listing their original ore.
     *
     * @return the same tags if there is nothing to add, otherwise a copy
     */
    public static Map<Identifier, List<TagLoader.EntryWithSource>> withOreVariants(String directory, Map<Identifier, List<TagLoader.EntryWithSource>> tags) {
        if (!directory.equals(BLOCK_TAGS) && !directory.equals(ITEM_TAGS)) {
            return tags;
        }
        Map<Identifier, List<Identifier>> variantsByOre = new HashMap<>();
        for (RegisteredOreVariant variant : OreVariantRegistrar.registered()) {
            variantsByOre.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(variant.ore()), id -> new ArrayList<>()).add(variant.variant().id());
        }
        if (variantsByOre.isEmpty()) {
            return tags;
        }
        Map<Identifier, List<TagLoader.EntryWithSource>> result = new LinkedHashMap<>(tags);
        int count = 0;
        for (Map.Entry<Identifier, List<TagLoader.EntryWithSource>> tag : tags.entrySet()) {
            if (EXCLUDED_TAG_PREFIXES.stream().anyMatch(tag.getKey().toString()::startsWith)) {
                continue;
            }
            Set<Identifier> added = new LinkedHashSet<>();
            for (TagLoader.EntryWithSource entry : tag.getValue()) {
                TagEntryAccessor accessor = (TagEntryAccessor) entry.entry();
                if (!accessor.isTag()) {
                    added.addAll(variantsByOre.getOrDefault(accessor.getId(), List.of()));
                }
            }
            if (!added.isEmpty()) {
                List<TagLoader.EntryWithSource> entries = new ArrayList<>(tag.getValue());
                added.forEach(variant -> entries.add(new TagLoader.EntryWithSource(TagEntry.optionalElement(variant), UniversalOres.MOD_ID)));
                result.put(tag.getKey(), entries);
                count++;
            }
        }
        LOGGER.info("Added ore variants to {} {}", count, directory);
        return result;
    }

    /**
     * Copies the cooking recipes of the original ores for their variants, unless they already accept them.
     * A copy is named {@code universal_ores:<namespace>/<path>} after its original. If that copy already exists
     * (like the generated ones for vanilla ores), the variants it lacks are added to it.
     *
     * @return the recipes, with the copies
     */
    public static Iterable<RecipeHolder<?>> withCookingRecipes(Iterable<RecipeHolder<?>> recipes, HolderLookup.Provider registries) {
        return withCookingRecipes(recipes, registries, OreVariantRegistrar.registered());
    }

    /**
     * @param oreVariants the variants to copy recipes for
     */
    public static List<RecipeHolder<?>> withCookingRecipes(Iterable<RecipeHolder<?>> recipes, HolderLookup.Provider registries, Collection<RegisteredOreVariant> oreVariants) {
        Map<Identifier, RecipeHolder<?>> result = new LinkedHashMap<>();
        recipes.forEach(holder -> result.put(holder.id().identifier(), holder));
        Map<Item, List<Item>> variantsByOre = new LinkedHashMap<>();
        for (RegisteredOreVariant variant : oreVariants) {
            variantsByOre.computeIfAbsent(variant.ore().asItem(), item -> new ArrayList<>()).add(variant.block().asItem());
        }
        DynamicOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);
        int copied = 0;
        for (RecipeHolder<?> holder : List.copyOf(result.values())) {
            Identifier id = holder.id().identifier();
            if (id.getNamespace().equals(UniversalOres.MOD_ID) || !(holder.value() instanceof AbstractCookingRecipe cooking)) {
                continue;
            }
            Identifier copyId = UniversalOres.id(id.getNamespace() + "/" + id.getPath());
            Ingredient existing = result.get(copyId) != null && result.get(copyId).value() instanceof AbstractCookingRecipe copy ? copy.input() : null;
            // Item stacks can't be made yet: item components are bound after recipes are read
            List<Item> missing = new ArrayList<>();
            variantsByOre.forEach((ore, variants) -> {
                if (cooking.input().acceptsItem(ore.builtInRegistryHolder())) {
                    variants.stream()
                            .filter(variant -> !cooking.input().acceptsItem(variant.builtInRegistryHolder()))
                            .filter(variant -> existing == null || !existing.acceptsItem(variant.builtInRegistryHolder()))
                            .forEach(missing::add);
                }
            });
            if (missing.isEmpty()) {
                continue;
            }
            JsonArray ingredient = new JsonArray();
            if (existing != null) {
                existing.items().forEach(item -> ingredient.add(item.unwrapKey().orElseThrow().identifier().toString()));
            }
            missing.forEach(variant -> ingredient.add(BuiltInRegistries.ITEM.getKey(variant).toString()));
            var copy = Recipe.CODEC.encodeStart(ops, holder.value())
                    .map(json -> {
                        json.getAsJsonObject().add("ingredient", ingredient);
                        return json;
                    })
                    .flatMap(json -> Recipe.CODEC.parse(ops, json));
            copy.ifError(error -> LOGGER.warn("Couldn't copy recipe {} for the variants of its ore: {}", id, error.message()));
            if (copy.isSuccess()) {
                result.put(copyId, new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, copyId), copy.getOrThrow()));
                copied++;
            }
        }
        LOGGER.info("Copied {} cooking recipes for ore variants", copied);
        return new ArrayList<>(result.values());
    }
}
