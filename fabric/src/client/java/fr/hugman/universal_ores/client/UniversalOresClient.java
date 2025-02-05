package fr.hugman.universal_ores.client;

import com.google.common.reflect.Reflection;
import fr.hugman.universal_ores.client.texture.atlas.UniversalOresAtlasSources;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class UniversalOresClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Reflection.initialize(UniversalOresAtlasSources.class);
    }
}
