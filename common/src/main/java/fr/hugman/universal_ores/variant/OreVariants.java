package fr.hugman.universal_ores.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The stones and ores Universal Ores knows of, and the ore variants they make.
 * <p>
 * They are listed in JSON files, read once at startup (before blocks are registered, so data packs can't add any):
 * first every {@code universal_ores/*.json} file found in a mod (so any mod can add its stones and ores), then every
 * {@code config/universal_ores/*.json} file, in alphabetical order (so modpacks can add, change or remove any).
 * <pre>{@code
 * {
 *   "stones": {
 *     "mod:marble": "minecraft:stone",
 *     "mod:dark_marble": { "ores_of": "minecraft:stone", "except": ["minecraft:coal_ore"] }
 *   },
 *   "ores": {
 *     "mod:tin_ore": "minecraft:stone",
 *     "minecraft:emerald_ore": false
 *   }
 * }
 * }</pre>
 * A stone gets a variant of every ore found in the stones it gets the ores of. {@code false} removes a stone or an
 * ore listed by an earlier file.
 */
public final class OreVariants {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String DIRECTORY = UniversalOres.MOD_ID;

    private static final Definitions DEFINITIONS = new Definitions();
    private static final Map<Identifier, OreVariant> VARIANTS = new LinkedHashMap<>();
    private static boolean loaded = false;

    private OreVariants() {
    }

    public static synchronized void load() {
        if (loaded) {
            return;
        }
        loaded = true;

        UniversalOresPlatform.INSTANCE.forEachModFile(DIRECTORY, DEFINITIONS::read);
        Path config = UniversalOresPlatform.INSTANCE.configDir().resolve(DIRECTORY);
        if (Files.isDirectory(config)) {
            try (Stream<Path> files = Files.walk(config)) {
                for (Path file : files.filter(f -> f.toString().endsWith(".json")).sorted().toList()) {
                    DEFINITIONS.read(file.toString(), Files.newInputStream(file));
                }
            } catch (IOException e) {
                LOGGER.error("Couldn't read the Universal Ores files in {}", config, e);
            }
        }

        VARIANTS.putAll(DEFINITIONS.combine());
        LOGGER.info("Universal Ores knows of {} stones and {} ores, making {} possible ore variants", DEFINITIONS.stones.size(), DEFINITIONS.ores.size(), VARIANTS.size());
    }

    /**
     * Every variant the definitions make, whether or not their blocks exist in this game.
     */
    public static Collection<OreVariant> all() {
        return Collections.unmodifiableCollection(VARIANTS.values());
    }

    /**
     * Stones and ores read from definition files, later files overriding earlier ones.
     */
    public static final class Definitions {
        private final Map<Identifier, StoneType> stones = new LinkedHashMap<>();
        private final Map<Identifier, OreType> ores = new LinkedHashMap<>();

        public Map<Identifier, StoneType> stones() {
            return Collections.unmodifiableMap(this.stones);
        }

        public Map<Identifier, OreType> ores() {
            return Collections.unmodifiableMap(this.ores);
        }

        /**
         * Reads a definition file. A wrong entry is logged and skipped, without dropping the rest of the file.
         *
         * @param source where the file comes from, for error messages
         * @param stream closed once read
         */
        public void read(String source, InputStream stream) {
            try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                readSection(source, root, "stones", this.stones, StoneType::codec);
                readSection(source, root, "ores", this.ores, OreType::codec);
            } catch (Exception e) {
                LOGGER.error("Couldn't read Universal Ores file {}", source, e);
            }
        }

        private static <T> void readSection(String source, JsonObject root, String section, Map<Identifier, T> into, Function<Identifier, Codec<T>> codec) {
            if (!root.has(section)) {
                return;
            }
            for (Map.Entry<String, JsonElement> entry : root.getAsJsonObject(section).entrySet()) {
                Identifier block = Identifier.tryParse(entry.getKey());
                if (block == null) {
                    LOGGER.error("Universal Ores file {}: {} isn't a valid block ID", source, entry.getKey());
                    continue;
                }
                JsonElement value = entry.getValue();
                if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isBoolean()) {
                    if (value.getAsBoolean()) {
                        LOGGER.error("Universal Ores file {}: {} can only be set to false, to remove it", source, block);
                    } else {
                        into.remove(block);
                    }
                    continue;
                }
                codec.apply(block).parse(JsonOps.INSTANCE, value)
                        .ifSuccess(definition -> into.put(block, definition))
                        .ifError(error -> LOGGER.error("Universal Ores file {}: invalid {} {}: {}", source, section, block, error.message()));
            }
        }

        /**
         * Pairs every stone with every ore found in the stones it gets the ores of.
         */
        public Map<Identifier, OreVariant> combine() {
            Map<Identifier, OreVariant> variants = new LinkedHashMap<>();
            for (StoneType stone : this.stones.values()) {
                for (OreType ore : this.ores.values()) {
                    if (stone.accepts(ore)) {
                        OreVariant variant = new OreVariant(stone, ore);
                        OreVariant previous = variants.putIfAbsent(variant.id(), variant);
                        if (previous != null) {
                            LOGGER.warn("Ore variant {} would be made twice; keeping {} in {}", variant.id(), previous.oreType().block(), previous.stoneType().block());
                        }
                    }
                }
            }
            return variants;
        }
    }
}
