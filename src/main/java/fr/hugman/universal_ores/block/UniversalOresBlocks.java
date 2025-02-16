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

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public class UniversalOresBlocks {
    private static final Function<AbstractBlock.Settings, Block> NO_EXP_ORE = s -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), s);
    private static final Function<AbstractBlock.Settings, Block> REDSTONE_ORE = s -> new RedstoneOreBlock(s.ticksRandomly().luminance((state) -> (Boolean) state.get(Properties.LIT) ? 9 : 0));
    private static final Function<AbstractBlock.Settings, Block> COAL_ORE = s -> new ExperienceDroppingBlock(UniformIntProvider.create(0, 2), s);
    private static final Function<AbstractBlock.Settings, Block> EMERALD_ORE = s -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), s);
    private static final Function<AbstractBlock.Settings, Block> LAPIS_ORE = s -> new ExperienceDroppingBlock(UniformIntProvider.create(2, 5), s);
    private static final Function<AbstractBlock.Settings, Block> DIAMOND_ORE = s -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), s);
    private static final Function<AbstractBlock.Settings, Block> NETHER_GOLD_ORE = s -> new ExperienceDroppingBlock(UniformIntProvider.create(0, 1), s.sounds(BlockSoundGroup.NETHER_GOLD_ORE));
    private static final Function<AbstractBlock.Settings, Block> QUARTZ_ORE = s -> new ExperienceDroppingBlock(UniformIntProvider.create(2, 5), s.sounds(BlockSoundGroup.NETHER_ORE));
    private static final Function<AbstractBlock.Settings, Block> NETHER_GOLD_ORE_PILLAR = s -> new DropExperienceRotatedPillarBlock(UniformIntProvider.create(0, 1), s.sounds(BlockSoundGroup.NETHER_GOLD_ORE));
    private static final Function<AbstractBlock.Settings, Block> QUARTZ_ORE_PILLAR = s -> new DropExperienceRotatedPillarBlock(UniformIntProvider.create(2, 5), s.sounds(BlockSoundGroup.NETHER_ORE));

    private static final Supplier<AbstractBlock.Settings> GRANITE = () -> AbstractBlock.Settings.copyShallow(Blocks.GRANITE).strength(3.0F, 3.0F);
    private static final Supplier<AbstractBlock.Settings> DIORITE = () -> AbstractBlock.Settings.copyShallow(Blocks.DIORITE).strength(3.0F, 3.0F);
    private static final Supplier<AbstractBlock.Settings> ANDESITE = () -> AbstractBlock.Settings.copyShallow(Blocks.ANDESITE).strength(3.0F, 3.0F);
    private static final Supplier<AbstractBlock.Settings> TUFF = () -> AbstractBlock.Settings.copyShallow(Blocks.TUFF).strength(3.0F, 3.0F);
    private static final Supplier<AbstractBlock.Settings> CALCITE = () -> AbstractBlock.Settings.copyShallow(Blocks.CALCITE).strength(0.5F, 0.5F);
    private static final Supplier<AbstractBlock.Settings> BLACKSTONE = () -> AbstractBlock.Settings.copyShallow(Blocks.BLACKSTONE).strength(3.0F, 3.0F);
    private static final Supplier<AbstractBlock.Settings> BASALT = () -> AbstractBlock.Settings.copyShallow(Blocks.BASALT).strength(3.0F, 3.0F);

    public static final OverworldOreBlocks GRANITE_ORES = registerOverworldOres("granite", GRANITE);
    public static final OverworldOreBlocks DIORITE_ORES = registerOverworldOres("diorite", DIORITE);
    public static final OverworldOreBlocks ANDESITE_ORES = registerOverworldOres("andesite", ANDESITE);
    public static final OverworldOreBlocks TUFF_ORES = registerOverworldOres("tuff", TUFF);
    public static final OverworldOreBlocks CALCITE_ORES = registerOverworldOres("calcite", CALCITE);
    public static final NetherOreBlocks BLACKSTONE_ORES = registerNetherOres("blackstone", BLACKSTONE);
    public static final NetherOreBlocks BASALT_ORES = registerNetherPillarOres("basalt", BASALT);

    public static final OverworldOreBlocks[] OVERWORLD_ORE_BLOCKS = new OverworldOreBlocks[]{GRANITE_ORES, DIORITE_ORES, ANDESITE_ORES, TUFF_ORES, CALCITE_ORES};
    public static final NetherOreBlocks[] NETHER_ORE_BLOCKS = new NetherOreBlocks[]{BLACKSTONE_ORES, BASALT_ORES};

    public static final Block[] COAL_ORES = Arrays.stream(UniversalOresBlocks.OVERWORLD_ORE_BLOCKS).map(OverworldOreBlocks::coal).toArray(Block[]::new);
    public static final Block[] IRON_ORES = Arrays.stream(UniversalOresBlocks.OVERWORLD_ORE_BLOCKS).map(OverworldOreBlocks::iron).toArray(Block[]::new);
    public static final Block[] GOLD_ORES = Arrays.stream(UniversalOresBlocks.OVERWORLD_ORE_BLOCKS).map(OverworldOreBlocks::gold).toArray(Block[]::new);
    public static final Block[] COPPER_ORES = Arrays.stream(UniversalOresBlocks.OVERWORLD_ORE_BLOCKS).map(OverworldOreBlocks::copper).toArray(Block[]::new);
    public static final Block[] LAPIS_ORES = Arrays.stream(UniversalOresBlocks.OVERWORLD_ORE_BLOCKS).map(OverworldOreBlocks::lapis).toArray(Block[]::new);
    public static final Block[] REDSTONE_ORES = Arrays.stream(UniversalOresBlocks.OVERWORLD_ORE_BLOCKS).map(OverworldOreBlocks::redstone).toArray(Block[]::new);
    public static final Block[] EMERALD_ORES = Arrays.stream(UniversalOresBlocks.OVERWORLD_ORE_BLOCKS).map(OverworldOreBlocks::emerald).toArray(Block[]::new);
    public static final Block[] DIAMOND_ORES = Arrays.stream(UniversalOresBlocks.OVERWORLD_ORE_BLOCKS).map(OverworldOreBlocks::diamond).toArray(Block[]::new);

    public static final Block[] NETHER_GOLD_ORES = Arrays.stream(UniversalOresBlocks.NETHER_ORE_BLOCKS).map(NetherOreBlocks::gold).toArray(Block[]::new);
    public static final Block[] QUARTZ_ORES = Arrays.stream(UniversalOresBlocks.NETHER_ORE_BLOCKS).map(NetherOreBlocks::quartz).toArray(Block[]::new);


    private static OverworldOreBlocks registerOverworldOres(String prefix, Supplier<AbstractBlock.Settings> settingsSupplier) {
        return new OverworldOreBlocks(
                register(prefix + "_coal_ore", COAL_ORE, settingsSupplier),
                register(prefix + "_iron_ore", NO_EXP_ORE, settingsSupplier),
                register(prefix + "_gold_ore", NO_EXP_ORE, settingsSupplier),
                register(prefix + "_copper_ore", NO_EXP_ORE, settingsSupplier),
                register(prefix + "_lapis_ore", LAPIS_ORE, settingsSupplier),
                register(prefix + "_redstone_ore", REDSTONE_ORE, settingsSupplier),
                register(prefix + "_emerald_ore", EMERALD_ORE, settingsSupplier),
                register(prefix + "_diamond_ore", DIAMOND_ORE, settingsSupplier)
        );
    }

    private static NetherOreBlocks registerNetherOres(String prefix, Supplier<AbstractBlock.Settings> settingsSupplier) {
        return new NetherOreBlocks(
                register(prefix + "_gold_ore", NETHER_GOLD_ORE, settingsSupplier),
                register(prefix + "_quartz_ore", QUARTZ_ORE, settingsSupplier)
        );
    }

    private static NetherOreBlocks registerNetherPillarOres(String prefix, Supplier<AbstractBlock.Settings> settingsSupplier) {
        return new NetherOreBlocks(
                register(prefix + "_gold_ore", NETHER_GOLD_ORE_PILLAR, settingsSupplier),
                register(prefix + "_quartz_ore", QUARTZ_ORE_PILLAR, settingsSupplier)
        );
    }

    private static Block register(String key, Function<AbstractBlock.Settings, Block> factory, Supplier<AbstractBlock.Settings> supplier) {
        return register(keyOf(key), factory, supplier.get());
    }

    private static Block register(RegistryKey<Block> key, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings blockSettings) {
        var block = factory.apply(blockSettings.registryKey(key));
        Registry.register(Registries.BLOCK, key, block);

        var itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, key.getValue());
        Registry.register(Registries.ITEM, itemRegistryKey, new BlockItem(block, new Item.Settings().registryKey(itemRegistryKey).useBlockPrefixedTranslationKey()));

        return block;
    }

    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, UniversalOres.id(id));
    }
}
