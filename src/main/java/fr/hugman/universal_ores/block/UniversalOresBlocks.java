package fr.hugman.universal_ores.block;

import fr.hugman.universal_ores.UniversalOres;
import net.minecraft.world.item.BlockItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public class UniversalOresBlocks {
    private static final Function<BlockBehaviour.Properties, Block> NO_EXP_ORE = s -> new DropExperienceBlock(ConstantInt.of(0), s);
    private static final Function<BlockBehaviour.Properties, Block> REDSTONE_ORE = s -> new RedStoneOreBlock(s.randomTicks().lightLevel((state) -> (Boolean) state.getValue(BlockStateProperties.LIT) ? 9 : 0));
    private static final Function<BlockBehaviour.Properties, Block> COAL_ORE = s -> new DropExperienceBlock(UniformInt.of(0, 2), s);
    private static final Function<BlockBehaviour.Properties, Block> EMERALD_ORE = s -> new DropExperienceBlock(UniformInt.of(3, 7), s);
    private static final Function<BlockBehaviour.Properties, Block> LAPIS_ORE = s -> new DropExperienceBlock(UniformInt.of(2, 5), s);
    private static final Function<BlockBehaviour.Properties, Block> DIAMOND_ORE = s -> new DropExperienceBlock(UniformInt.of(3, 7), s);
    private static final Function<BlockBehaviour.Properties, Block> NETHER_GOLD_ORE = s -> new DropExperienceBlock(UniformInt.of(0, 1), s.sound(SoundType.NETHER_GOLD_ORE));
    private static final Function<BlockBehaviour.Properties, Block> QUARTZ_ORE = s -> new DropExperienceBlock(UniformInt.of(2, 5), s.sound(SoundType.NETHER_ORE));
    private static final Function<BlockBehaviour.Properties, Block> NETHER_GOLD_ORE_PILLAR = s -> new DropExperienceRotatedPillarBlock(UniformInt.of(0, 1), s.sound(SoundType.NETHER_GOLD_ORE));
    private static final Function<BlockBehaviour.Properties, Block> QUARTZ_ORE_PILLAR = s -> new DropExperienceRotatedPillarBlock(UniformInt.of(2, 5), s.sound(SoundType.NETHER_ORE));

    private static final Supplier<BlockBehaviour.Properties> GRANITE = () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.GRANITE).strength(3.0F, 3.0F);
    private static final Supplier<BlockBehaviour.Properties> DIORITE = () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.DIORITE).strength(3.0F, 3.0F);
    private static final Supplier<BlockBehaviour.Properties> ANDESITE = () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.ANDESITE).strength(3.0F, 3.0F);
    private static final Supplier<BlockBehaviour.Properties> TUFF = () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.TUFF).strength(3.0F, 3.0F);
    private static final Supplier<BlockBehaviour.Properties> CALCITE = () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.CALCITE).strength(0.5F, 0.5F);
    private static final Supplier<BlockBehaviour.Properties> BLACKSTONE = () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.BLACKSTONE).strength(3.0F, 3.0F);
    private static final Supplier<BlockBehaviour.Properties> BASALT = () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.BASALT).strength(3.0F, 3.0F);

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


    private static OverworldOreBlocks registerOverworldOres(String prefix, Supplier<BlockBehaviour.Properties> settingsSupplier) {
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

    private static NetherOreBlocks registerNetherOres(String prefix, Supplier<BlockBehaviour.Properties> settingsSupplier) {
        return new NetherOreBlocks(
                register(prefix + "_gold_ore", NETHER_GOLD_ORE, settingsSupplier),
                register(prefix + "_quartz_ore", QUARTZ_ORE, settingsSupplier)
        );
    }

    private static NetherOreBlocks registerNetherPillarOres(String prefix, Supplier<BlockBehaviour.Properties> settingsSupplier) {
        return new NetherOreBlocks(
                register(prefix + "_gold_ore", NETHER_GOLD_ORE_PILLAR, settingsSupplier),
                register(prefix + "_quartz_ore", QUARTZ_ORE_PILLAR, settingsSupplier)
        );
    }

    private static Block register(String key, Function<BlockBehaviour.Properties, Block> factory, Supplier<BlockBehaviour.Properties> supplier) {
        return register(keyOf(key), factory, supplier.get());
    }

    private static Block register(ResourceKey<Block> key, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties blockSettings) {
        var block = factory.apply(blockSettings.setId(key));
        Registry.register(BuiltInRegistries.BLOCK, key, block);

        var itemRegistryKey = ResourceKey.create(Registries.ITEM, key.identifier());
        Registry.register(BuiltInRegistries.ITEM, itemRegistryKey, new BlockItem(block, new Item.Properties().setId(itemRegistryKey).useBlockDescriptionPrefix()));

        return block;
    }

    private static ResourceKey<Block> keyOf(String id) {
        return ResourceKey.create(Registries.BLOCK, UniversalOres.id(id));
    }
}
