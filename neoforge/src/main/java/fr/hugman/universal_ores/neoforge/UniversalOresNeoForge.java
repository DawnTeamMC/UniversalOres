package fr.hugman.universal_ores.neoforge;

import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.platform.UniversalOresPlatform;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(UniversalOres.MOD_ID)
public class UniversalOresNeoForge {
    private static boolean registered = false;

    public UniversalOresNeoForge(IEventBus modBus) {
        ((NeoForgeUniversalOresPlatform) UniversalOresPlatform.INSTANCE).subscribe(modBus);

        modBus.addListener(RegisterEvent.class, UniversalOresNeoForge::register);
    }

    /**
     * NeoForge keeps every registry open for the whole registration phase, so all of Universal Ores' content is
     * registered at once, as soon as the first registry is ready, just like on Fabric.
     */
    private static void register(RegisterEvent event) {
        if (registered) {
            return;
        }
        registered = true;

        UniversalOres.init();
    }
}
