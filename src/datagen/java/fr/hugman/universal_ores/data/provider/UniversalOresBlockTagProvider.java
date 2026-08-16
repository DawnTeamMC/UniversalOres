package fr.hugman.universal_ores.data.provider;

import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class UniversalOresBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public UniversalOresBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider registries) {
        // Promenade
        builder(BlockItemTags.COAL_ORES.block()).add(keys(UniversalOresBlocks.COAL_ORES));
        builder(BlockItemTags.IRON_ORES.block()).add(keys(UniversalOresBlocks.IRON_ORES));
        builder(BlockItemTags.GOLD_ORES.block()).add(keys(UniversalOresBlocks.GOLD_ORES, UniversalOresBlocks.NETHER_GOLD_ORES));
        builder(BlockItemTags.COPPER_ORES.block()).add(keys(UniversalOresBlocks.COPPER_ORES));
        builder(BlockItemTags.REDSTONE_ORES.block()).add(keys(UniversalOresBlocks.REDSTONE_ORES));
        builder(BlockItemTags.LAPIS_ORES.block()).add(keys(UniversalOresBlocks.LAPIS_ORES));
        builder(BlockItemTags.DIAMOND_ORES.block()).add(keys(UniversalOresBlocks.DIAMOND_ORES));
        builder(BlockItemTags.EMERALD_ORES.block()).add(keys(UniversalOresBlocks.EMERALD_ORES));

        builder(BlockTags.NEEDS_IRON_TOOL).add(keys(
                UniversalOresBlocks.GOLD_ORES,
                UniversalOresBlocks.REDSTONE_ORES,
                UniversalOresBlocks.EMERALD_ORES,
                UniversalOresBlocks.DIAMOND_ORES
        ));

        builder(BlockTags.NEEDS_STONE_TOOL).add(keys(
                UniversalOresBlocks.IRON_ORES,
                UniversalOresBlocks.COPPER_ORES,
                UniversalOresBlocks.LAPIS_ORES
        ));

        builder(BlockTags.SNAPS_GOAT_HORN).add(keys(
                UniversalOresBlocks.COAL_ORES,
                UniversalOresBlocks.IRON_ORES,
                UniversalOresBlocks.COPPER_ORES,
                UniversalOresBlocks.EMERALD_ORES
        ));

        builder(BlockTags.MINEABLE_WITH_PICKAXE).add(keys(
                UniversalOresBlocks.COAL_ORES,
                UniversalOresBlocks.IRON_ORES,
                UniversalOresBlocks.GOLD_ORES,
                UniversalOresBlocks.COPPER_ORES,
                UniversalOresBlocks.REDSTONE_ORES,
                UniversalOresBlocks.LAPIS_ORES,
                UniversalOresBlocks.EMERALD_ORES,
                UniversalOresBlocks.DIAMOND_ORES,
                UniversalOresBlocks.NETHER_GOLD_ORES,
                UniversalOresBlocks.QUARTZ_ORES
        ));

        builder(ConventionalBlockTags.QUARTZ_ORES).add(keys(UniversalOresBlocks.QUARTZ_ORES));
    }

    @SuppressWarnings("unchecked")
    private static ResourceKey<Block>[] keys(Block[]... groups) {
        return Arrays.stream(groups)
                .flatMap(Arrays::stream)
                .map(block -> BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow())
                .toArray(ResourceKey[]::new);
    }
}
