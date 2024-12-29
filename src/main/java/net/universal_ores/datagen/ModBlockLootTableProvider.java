package net.universal_ores.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.universal_ores.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    public ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {




        addDrop(ModBlocks.GRANITE_DIAMOND_ORE, oreDrops(ModBlocks.GRANITE_DIAMOND_ORE, Items.DIAMOND));
        addDrop(ModBlocks.ANDESITE_DIAMOND_ORE, oreDrops(ModBlocks.ANDESITE_DIAMOND_ORE, Items.DIAMOND));
        addDrop(ModBlocks.DIORITE_DIAMOND_ORE, oreDrops(ModBlocks.DIORITE_DIAMOND_ORE, Items.DIAMOND));
        addDrop(ModBlocks.TUFF_DIAMOND_ORE, oreDrops(ModBlocks.TUFF_DIAMOND_ORE, Items.DIAMOND));
        addDrop(ModBlocks.CALCITE_DIAMOND_ORE, oreDrops(ModBlocks.CALCITE_DIAMOND_ORE, Items.DIAMOND));



        addDrop(ModBlocks.GRANITE_EMERALD_ORE, oreDrops(ModBlocks.GRANITE_EMERALD_ORE, Items.EMERALD));
        addDrop(ModBlocks.ANDESITE_EMERALD_ORE, oreDrops(ModBlocks.ANDESITE_EMERALD_ORE, Items.EMERALD));
        addDrop(ModBlocks.DIORITE_EMERALD_ORE, oreDrops(ModBlocks.DIORITE_EMERALD_ORE, Items.EMERALD));
        addDrop(ModBlocks.TUFF_EMERALD_ORE, oreDrops(ModBlocks.TUFF_EMERALD_ORE, Items.EMERALD));
        addDrop(ModBlocks.CALCITE_EMERALD_ORE, oreDrops(ModBlocks.CALCITE_EMERALD_ORE, Items.EMERALD));



        addDrop(ModBlocks.GRANITE_COAL_ORE, oreDrops(ModBlocks.GRANITE_COAL_ORE, Items.COAL));
        addDrop(ModBlocks.ANDESITE_COAL_ORE, oreDrops(ModBlocks.ANDESITE_COAL_ORE, Items.COAL));
        addDrop(ModBlocks.DIORITE_COAL_ORE, oreDrops(ModBlocks.DIORITE_COAL_ORE, Items.COAL));
        addDrop(ModBlocks.TUFF_COAL_ORE, oreDrops(ModBlocks.TUFF_COAL_ORE, Items.COAL));
        addDrop(ModBlocks.CALCITE_COAL_ORE, oreDrops(ModBlocks.CALCITE_COAL_ORE, Items.COAL));




        addDrop(ModBlocks.GRANITE_GOLD_ORE, oreDrops(ModBlocks.GRANITE_GOLD_ORE, Items.RAW_GOLD));
        addDrop(ModBlocks.ANDESITE_GOLD_ORE, oreDrops(ModBlocks.ANDESITE_GOLD_ORE, Items.RAW_GOLD));
        addDrop(ModBlocks.DIORITE_GOLD_ORE, oreDrops(ModBlocks.DIORITE_GOLD_ORE, Items.RAW_GOLD));
        addDrop(ModBlocks.TUFF_GOLD_ORE, oreDrops(ModBlocks.TUFF_GOLD_ORE, Items.RAW_GOLD));
        addDrop(ModBlocks.CALCITE_GOLD_ORE, oreDrops(ModBlocks.CALCITE_GOLD_ORE, Items.RAW_GOLD));



        addDrop(ModBlocks.GRANITE_IRON_ORE, oreDrops(ModBlocks.GRANITE_IRON_ORE, Items.RAW_IRON));
        addDrop(ModBlocks.ANDESITE_IRON_ORE, oreDrops(ModBlocks.ANDESITE_IRON_ORE, Items.RAW_IRON));
        addDrop(ModBlocks.DIORITE_IRON_ORE, oreDrops(ModBlocks.DIORITE_IRON_ORE, Items.RAW_IRON));
        addDrop(ModBlocks.TUFF_IRON_ORE, oreDrops(ModBlocks.TUFF_IRON_ORE, Items.RAW_IRON));
        addDrop(ModBlocks.CALCITE_IRON_ORE, oreDrops(ModBlocks.CALCITE_IRON_ORE, Items.RAW_IRON));





        addDrop(ModBlocks.GRANITE_REDSTONE_ORE, multipleOreDrops(ModBlocks.GRANITE_REDSTONE_ORE, Items.REDSTONE, 4,5));
        addDrop(ModBlocks.ANDESITE_REDSTONE_ORE, multipleOreDrops(ModBlocks.ANDESITE_REDSTONE_ORE, Items.REDSTONE, 4,5));
        addDrop(ModBlocks.DIORITE_REDSTONE_ORE, multipleOreDrops(ModBlocks.DIORITE_REDSTONE_ORE, Items.REDSTONE, 4,5));
        addDrop(ModBlocks.TUFF_REDSTONE_ORE, multipleOreDrops(ModBlocks.TUFF_REDSTONE_ORE, Items.REDSTONE, 4,5));
        addDrop(ModBlocks.CALCITE_REDSTONE_ORE, multipleOreDrops(ModBlocks.CALCITE_REDSTONE_ORE, Items.REDSTONE, 4,5));



        addDrop(ModBlocks.GRANITE_COPPER_ORE, multipleOreDrops(ModBlocks.GRANITE_COPPER_ORE, Items.RAW_COPPER, 2, 5));
        addDrop(ModBlocks.ANDESITE_COPPER_ORE, multipleOreDrops(ModBlocks.ANDESITE_COPPER_ORE, Items.RAW_COPPER, 2, 5));
        addDrop(ModBlocks.DIORITE_COPPER_ORE, multipleOreDrops(ModBlocks.DIORITE_COPPER_ORE, Items.RAW_COPPER, 2, 5));
        addDrop(ModBlocks.TUFF_COPPER_ORE, multipleOreDrops(ModBlocks.TUFF_COPPER_ORE, Items.RAW_COPPER, 2, 5));
        addDrop(ModBlocks.CALCITE_COPPER_ORE, multipleOreDrops(ModBlocks.CALCITE_COPPER_ORE, Items.RAW_COPPER, 2, 5));



        addDrop(ModBlocks.GRANITE_LAPIS_ORE, multipleOreDrops(ModBlocks.GRANITE_LAPIS_ORE, Items.LAPIS_LAZULI, 4,9));
        addDrop(ModBlocks.ANDESITE_LAPIS_ORE, multipleOreDrops(ModBlocks.ANDESITE_LAPIS_ORE, Items.LAPIS_LAZULI, 4,9));
        addDrop(ModBlocks.DIORITE_LAPIS_ORE, multipleOreDrops(ModBlocks.DIORITE_LAPIS_ORE, Items.LAPIS_LAZULI, 4,9));
        addDrop(ModBlocks.TUFF_LAPIS_ORE, multipleOreDrops(ModBlocks.TUFF_LAPIS_ORE, Items.LAPIS_LAZULI, 4,9));
        addDrop(ModBlocks.CALCITE_LAPIS_ORE, multipleOreDrops(ModBlocks.CALCITE_LAPIS_ORE, Items.LAPIS_LAZULI, 4,9));






        addDrop(ModBlocks.BLACKSTONE_GOLD_ORE, multipleOreDrops(ModBlocks.BLACKSTONE_GOLD_ORE, Items.GOLD_NUGGET,2,6));
        addDrop(ModBlocks.BASALT_GOLD_ORE, multipleOreDrops(ModBlocks.BASALT_GOLD_ORE, Items.GOLD_NUGGET,2,6));
        addDrop(ModBlocks.BLACKSTONE_QUARTZ_ORE, oreDrops(ModBlocks.BLACKSTONE_QUARTZ_ORE, Items.QUARTZ));
        addDrop(ModBlocks.BASALT_QUARTZ_ORE, oreDrops(ModBlocks.BASALT_QUARTZ_ORE, Items.QUARTZ));

    }
    public LootTable.Builder multipleOreDrops(Block drop, Item item, float minDrops, float maxDrops) {
        RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        return this.dropsWithSilkTouch(drop, this.applyExplosionDecay(drop, ((LeafEntry.Builder<?>)
                ItemEntry.builder(item).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minDrops, maxDrops))))
                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))));
    }
}
