package fr.hugman.universal_ores;

import com.google.common.reflect.Reflection;
import fr.hugman.universal_ores.block.UniversalOresBlockTypes;
import fr.hugman.universal_ores.block.UniversalOresBlocks;
import fr.hugman.universal_ores.item.creative_tab.UniversalOresCreativeModeTabAdditions;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class UniversalOres implements ModInitializer {
    public static final String MOD_ID = "universal_ores";

    @Override
    public void onInitialize() {
        Reflection.initialize(UniversalOresBlockTypes.class);
        Reflection.initialize(UniversalOresBlocks.class);
        UniversalOresCreativeModeTabAdditions.appendItemGroups();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}