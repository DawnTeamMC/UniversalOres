package fr.hugman.universal_ores.data.provider;

import com.google.gson.JsonElement;
import fr.hugman.universal_ores.data.BuiltInOreVariants;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Puts each variant in the vanilla tags listing its original ore, like the variants of other mods are at load time.
 */
final class OreVariantTags {
    private OreVariantTags() {
    }

    static <T> void forEachTag(ResourceKey<? extends Registry<T>> registry, BiConsumer<TagKey<T>, List<ResourceKey<T>>> consumer) {
        Map<Identifier, List<Identifier>> variantsByOre = new HashMap<>();
        for (RegisteredOreVariant variant : BuiltInOreVariants.all()) {
            variantsByOre.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(variant.ore()), id -> new ArrayList<>()).add(variant.variant().id());
        }
        BuiltInOreVariants.forEachVanillaFile(Registries.tagsDirPath(registry), (tag, json) -> {
            List<ResourceKey<T>> variants = new ArrayList<>();
            for (JsonElement value : json.getAsJsonObject().getAsJsonArray("values")) {
                String entry = value.isJsonObject() ? value.getAsJsonObject().get("id").getAsString() : value.getAsString();
                if (!entry.startsWith("#")) {
                    variantsByOre.getOrDefault(Identifier.parse(entry), List.of()).forEach(id -> variants.add(ResourceKey.create(registry, id)));
                }
            }
            if (!variants.isEmpty()) {
                consumer.accept(TagKey.create(registry, tag), variants);
            }
        });
    }
}
