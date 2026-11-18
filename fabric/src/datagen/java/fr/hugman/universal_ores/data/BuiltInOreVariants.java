package fr.hugman.universal_ores.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.repository.ServerPacksSource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

/**
 * The ore variants that always exist: those of vanilla ores in vanilla stones. Their data is generated here, and
 * shipped as regular files. The variants of other mods only exist when those mods are installed, so their data is
 * made at load time instead (see {@code OreVariantData} and {@code OreVariantAssets}).
 */
public final class BuiltInOreVariants {
    private BuiltInOreVariants() {
    }

    public static List<RegisteredOreVariant> all() {
        return OreVariantRegistrar.registered().stream()
                .filter(variant -> isVanilla(variant.variant().stoneType().block()) && isVanilla(variant.variant().oreType().block()))
                .toList();
    }

    private static boolean isVanilla(Identifier id) {
        return id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE);
    }

    /**
     * Reads the JSON files of vanilla's data pack in a directory, like {@code tags/block} or {@code recipe}.
     *
     * @param visitor gets each file's ID inside that directory, without extension
     */
    public static void forEachVanillaFile(String directory, BiConsumer<Identifier, JsonElement> visitor) {
        Map<Identifier, JsonElement> files = new TreeMap<>();
        try (VanillaPackResources vanilla = ServerPacksSource.createVanillaPackSource()) {
            vanilla.listResources(PackType.SERVER_DATA, Identifier.DEFAULT_NAMESPACE, directory, (location, supplier) -> {
                String path = location.getPath();
                if (!path.endsWith(".json")) {
                    return;
                }
                try (Reader reader = new InputStreamReader(supplier.get(), StandardCharsets.UTF_8)) {
                    files.put(location.withPath(path.substring(directory.length() + 1, path.length() - ".json".length())), JsonParser.parseReader(reader));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
        files.forEach(visitor);
    }
}
