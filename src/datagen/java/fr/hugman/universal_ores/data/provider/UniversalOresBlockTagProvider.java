package fr.hugman.universal_ores.data.provider;

import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class UniversalOresBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public UniversalOresBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        // Promenade
        getOrCreateTagBuilder(BlockTags.COAL_ORES).add(UniversalOresBlocks.COAL_ORES);
        getOrCreateTagBuilder(BlockTags.IRON_ORES).add(UniversalOresBlocks.IRON_ORES);
        getOrCreateTagBuilder(BlockTags.GOLD_ORES).add(UniversalOresBlocks.GOLD_ORES).add(UniversalOresBlocks.NETHER_GOLD_ORES);
        getOrCreateTagBuilder(BlockTags.COPPER_ORES).add(UniversalOresBlocks.COPPER_ORES);
        getOrCreateTagBuilder(BlockTags.REDSTONE_ORES).add(UniversalOresBlocks.REDSTONE_ORES);
        getOrCreateTagBuilder(BlockTags.LAPIS_ORES).add(UniversalOresBlocks.LAPIS_ORES);
        getOrCreateTagBuilder(BlockTags.DIAMOND_ORES).add(UniversalOresBlocks.DIAMOND_ORES);
        getOrCreateTagBuilder(BlockTags.EMERALD_ORES).add(UniversalOresBlocks.EMERALD_ORES);

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(UniversalOresBlocks.GOLD_ORES)
                .add(UniversalOresBlocks.REDSTONE_ORES)
                .add(UniversalOresBlocks.EMERALD_ORES)
                .add(UniversalOresBlocks.DIAMOND_ORES);

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(UniversalOresBlocks.IRON_ORES)
                .add(UniversalOresBlocks.COPPER_ORES)
                .add(UniversalOresBlocks.LAPIS_ORES);

        getOrCreateTagBuilder(BlockTags.SNAPS_GOAT_HORN)
                .add(UniversalOresBlocks.COAL_ORES)
                .add(UniversalOresBlocks.IRON_ORES)
                .add(UniversalOresBlocks.COPPER_ORES)
                .add(UniversalOresBlocks.EMERALD_ORES);

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
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

        getOrCreateTagBuilder(ConventionalBlockTags.QUARTZ_ORES)
                .add(UniversalOresBlocks.QUARTZ_ORES);
    }
}