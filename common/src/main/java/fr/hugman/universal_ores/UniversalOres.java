package fr.hugman.universal_ores;

import com.google.common.reflect.Reflection;
import fr.hugman.universal_ores.block.UniversalOresBlockTypes;
import fr.hugman.universal_ores.block.UniversalOresBlocks;
import fr.hugman.universal_ores.item.creative_tab.UniversalOresCreativeModeTabAdditions;
import net.minecraft.resources.Identifier;

public class UniversalOres {
    public static final String MOD_ID = "universal_ores";

    private static boolean initialized = false;

    public static void init() {
        Reflection.initialize(UniversalOresBlockTypes.class);
        Reflection.initialize(UniversalOresBlocks.class);
        UniversalOresCreativeModeTabAdditions.appendItemGroups();
        initialized = true;
    }

    /**
     * Vanilla can build ore configurations before the mod is initialized (e.g. NeoForge validates the built-in
     * registries at startup in development). Mixins must not touch the mod's blocks until then, as that would
     * register them while registries are frozen.
     */
    public static boolean isInitialized() {
        return initialized;
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}