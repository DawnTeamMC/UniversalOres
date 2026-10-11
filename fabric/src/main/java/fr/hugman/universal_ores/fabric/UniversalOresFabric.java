package fr.hugman.universal_ores.fabric;

import fr.hugman.universal_ores.UniversalOres;
import net.fabricmc.api.ModInitializer;

public class UniversalOresFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        UniversalOres.init();
    }
}
