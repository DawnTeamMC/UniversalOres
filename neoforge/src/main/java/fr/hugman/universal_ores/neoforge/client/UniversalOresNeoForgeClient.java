package fr.hugman.universal_ores.neoforge.client;

import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.client.texture.atlas.UniversalOresSpriteSources;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterSpriteSourcesEvent;

@Mod(value = UniversalOres.MOD_ID, dist = Dist.CLIENT)
public class UniversalOresNeoForgeClient {
    public UniversalOresNeoForgeClient(IEventBus modBus) {
        modBus.addListener(RegisterSpriteSourcesEvent.class, event -> UniversalOresSpriteSources.register(event::register));
    }
}
