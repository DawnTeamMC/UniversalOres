package fr.hugman.universal_ores.client.texture.atlas;

import com.mojang.serialization.MapCodec;
import fr.hugman.universal_ores.UniversalOres;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

public class UniversalOresSpriteSources {
    public static void register(BiConsumer<Identifier, MapCodec<? extends SpriteSource>> registrar) {
        registrar.accept(UniversalOres.id("ore_overlay"), OreOverlaySpriteSource.MAP_CODEC);
    }
}
