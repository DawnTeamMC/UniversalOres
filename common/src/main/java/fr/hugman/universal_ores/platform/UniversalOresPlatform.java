package fr.hugman.universal_ores.platform;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

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
}
