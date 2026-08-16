package fr.hugman.universal_ores.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.tags.BlockItemTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class UniversalOresItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public UniversalOresItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable BlockTagsProvider blockTagsProvider) {
        super(output, completableFuture, blockTagsProvider);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider registries) {
        copy(BlockItemTags.GOLD_ORES);
        copy(BlockItemTags.IRON_ORES);
        copy(BlockItemTags.DIAMOND_ORES);
        copy(BlockItemTags.REDSTONE_ORES);
        copy(BlockItemTags.LAPIS_ORES);
        copy(BlockItemTags.COAL_ORES);
        copy(BlockItemTags.EMERALD_ORES);
        copy(BlockItemTags.COPPER_ORES);
        copy(ConventionalBlockTags.QUARTZ_ORES, ConventionalItemTags.QUARTZ_ORES);
    }

    private void copy(BlockItemTagId tag) {
        copy(tag.block(), tag.item());
    }
}
