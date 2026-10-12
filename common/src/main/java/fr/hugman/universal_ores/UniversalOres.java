package fr.hugman.universal_ores;

import com.google.common.reflect.Reflection;
import fr.hugman.universal_ores.block.UniversalOresBlockTypes;
import fr.hugman.universal_ores.item.creative_tab.UniversalOresCreativeModeTabAdditions;
import fr.hugman.universal_ores.variant.OreVariantData;
import fr.hugman.universal_ores.variant.OreVariants;
import net.minecraft.resources.Identifier;

public class UniversalOres {
    public static final String MOD_ID = "universal_ores";

    private static boolean initialized = false;

    /**
     * Loads the definitions. The ore variants themselves are registered by the loader, through
     * {@link fr.hugman.universal_ores.variant.OreVariantRegistrar}, once the blocks they need exist.
     */
    public static void init() {
        OreVariants.load();
        Reflection.initialize(UniversalOresBlockTypes.class);
        OreVariantData.init();
        UniversalOresCreativeModeTabAdditions.appendItemGroups();
        initialized = true;
    }

    /**
     * Vanilla can build ore configurations before the mod is initialized (e.g. NeoForge validates the built-in
     * registries at startup in development). Mixins must not look for the mod's blocks until then.
     */
    public static boolean isInitialized() {
        return initialized;
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
