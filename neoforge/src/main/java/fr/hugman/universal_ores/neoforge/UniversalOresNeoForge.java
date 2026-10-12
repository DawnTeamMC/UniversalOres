package fr.hugman.universal_ores.neoforge;

import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.pack.GeneratedPacks;
import fr.hugman.universal_ores.platform.UniversalOresPlatform;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(UniversalOres.MOD_ID)
public class UniversalOresNeoForge {
    private static boolean initialized = false;

    public UniversalOresNeoForge(IEventBus modBus) {
        ((NeoForgeUniversalOresPlatform) UniversalOresPlatform.INSTANCE).subscribe(modBus);

        modBus.addListener(RegisterEvent.class, UniversalOresNeoForge::init);
        modBus.addListener(EventPriority.LOWEST, RegisterEvent.class, UniversalOresNeoForge::registerVariants);
        modBus.addListener(AddPackFindersEvent.class, event -> event.addRepositorySource(GeneratedPacks.source(event.getPackType())));
    }

    /**
     * NeoForge keeps every registry open for the whole registration phase, so Universal Ores initializes as soon as
     * the first registry is ready, just like on Fabric.
     */
    private static void init(RegisterEvent event) {
        if (initialized) {
            return;
        }
        initialized = true;

        UniversalOres.init();
    }

    /**
     * NeoForge sends each event priority to every mod before the next one, so at the lowest priority of the block
     * registration, every mod has registered its blocks.
     */
    private static void registerVariants(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.BLOCK)) {
            OreVariantRegistrar.registerAvailable();
        }
    }
}
