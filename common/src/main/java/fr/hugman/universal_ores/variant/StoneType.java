package fr.hugman.universal_ores.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

/**
 * A stone that ores can be found in, read from {@code data/<namespace>/universal_ores/stone_type/<path>.json}.
 *
 * @param block    the stone block
 * @param category which ores it gets: those of the same category (e.g. {@code overworld}, {@code nether})
 * @param ores     if present, only these ore types (instead of every ore type of the category)
 * @param strength if present, the hardness and blast resistance of its ores (otherwise those of the original ores)
 * @param textures if present, the textures to draw the ores on (otherwise {@code <namespace>:block/<path>} when it
 *                 exists, or {@code <path>_side} and {@code <path>_top})
 */
public record StoneType(
        Identifier block,
        String category,
        Optional<List<Identifier>> ores,
        Optional<Float> strength,
        Optional<StoneTextures> textures
) {
    public static final Codec<StoneType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("block").forGetter(StoneType::block),
            Codec.STRING.fieldOf("category").forGetter(StoneType::category),
            Identifier.CODEC.listOf().optionalFieldOf("ores").forGetter(StoneType::ores),
            Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("strength").forGetter(StoneType::strength),
            StoneTextures.CODEC.optionalFieldOf("textures").forGetter(StoneType::textures)
    ).apply(instance, StoneType::new));

    /**
     * Either one texture for every face ({@code all}), or a side texture and an end texture for pillars.
     */
    public record StoneTextures(Optional<Identifier> all, Optional<Identifier> side, Optional<Identifier> end) {
        public static final Codec<StoneTextures> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.optionalFieldOf("all").forGetter(StoneTextures::all),
                Identifier.CODEC.optionalFieldOf("side").forGetter(StoneTextures::side),
                Identifier.CODEC.optionalFieldOf("end").forGetter(StoneTextures::end)
        ).apply(instance, StoneTextures::new));
    }
}
