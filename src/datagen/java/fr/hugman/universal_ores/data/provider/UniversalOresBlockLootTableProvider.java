package fr.hugman.universal_ores.data.provider;

import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class UniversalOresBlockLootTableProvider extends FabricBlockLootTableProvider {
    public UniversalOresBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        var enchantments = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);

        for (var block : UniversalOresBlocks.COAL_ORES) {
            this.addDrop(block, b -> this.oreDrops(b, Items.COAL));
        }
        for (var block : UniversalOresBlocks.IRON_ORES) {
            this.addDrop(block, b -> this.oreDrops(b, Items.RAW_IRON));
        }
        for (var block : UniversalOresBlocks.GOLD_ORES) {
            this.addDrop(block, b -> this.oreDrops(b, Items.RAW_GOLD));
        }
        for (var block : UniversalOresBlocks.COPPER_ORES) {
            this.addDrop(block, this::copperOreDrops);
        }
        for (var block : UniversalOresBlocks.LAPIS_ORES) {
            this.addDrop(block, this::lapisOreDrops);
        }
        for (var block : UniversalOresBlocks.REDSTONE_ORES) {
            this.addDrop(block, this::redstoneOreDrops);
        }
        for (var block : UniversalOresBlocks.EMERALD_ORES) {
            this.addDrop(block, b -> this.oreDrops(b, Items.EMERALD));
        }
        for (var block : UniversalOresBlocks.DIAMOND_ORES) {
            this.addDrop(block, b -> this.oreDrops(b, Items.DIAMOND));
        }
        for (var block : UniversalOresBlocks.NETHER_GOLD_ORES) {
            this.addDrop(block, b -> this.dropsWithSilkTouch(b,
                    this.applyExplosionDecay(b, ItemEntry.builder(Items.GOLD_NUGGET)
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 6.0F)))
                            .apply(ApplyBonusLootFunction.oreDrops(enchantments.getOrThrow(Enchantments.FORTUNE)))
                    )
            ));
        }
        for (var block : UniversalOresBlocks.QUARTZ_ORES) {
            this.addDrop(block, b -> this.oreDrops(b, Items.QUARTZ));

        }
    }
}