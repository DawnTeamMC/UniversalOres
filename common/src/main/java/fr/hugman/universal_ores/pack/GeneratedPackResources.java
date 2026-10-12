package fr.hugman.universal_ores.pack;

import com.google.common.base.Suppliers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A pack whose files are written in memory by generators, the first time one of them is asked for.
 */
public final class GeneratedPackResources implements PackResources {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final PackLocationInfo location;
    private final PackType type;
    private final Supplier<Map<Identifier, byte[]>> files;

    GeneratedPackResources(PackLocationInfo location, PackType type, List<Consumer<Output>> generators) {
        this.location = location;
        this.type = type;
        this.files = Suppliers.memoize(() -> {
            Map<Identifier, byte[]> files = new TreeMap<>();
            Output output = (path, json) -> files.put(path, GSON.toJson(json).getBytes(StandardCharsets.UTF_8));
            for (Consumer<Output> generator : generators) {
                try {
                    generator.accept(output);
                } catch (RuntimeException e) {
                    LOGGER.error("Couldn't generate the files of {}", location.id(), e);
                }
            }
            return files;
        });
    }

    /**
     * Where generators write their files.
     */
    @FunctionalInterface
    public interface Output {
        /**
         * @param path the file's location inside its namespace, with its extension (e.g. {@code mod:models/block/x.json})
         */
        void accept(Identifier path, JsonElement json);
    }

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String @NotNull ... path) {
        return null;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(@NotNull PackType type, @NotNull Identifier location) {
        if (type != this.type) {
            return null;
        }
        byte[] bytes = this.files.get().get(location);
        return bytes == null ? null : () -> new ByteArrayInputStream(bytes);
    }

    @Override
    public void listResources(@NotNull PackType type, @NotNull String namespace, @NotNull String directory, @NotNull ResourceOutput output) {
        if (type != this.type) {
            return;
        }
        String prefix = directory + "/";
        this.files.get().forEach((path, bytes) -> {
            if (path.getNamespace().equals(namespace) && path.getPath().startsWith(prefix)) {
                output.accept(path, () -> new ByteArrayInputStream(bytes));
            }
        });
    }

    @Override
    public @NotNull Set<String> getNamespaces(@NotNull PackType type) {
        if (type != this.type) {
            return Set.of();
        }
        Set<String> namespaces = new HashSet<>();
        this.files.get().keySet().forEach(path -> namespaces.add(path.getNamespace()));
        return namespaces;
    }

    @Override
    public <T> @Nullable T getMetadataSection(@NotNull MetadataSectionType<T> type) {
        return null;
    }

    @Override
    public @NotNull PackLocationInfo location() {
        return this.location;
    }

    @Override
    public void close() {
    }
}
