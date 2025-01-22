package fr.hugman.universal_ores.block;

import fr.hugman.universal_ores.UniversalOres;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.function.Function;
import java.util.function.Supplier;

public class UniversalOresBlocks {
    public static final Function<AbstractBlock.Settings, Block> NO_EXP_ORE = s -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), s);
    public static final Function<AbstractBlock.Settings, Block> REDSTONE_ORE = s -> new RedstoneOreBlock(s.ticksRandomly().luminance((state) -> (Boolean) state.get(Properties.LIT) ? 9 : 0));
    public static final Function<AbstractBlock.Settings, Block> COAL_ORE = s -> new ExperienceDroppingBlock(UniformIntProvider.create(0, 2), s);
    public static final Function<AbstractBlock.Settings, Block> EMERALD_ORE = s -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), s);
    public static final Function<AbstractBlock.Settings, Block> LAPIS_ORE = s -> new ExperienceDroppingBlock(UniformIntProvider.create(2, 5), s);
    public static final Function<AbstractBlock.Settings, Block> DIAMOND_ORE = s -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), s);
    public static final Function<AbstractBlock.Settings, Block> NETHER_GOLD_ORE = s -> new ExperienceDroppingBlock(UniformIntProvider.create(0, 1), s.sounds(BlockSoundGroup.NETHER_GOLD_ORE));
    public static final Function<AbstractBlock.Settings, Block> QUARTZ_ORE = s -> new ExperienceDroppingBlock(UniformIntProvider.create(2, 5), s.sounds(BlockSoundGroup.NETHER_ORE));

    public static final Supplier<AbstractBlock.Settings> GRANITE = () -> AbstractBlock.Settings.copyShallow(Blocks.GRANITE).strength(3.0F, 3.0F);
    public static final Supplier<AbstractBlock.Settings> DIORITE = () -> AbstractBlock.Settings.copyShallow(Blocks.DIORITE).strength(3.0F, 3.0F);
    public static final Supplier<AbstractBlock.Settings> ANDESITE = () -> AbstractBlock.Settings.copyShallow(Blocks.ANDESITE).strength(3.0F, 3.0F);
    public static final Supplier<AbstractBlock.Settings> TUFF = () -> AbstractBlock.Settings.copyShallow(Blocks.TUFF).strength(3.0F, 3.0F);
    public static final Supplier<AbstractBlock.Settings> CALCITE = () -> AbstractBlock.Settings.copyShallow(Blocks.CALCITE).strength(0.5F, 0.5F);
    public static final Supplier<AbstractBlock.Settings> BLACKSTONE = () -> AbstractBlock.Settings.copyShallow(Blocks.BLACKSTONE).strength(3.0F, 3.0F);
    public static final Supplier<AbstractBlock.Settings> BASALT = () -> AbstractBlock.Settings.copyShallow(Blocks.BASALT).strength(3.0F, 3.0F);

    public static final OverworldOreBlocks GRANITE_ORES = registerOverworldOres("granite", GRANITE);
    public static final OverworldOreBlocks DIORITE_ORES = registerOverworldOres("diorite", DIORITE);
    public static final OverworldOreBlocks ANDESITE_ORES = registerOverworldOres("andesite", ANDESITE);
    public static final OverworldOreBlocks TUFF_ORES = registerOverworldOres("tuff", TUFF);
    public static final OverworldOreBlocks CALCITE_ORES = registerOverworldOres("calcite", CALCITE);
    public static final NetherOreBlocks BLACKSTONE_ORES = registerNetherOres("blackstone", BLACKSTONE);
    public static final NetherOreBlocks BASALT_ORES = registerNetherOres("basalt", BASALT);

    private static OverworldOreBlocks registerOverworldOres(String prefix, Supplier<AbstractBlock.Settings> settingsSupplier) {
        return new OverworldOreBlocks(
                register(prefix + "_coal_ore", COAL_ORE, settingsSupplier),
                register(prefix + "_iron_ore", NO_EXP_ORE, settingsSupplier),
                register(prefix + "_copper_ore", NO_EXP_ORE, settingsSupplier),
                register(prefix + "_gold_ore", NO_EXP_ORE, settingsSupplier),
                register(prefix + "_redstone_ore", REDSTONE_ORE, settingsSupplier),
                register(prefix + "_emerald_ore", EMERALD_ORE, settingsSupplier),
                register(prefix + "_lapis_ore", LAPIS_ORE, settingsSupplier),
                register(prefix + "_diamond_ore", DIAMOND_ORE, settingsSupplier)
        );
    }

    private static NetherOreBlocks registerNetherOres(String prefix, Supplier<AbstractBlock.Settings> settingsSupplier) {
        return new NetherOreBlocks(
                register(prefix + "_gold_ore", NETHER_GOLD_ORE, settingsSupplier),
                register(prefix + "_quartz_ore", QUARTZ_ORE, settingsSupplier)
        );
    }

    private static Block register(String key, Function<AbstractBlock.Settings, Block> factory, Supplier<AbstractBlock.Settings> supplier) {
        return register(keyOf(key), factory, supplier.get());
    }

    private static Block register(RegistryKey<Block> key, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings blockSettings) {
        var block = factory.apply(blockSettings);
        Registry.register(Registries.BLOCK, key, block);

        var itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, key.getValue());
        Registry.register(Registries.ITEM, itemRegistryKey, new BlockItem(block, new Item.Settings()));

        return block;
    }

    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, UniversalOres.id(id));
    }
}
