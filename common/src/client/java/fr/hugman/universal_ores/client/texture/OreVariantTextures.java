package fr.hugman.universal_ores.client.texture;

import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import fr.hugman.universal_ores.variant.StoneType;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

/**
 * Which textures an ore variant has, and which stone texture each of them is drawn on.
 */
public final class OreVariantTextures {
    private OreVariantTextures() {
    }

    /**
     * @param sprite the variant's texture
     * @param stone  the stone texture it is drawn on
     */
    public record Face(Identifier sprite, Identifier stone) {
    }

    /**
     * Pillar stones (like basalt) have a side face and an end face ({@code _side} and {@code _top}), other stones a
     * single face. Unless the stone type names its textures, they are {@code <namespace>:block/<path>}, or
     * {@code <path>_side} and {@code <path>_top} for pillars.
     */
    public static List<Face> faces(RegisteredOreVariant registered) {
        StoneType stone = registered.variant().stoneType();
        Identifier sprite = registered.variant().id().withPrefix("block/");
        Identifier stoneTexture = stone.block().withPrefix("block/");
        var textures = stone.textures();
        if (textures.isPresent() && textures.get().all().isPresent()) {
            return List.of(new Face(sprite, textures.get().all().get()));
        }
        if (textures.isPresent() && textures.get().side().isPresent() && textures.get().end().isPresent()) {
            return pillar(sprite, textures.get().side().get(), textures.get().end().get());
        }
        if (isPillar(registered)) {
            return pillar(sprite, stoneTexture.withSuffix("_side"), stoneTexture.withSuffix("_top"));
        }
        return List.of(new Face(sprite, stoneTexture));
    }

    /**
     * Whether the stone is a pillar: its variants have its side and end textures, even those that can't be turned like
     * it (like redstone ore variants, which light up instead).
     */
    public static boolean isPillar(RegisteredOreVariant registered) {
        return registered.stone().defaultBlockState().hasProperty(BlockStateProperties.AXIS)
                || registered.block().defaultBlockState().hasProperty(BlockStateProperties.AXIS);
    }

    private static List<Face> pillar(Identifier sprite, Identifier side, Identifier end) {
        return List.of(new Face(sprite.withSuffix("_side"), side), new Face(sprite.withSuffix("_top"), end));
    }

    public static boolean exists(ResourceManager resourceManager, Identifier texture) {
        return resourceManager.getResource(SpriteSource.TEXTURE_ID_CONVERTER.idToFile(texture)).isPresent();
    }
}
