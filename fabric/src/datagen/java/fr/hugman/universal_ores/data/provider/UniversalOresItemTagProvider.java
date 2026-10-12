package fr.hugman.universal_ores.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class UniversalOresItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public UniversalOresItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(@NotNull HolderLookup.Provider registries) {
        OreVariantTags.forEachTag(Registries.ITEM, (tag, variants) -> builder(tag).add(variants.toArray(ResourceKey[]::new)));
    }
}
