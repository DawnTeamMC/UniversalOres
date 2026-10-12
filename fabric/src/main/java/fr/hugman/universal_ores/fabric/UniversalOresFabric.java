package fr.hugman.universal_ores.fabric;

import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.registries.BuiltInRegistries;

public class UniversalOresFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        UniversalOres.init();
        // Mods initialize in no particular order and registries stay open until all of them are done:
        // variants are registered now for the blocks that exist, and as soon as their other blocks show up
        OreVariantRegistrar.registerAvailable();
        RegistryEntryAddedCallback.event(BuiltInRegistries.BLOCK).register((rawId, id, block) -> OreVariantRegistrar.onBlockRegistered(id));
    }
}
