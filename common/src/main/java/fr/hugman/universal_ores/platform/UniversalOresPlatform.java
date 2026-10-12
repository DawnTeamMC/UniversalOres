package fr.hugman.universal_ores.platform;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.ServiceLoader;
import java.util.function.Consumer;

/**
 * Everything the shared code needs from the mod loader.
 * <p>
 * Each loader project provides one implementation, declared in {@code META-INF/services}.
 */
public interface UniversalOresPlatform {
    UniversalOresPlatform INSTANCE = ServiceLoader.load(UniversalOresPlatform.class, UniversalOresPlatform.class.getClassLoader())
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No Universal Ores platform implementation found"));

    void modifyCreativeModeTab(ResourceKey<CreativeModeTab> tab, Consumer<CreativeModeTabOutput> modifier);

    Path configDir();

    /**
     * Visits the JSON files found in every mod (including Minecraft) under {@code data/<namespace>/<directory>/},
     * before data packs are loaded. The visitor gets {@code <namespace>:<path inside the directory, without .json>}.
     */
    void forEachModDataFile(String directory, DataFileVisitor visitor);

    @FunctionalInterface
    interface DataFileVisitor {
        /**
         * @param stream closed by the visitor
         */
        void visit(Identifier id, InputStream stream);
    }
}
