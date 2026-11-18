package fr.hugman.universal_ores.data.provider;

import com.google.gson.JsonElement;
import fr.hugman.universal_ores.data.BuiltInOreVariants;
import fr.hugman.universal_ores.variant.OreVariantData;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * The same loot tables as the ones made at load time for the variants of other mods: the original ore's loot table,
 * with the original ore item swapped for the variant. Data packs changing the ore's drops change the variant's too.
 */
public class UniversalOresBlockLootTableProvider extends FabricCodecDataProvider<JsonElement> {
    public UniversalOresBlockLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, Registries.LOOT_TABLE, ExtraCodecs.JSON);
    }

    @Override
    protected void configure(BiConsumer<Identifier, JsonElement> provider, HolderLookup.Provider registries) {
        for (RegisteredOreVariant variant : BuiltInOreVariants.all()) {
            variant.block().getLootTable().ifPresent(key -> provider.accept(key.identifier(), OreVariantData.lootTable(variant)));
        }
    }

    @Override
    public @NotNull String getName() {
        return "Block Loot Tables";
    }
}
