package fr.hugman.universal_ores.variant;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import fr.hugman.universal_ores.block.DropExperienceRotatedPillarBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
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
 * Mods register their blocks in no particular order, so each variant is registered as soon as both of its blocks
 * are: the loaders call {@link #registerAvailable()} once every mod they know of has registered its blocks, and
 * {@link #onBlockRegistered(Identifier)} for blocks registered later on.
 * <p>
 * A variant behaves like its original ore (hardness, drops, experience, light, random ticks) and looks and sounds
 * like its stone. Its ID only depends on the two blocks, never on the loading order.
 */
public final class OreVariantRegistrar {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<Identifier, RegisteredOreVariant> REGISTERED = new LinkedHashMap<>();
    private static final Map<Identifier, List<OreVariant>> WAITING = new HashMap<>();
    private static boolean waitingFilled = false;

    private OreVariantRegistrar() {
    }

    public static synchronized void registerAvailable() {
        fillWaiting();
        for (OreVariant variant : OreVariants.all()) {
            tryRegister(variant);
        }
    }

    public static synchronized void onBlockRegistered(Identifier id) {
        if (!waitingFilled) {
            return;
        }
        List<OreVariant> waiting = WAITING.get(id);
        if (waiting != null) {
            for (OreVariant variant : List.copyOf(waiting)) {
                tryRegister(variant);
            }
        }
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

    private static void fillWaiting() {
        if (waitingFilled) {
            return;
        }
        waitingFilled = true;
        for (OreVariant variant : OreVariants.all()) {
            WAITING.computeIfAbsent(variant.stoneType().block(), id -> new ArrayList<>()).add(variant);
            WAITING.computeIfAbsent(variant.oreType().block(), id -> new ArrayList<>()).add(variant);
        }
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

        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, variant.id());
        Block block = createBlock(variant, stone.get(), ore.get(), host.orElse(Blocks.STONE), blockKey);
        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
        REGISTERED.put(variant.id(), new RegisteredOreVariant(variant, block, stone.get(), ore.get()));

        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, variant.id());
        Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));

        WAITING.values().forEach(list -> list.remove(variant));
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
            IntProvider experience = experienceOf(experienceOre);
            return pillar ? new DropExperienceRotatedPillarBlock(experience, properties) : new DropExperienceBlock(experience, properties);
        }
        return pillar ? new RotatedPillarBlock(properties) : new Block(properties);
    }

    /**
     * Read through the block's codec, which is the only public way to get it.
     */
    private static IntProvider experienceOf(DropExperienceBlock block) {
        return DropExperienceBlock.CODEC.codec().encodeStart(JsonOps.INSTANCE, block)
                .map(json -> json.getAsJsonObject().get("experience"))
                .flatMap(json -> IntProviders.codec(0, 10).parse(JsonOps.INSTANCE, json))
                .resultOrPartial(error -> LOGGER.warn("Couldn't read the experience dropped by {}: {}", block, error))
                .orElse(ConstantInt.ZERO);
    }
}
