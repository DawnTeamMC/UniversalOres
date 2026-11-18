package fr.hugman.universal_ores.variant;

import com.mojang.logging.LogUtils;
import fr.hugman.universal_ores.block.DropExperienceRotatedPillarBlock;
import fr.hugman.universal_ores.mixin.DropExperienceBlockAccessor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.slf4j.Logger;

import java.util.*;

/**
 * Registers the block and item of every ore variant whose stone and original ore exist.
 * <p>
 * Mods register their blocks in no particular order, so the loaders call {@link #registerAvailable()} once every
 * mod has registered its blocks, just before the registries are frozen.
 * <p>
 * A variant behaves like its original ore (hardness, drops, experience, light, random ticks) and looks and sounds
 * like its stone. Its ID only depends on the two blocks, never on the loading order.
 */
public final class OreVariantRegistrar {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<Identifier, RegisteredOreVariant> REGISTERED = new LinkedHashMap<>();

    private OreVariantRegistrar() {
    }

    public static synchronized void registerAvailable() {
        for (OreVariant variant : OreVariants.all()) {
            tryRegister(variant);
        }
        LOGGER.info("Registered {} ore variants", REGISTERED.size());
    }

    /**
     * The registered variants, in the order of the definitions.
     */
    public static Collection<RegisteredOreVariant> registered() {
        return Collections.unmodifiableCollection(REGISTERED.values());
    }

    public static Optional<RegisteredOreVariant> get(Identifier id) {
        return Optional.ofNullable(REGISTERED.get(id));
    }

    private static void tryRegister(OreVariant variant) {
        if (REGISTERED.containsKey(variant.id())) {
            return;
        }
        Optional<Block> stone = BuiltInRegistries.BLOCK.getOptional(variant.stoneType().block());
        Optional<Block> ore = BuiltInRegistries.BLOCK.getOptional(variant.oreType().block());
        Optional<Block> host = BuiltInRegistries.BLOCK.getOptional(variant.oreType().host());
        if (stone.isEmpty() || ore.isEmpty()) {
            return;
        }
        if (BuiltInRegistries.BLOCK.containsKey(variant.id())) {
            LOGGER.warn("Ore variant {} is already registered by something else", variant.id());
            return;
        }
        Optional<Identifier> existing = existingVariant(variant);
        if (existing.isPresent()) {
            LOGGER.debug("Not registering ore variant {}: {} already adds it", variant.id(), existing.get());
            return;
        }

        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, variant.id());
        Block block = createBlock(variant, stone.get(), ore.get(), host.orElse(Blocks.STONE), blockKey);
        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
        REGISTERED.put(variant.id(), new RegisteredOreVariant(variant, block, stone.get(), ore.get()));

        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, variant.id());
        Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));

    }

    /**
     * Mods often add some variants of their own ores, or of vanilla ores in their own stones, named
     * {@code <stone>_<ore>} (like Nature's Spirit's {@code chert_coal_ore} or Mythic Metals' {@code calcite_kyber_ore}).
     * Those aren't added a second time.
     */
    private static Optional<Identifier> existingVariant(OreVariant variant) {
        String path = variant.stoneType().block().getPath() + "_" + variant.oreType().block().getPath();
        for (String namespace : List.of(variant.stoneType().block().getNamespace(), variant.oreType().block().getNamespace())) {
            Identifier id = Identifier.tryBuild(namespace, path);
            if (id != null && !namespace.equals(Identifier.DEFAULT_NAMESPACE) && BuiltInRegistries.BLOCK.containsKey(id)) {
                return Optional.of(id);
            }
        }
        return Optional.empty();
    }

    private static Block createBlock(OreVariant variant, Block stone, Block ore, Block host, ResourceKey<Block> key) {
        // The ore's hardness, light, random ticks and tool requirements, with the stone's look
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofLegacyCopy(ore)
                .mapColor(stone.defaultMapColor())
                .instrument(stone.defaultBlockState().instrument())
                // Ores that sound different from their own stone (like nether gold ore) keep their sound
                .sound(ore.defaultBlockState().getSoundType() != host.defaultBlockState().getSoundType()
                        ? ore.defaultBlockState().getSoundType()
                        : stone.defaultBlockState().getSoundType())
                .setId(key);
        variant.stoneType().strength().ifPresent(properties::strength);

        boolean pillar = stone.defaultBlockState().hasProperty(BlockStateProperties.AXIS);
        if (ore instanceof RedStoneOreBlock) {
            return new RedStoneOreBlock(properties);
        }
        if (ore instanceof DropExperienceBlock experienceOre) {
            IntProvider experience = ((DropExperienceBlockAccessor) experienceOre).getXpRange();
            return pillar ? new DropExperienceRotatedPillarBlock(experience, properties) : new DropExperienceBlock(experience, properties);
        }
        return pillar ? new RotatedPillarBlock(properties) : new Block(properties);
    }
}
