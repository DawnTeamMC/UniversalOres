package fr.hugman.universal_ores.data.provider;

import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class UniversalOresBlockLootTableProvider extends FabricBlockLootSubProvider {
    public UniversalOresBlockLootTableProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        var enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        for (var block : UniversalOresBlocks.COAL_ORES) {
            this.add(block, b -> this.createOreDrop(b, Items.COAL));
        }
        for (var block : UniversalOresBlocks.IRON_ORES) {
            this.add(block, b -> this.createOreDrop(b, Items.RAW_IRON));
        }
        for (var block : UniversalOresBlocks.GOLD_ORES) {
            this.add(block, b -> this.createOreDrop(b, Items.RAW_GOLD));
        }
        for (var block : UniversalOresBlocks.COPPER_ORES) {
            this.add(block, this::createCopperOreDrops);
        }
        for (var block : UniversalOresBlocks.LAPIS_ORES) {
            this.add(block, this::createLapisOreDrops);
        }
        for (var block : UniversalOresBlocks.REDSTONE_ORES) {
            this.add(block, this::createRedstoneOreDrops);
        }
        for (var block : UniversalOresBlocks.EMERALD_ORES) {
            this.add(block, b -> this.createOreDrop(b, Items.EMERALD));
        }
        for (var block : UniversalOresBlocks.DIAMOND_ORES) {
            this.add(block, b -> this.createOreDrop(b, Items.DIAMOND));
        }
        for (var block : UniversalOresBlocks.NETHER_GOLD_ORES) {
            this.add(block, b -> this.createSilkTouchDispatchTable(b,
                    this.applyExplosionDecay(b, LootItem.lootTableItem(Items.GOLD_NUGGET)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F)))
                            .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
                    )
            ));
        }
        for (var block : UniversalOresBlocks.QUARTZ_ORES) {
            this.add(block, b -> this.createOreDrop(b, Items.QUARTZ));

        }
    }
}