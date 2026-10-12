package fr.hugman.universal_ores.client.texture;

import fr.hugman.universal_ores.variant.OreVariant;
import fr.hugman.universal_ores.variant.StoneType;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.List;

/**
 * Which textures an ore variant has, and which stone texture each of them is drawn on.
 */
public final class OreVariantTextures {
    private OreVariantTextures() {
    }

    /**
     * @param suffix  added to the variant's texture name: empty, {@code _side} or {@code _top}
     * @param sprite  the variant's texture
     * @param stone   the stone texture it is drawn on
     */
    public record Face(String suffix, Identifier sprite, Identifier stone) {
    }

    /**
     * Cube stones have a single face, pillar stones (like basalt) have a side face and a top face.
     * Stone types may name their textures; otherwise {@code <namespace>:block/<path>} is used when it exists, or else
     * {@code <path>_side} and {@code <path>_top}.
     */
    public static List<Face> faces(ResourceManager resourceManager, OreVariant variant) {
        StoneType stone = variant.stoneType();
        Identifier sprite = variant.id().withPrefix("block/");
        var textures = stone.textures();
        if (textures.isPresent() && textures.get().all().isPresent()) {
            return List.of(new Face("", sprite, textures.get().all().get()));
        }
        if (textures.isPresent() && textures.get().side().isPresent() && textures.get().end().isPresent()) {
            return pillar(sprite, textures.get().side().get(), textures.get().end().get());
        }

        Identifier all = stone.block().withPrefix("block/");
        if (exists(resourceManager, all)) {
            return List.of(new Face("", sprite, all));
        }
        return pillar(sprite, all.withSuffix("_side"), all.withSuffix("_top"));
    }

    private static List<Face> pillar(Identifier sprite, Identifier side, Identifier end) {
        return List.of(new Face("_side", sprite.withSuffix("_side"), side), new Face("_top", sprite.withSuffix("_top"), end));
    }

    public static boolean exists(ResourceManager resourceManager, Identifier texture) {
        return resourceManager.getResource(SpriteSource.TEXTURE_ID_CONVERTER.idToFile(texture)).isPresent();
    }
}
