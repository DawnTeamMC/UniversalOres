package fr.hugman.universal_ores.data.provider;

import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class UniversalOresBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public UniversalOresBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider registries) {
        // Promenade
        valueLookupBuilder(BlockTags.COAL_ORES).add(UniversalOresBlocks.COAL_ORES);
        valueLookupBuilder(BlockTags.IRON_ORES).add(UniversalOresBlocks.IRON_ORES);
        valueLookupBuilder(BlockTags.GOLD_ORES).add(UniversalOresBlocks.GOLD_ORES).add(UniversalOresBlocks.NETHER_GOLD_ORES);
        valueLookupBuilder(BlockTags.COPPER_ORES).add(UniversalOresBlocks.COPPER_ORES);
        valueLookupBuilder(BlockTags.REDSTONE_ORES).add(UniversalOresBlocks.REDSTONE_ORES);
        valueLookupBuilder(BlockTags.LAPIS_ORES).add(UniversalOresBlocks.LAPIS_ORES);
        valueLookupBuilder(BlockTags.DIAMOND_ORES).add(UniversalOresBlocks.DIAMOND_ORES);
        valueLookupBuilder(BlockTags.EMERALD_ORES).add(UniversalOresBlocks.EMERALD_ORES);

        valueLookupBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(UniversalOresBlocks.GOLD_ORES)
                .add(UniversalOresBlocks.REDSTONE_ORES)
                .add(UniversalOresBlocks.EMERALD_ORES)
                .add(UniversalOresBlocks.DIAMOND_ORES);

        valueLookupBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(UniversalOresBlocks.IRON_ORES)
                .add(UniversalOresBlocks.COPPER_ORES)
                .add(UniversalOresBlocks.LAPIS_ORES);

        valueLookupBuilder(BlockTags.SNAPS_GOAT_HORN)
                .add(UniversalOresBlocks.COAL_ORES)
                .add(UniversalOresBlocks.IRON_ORES)
                .add(UniversalOresBlocks.COPPER_ORES)
                .add(UniversalOresBlocks.EMERALD_ORES);

        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(UniversalOresBlocks.COAL_ORES)
                .add(UniversalOresBlocks.IRON_ORES)
                .add(UniversalOresBlocks.GOLD_ORES)
                .add(UniversalOresBlocks.COPPER_ORES)
                .add(UniversalOresBlocks.REDSTONE_ORES)
                .add(UniversalOresBlocks.LAPIS_ORES)
                .add(UniversalOresBlocks.EMERALD_ORES)
                .add(UniversalOresBlocks.DIAMOND_ORES)
                .add(UniversalOresBlocks.NETHER_GOLD_ORES)
                .add(UniversalOresBlocks.QUARTZ_ORES);

        valueLookupBuilder(ConventionalBlockTags.QUARTZ_ORES)
                .add(UniversalOresBlocks.QUARTZ_ORES);
    }
}