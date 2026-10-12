package fr.hugman.universal_ores.variant;

import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.platform.UniversalOresPlatform;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * The stone types and ore types, and the ore variants they make.
 * <p>
 * Definitions are read once at startup, before blocks are registered, from every mod
 * ({@code data/<namespace>/universal_ores/<stone_type|ore_type>/<path>.json}) and then from the
 * {@code config/universal_ores/<namespace>/<stone_type|ore_type>/<path>.json} files, which override them.
 * They can't come from data packs, which are only loaded once the blocks are already registered.
 */
public final class OreVariants {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String STONE_TYPE_DIRECTORY = UniversalOres.MOD_ID + "/stone_type";
    public static final String ORE_TYPE_DIRECTORY = UniversalOres.MOD_ID + "/ore_type";

    private static final Map<Identifier, StoneType> STONE_TYPES = new LinkedHashMap<>();
    private static final Map<Identifier, OreType> ORE_TYPES = new LinkedHashMap<>();
    private static final Map<Identifier, OreVariant> VARIANTS = new LinkedHashMap<>();
    private static boolean loaded = false;

    private OreVariants() {
    }

    public static synchronized void load() {
        if (loaded) {
            return;
        }
        loaded = true;

        UniversalOresPlatform.INSTANCE.forEachModDataFile(STONE_TYPE_DIRECTORY, (id, stream) -> read(id, stream, StoneType.CODEC, STONE_TYPES));
        UniversalOresPlatform.INSTANCE.forEachModDataFile(ORE_TYPE_DIRECTORY, (id, stream) -> read(id, stream, OreType.CODEC, ORE_TYPES));
        Path config = UniversalOresPlatform.INSTANCE.configDir().resolve(UniversalOres.MOD_ID);
        readConfig(config, "stone_type", StoneType.CODEC, STONE_TYPES);
        readConfig(config, "ore_type", OreType.CODEC, ORE_TYPES);

        VARIANTS.putAll(combine(STONE_TYPES, ORE_TYPES));
        LOGGER.info("Loaded {} stone types and {} ore types, making {} possible ore variants", STONE_TYPES.size(), ORE_TYPES.size(), VARIANTS.size());
    }

    /**
     * Pairs every stone type with the ore types of its category, except an ore with its own host stone.
     */
    public static Map<Identifier, OreVariant> combine(Map<Identifier, StoneType> stoneTypes, Map<Identifier, OreType> oreTypes) {
        Map<Identifier, OreVariant> variants = new LinkedHashMap<>();
        stoneTypes.forEach((stoneId, stone) -> oreTypes.forEach((oreId, ore) -> {
            if (!ore.category().equals(stone.category()) || ore.host().equals(stone.block()) || ore.block().equals(stone.block())) {
                return;
            }
            if (stone.ores().isPresent() && !stone.ores().get().contains(oreId)) {
                return;
            }
            var variant = new OreVariant(OreVariant.idOf(stone, ore), stoneId, stone, oreId, ore);
            var previous = variants.putIfAbsent(variant.id(), variant);
            if (previous != null) {
                LOGGER.warn("Ore variant {} is made by both {} + {} and {} + {}; keeping the first", variant.id(), previous.stoneTypeId(), previous.oreTypeId(), stoneId, oreId);
            }
        }));
        return variants;
    }

    /**
     * Every variant the definitions make, whether or not their blocks exist in this game.
     */
    public static Collection<OreVariant> all() {
        return Collections.unmodifiableCollection(VARIANTS.values());
    }

    public static Optional<OreVariant> get(Identifier id) {
        return Optional.ofNullable(VARIANTS.get(id));
    }

    public static Map<Identifier, StoneType> stoneTypes() {
        return Collections.unmodifiableMap(STONE_TYPES);
    }

    public static Map<Identifier, OreType> oreTypes() {
        return Collections.unmodifiableMap(ORE_TYPES);
    }

    private static <T> void read(Identifier id, InputStream stream, Codec<T> codec, Map<Identifier, T> into) {
        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            codec.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader))
                    .ifSuccess(value -> into.put(id, value))
                    .ifError(error -> LOGGER.error("Couldn't read Universal Ores definition {}: {}", id, error.message()));
        } catch (Exception e) {
            LOGGER.error("Couldn't read Universal Ores definition {}", id, e);
        }
    }

    private static <T> void readConfig(Path config, String kind, Codec<T> codec, Map<Identifier, T> into) {
        if (!Files.isDirectory(config)) {
            return;
        }
        try (Stream<Path> namespaces = Files.list(config)) {
            for (Path namespace : namespaces.filter(Files::isDirectory).toList()) {
                Path directory = namespace.resolve(kind);
                if (!Files.isDirectory(directory)) {
                    continue;
                }
                try (Stream<Path> files = Files.walk(directory)) {
                    for (Path file : files.filter(f -> f.toString().endsWith(".json")).toList()) {
                        String path = directory.relativize(file).toString().replace('\\', '/');
                        Identifier id = Identifier.tryBuild(namespace.getFileName().toString(), path.substring(0, path.length() - ".json".length()));
                        if (id == null) {
                            LOGGER.error("Invalid Universal Ores definition path {}", file);
                            continue;
                        }
                        try (InputStream stream = Files.newInputStream(file)) {
                            read(id, stream, codec, into);
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Couldn't read the Universal Ores definitions in {}", config, e);
        }
    }
}
