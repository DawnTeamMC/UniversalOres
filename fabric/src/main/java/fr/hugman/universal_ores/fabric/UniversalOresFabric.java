package fr.hugman.universal_ores.fabric;

import fr.hugman.universal_ores.UniversalOres;
import net.fabricmc.api.ModInitializer;

public class UniversalOresFabric implements ModInitializer {
    /**
     * The ore variants are registered later, once every mod has registered its blocks: see {@code BuiltInRegistriesMixin}.
     */
    @Override
    public void onInitialize() {
        UniversalOres.init();
    }
}
