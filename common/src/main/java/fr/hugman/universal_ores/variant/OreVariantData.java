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
import net.minecraft.world.item.crafting.Recipe;
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
            for (RegisteredOreVariant variant : OreVariantRegistrar.registered()) {
                Optional<ResourceKey<LootTable>> lootTable = variant.block().getLootTable();
                if (lootTable.isEmpty()) {
                    continue;
                }
                Identifier id = lootTable.get().identifier();
                output.accept(id.withPath(path -> Registries.elementsDirPath(Registries.LOOT_TABLE) + "/" + path + ".json"), lootTable(variant, id));
            }
        });
    }

    private static JsonElement lootTable(RegisteredOreVariant variant, Identifier id) {
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
     * Adds the variants to the block and item tags listing their original ore.
     */
    public static void addToTags(String directory, Map<Identifier, List<TagLoader.EntryWithSource>> tags) {
        if (!directory.equals(BLOCK_TAGS) && !directory.equals(ITEM_TAGS)) {
            return;
        }
        Map<Identifier, List<Identifier>> variantsByOre = new HashMap<>();
        for (RegisteredOreVariant variant : OreVariantRegistrar.registered()) {
            variantsByOre.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(variant.ore()), id -> new ArrayList<>()).add(variant.variant().id());
        }
        if (variantsByOre.isEmpty()) {
            return;
        }
        tags.forEach((tag, entries) -> {
            String name = tag.toString();
            if (EXCLUDED_TAG_PREFIXES.stream().anyMatch(name::startsWith)) {
                return;
            }
            Set<Identifier> added = new LinkedHashSet<>();
            for (TagLoader.EntryWithSource entry : entries) {
                TagEntryAccessor accessor = (TagEntryAccessor) entry.entry();
                if (!accessor.isTag()) {
                    added.addAll(variantsByOre.getOrDefault(accessor.getId(), List.of()));
                }
            }
            for (Identifier variant : added) {
                entries.add(new TagLoader.EntryWithSource(TagEntry.optionalElement(variant), UniversalOres.MOD_ID));
            }
        });
    }

    /**
     * Copies the cooking recipes of the original ores for their variants, unless they already accept them.
     */
    public static void addCookingRecipes(Map<Identifier, Recipe<?>> recipes, HolderLookup.Provider registries) {
        Map<Item, List<Item>> variantsByOre = new LinkedHashMap<>();
        for (RegisteredOreVariant variant : OreVariantRegistrar.registered()) {
            variantsByOre.computeIfAbsent(variant.ore().asItem(), item -> new ArrayList<>()).add(variant.block().asItem());
        }
        if (variantsByOre.isEmpty()) {
            return;
        }
        DynamicOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);
        Map<Identifier, Recipe<?>> copies = new LinkedHashMap<>();
        recipes.forEach((id, recipe) -> {
            if (!(recipe instanceof AbstractCookingRecipe cooking)) {
                return;
            }
            // Item stacks can't be made yet: item components are bound after recipes are read
            List<Item> variants = new ArrayList<>();
            variantsByOre.forEach((ore, oreVariants) -> {
                if (cooking.input().acceptsItem(ore.builtInRegistryHolder())) {
                    oreVariants.stream().filter(variant -> !cooking.input().acceptsItem(variant.builtInRegistryHolder())).forEach(variants::add);
                }
            });
            if (variants.isEmpty()) {
                return;
            }
            JsonArray ingredient = new JsonArray();
            variants.forEach(variant -> ingredient.add(BuiltInRegistries.ITEM.getKey(variant).toString()));
            Recipe.CODEC.encodeStart(ops, recipe)
                    .map(json -> {
                        json.getAsJsonObject().add("ingredient", ingredient);
                        return json;
                    })
                    .flatMap(json -> Recipe.CODEC.parse(ops, json))
                    .ifSuccess(copy -> copies.put(UniversalOres.id(id.getNamespace() + "/" + id.getPath()), copy))
                    .ifError(error -> LOGGER.warn("Couldn't copy recipe {} for the variants of its ore: {}", id, error.message()));
        });
        copies.forEach(recipes::putIfAbsent);
    }
}
