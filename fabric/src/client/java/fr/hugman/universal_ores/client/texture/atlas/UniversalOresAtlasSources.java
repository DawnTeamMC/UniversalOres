package fr.hugman.universal_ores.client.texture.atlas;

import com.mojang.serialization.MapCodec;
import fr.hugman.universal_ores.UniversalOres;
import net.fabricmc.fabric.api.client.rendering.v1.AtlasSourceTypeRegistry;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.AtlasSourceType;

public class UniversalOresAtlasSources {
    public static final AtlasSourceType ORE_OVERLAY = of("ore_overlay", OreOverlayAtlasSource.CODEC);

    public static AtlasSourceType of(String id, MapCodec<? extends AtlasSource> codec) {
        var atlasSourceType = new AtlasSourceType(codec);
        AtlasSourceTypeRegistry.register(UniversalOres.id(id), atlasSourceType);
        return atlasSourceType;
    }
}
