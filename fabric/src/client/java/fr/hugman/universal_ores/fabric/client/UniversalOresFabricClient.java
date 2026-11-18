package fr.hugman.universal_ores.fabric.client;

import fr.hugman.universal_ores.client.UniversalOresClient;
import fr.hugman.universal_ores.client.texture.atlas.UniversalOresSpriteSources;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.SpriteSourceRegistry;

public class UniversalOresFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UniversalOresClient.init();
        UniversalOresSpriteSources.register(SpriteSourceRegistry::register);
    }
}
