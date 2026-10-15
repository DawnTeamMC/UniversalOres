package fr.hugman.universal_ores.platform;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.ServiceLoader;
import java.util.function.BiConsumer;
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
     * Visits the JSON files found in the {@code <directory>/} folder of every mod, before data packs are loaded.
     *
     * @param visitor gets where the file comes from (for error messages) and its content, which it closes
     */
    void forEachModFile(String directory, BiConsumer<String, InputStream> visitor);
}
