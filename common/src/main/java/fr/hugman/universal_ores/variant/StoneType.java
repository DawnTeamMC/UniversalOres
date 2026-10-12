package fr.hugman.universal_ores.variant;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

/**
 * A stone that ores can be found in. In a definition file, under {@code "stones"}, keyed by the stone block:
 * <pre>{@code
 * "minecraft:granite": "minecraft:stone",
 * "minecraft:calcite": { "ores_of": "minecraft:stone", "strength": 0.5 }
 * }</pre>
 *
 * @param block    the stone block
 * @param oresOf   the stones whose ores it gets: an ore found in one of them gets a variant in this stone
 * @param only     if present, only these ores (by ore block)
 * @param except   these ores (by ore block) don't get a variant in this stone
 * @param strength if present, the hardness and blast resistance of its ores (otherwise those of the original ores)
 * @param textures if present, the textures to draw the ores on (otherwise {@code <namespace>:block/<path>}, or
 *                 {@code <path>_side} and {@code <path>_top} for pillars)
 */
public record StoneType(
        Identifier block,
        List<Identifier> oresOf,
        Optional<List<Identifier>> only,
        List<Identifier> except,
        Optional<Float> strength,
        Optional<StoneTextures> textures
) {
    private static final Codec<List<Identifier>> ONE_OR_MORE = Codec.either(Identifier.CODEC, Identifier.CODEC.listOf())
            .xmap(either -> either.map(List::of, list -> list), list -> list.size() == 1 ? Either.left(list.getFirst()) : Either.right(list));

    /**
     * Either the stones whose ores it gets, or an object.
     */
    public static Codec<StoneType> codec(Identifier block) {
        Codec<StoneType> full = RecordCodecBuilder.create(instance -> instance.group(
                ONE_OR_MORE.fieldOf("ores_of").forGetter(StoneType::oresOf),
                Identifier.CODEC.listOf().optionalFieldOf("only").forGetter(StoneType::only),
                Identifier.CODEC.listOf().optionalFieldOf("except", List.of()).forGetter(StoneType::except),
                Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("strength").forGetter(StoneType::strength),
                StoneTextures.CODEC.optionalFieldOf("texture").forGetter(StoneType::textures)
        ).apply(instance, (oresOf, only, except, strength, textures) -> new StoneType(block, oresOf, only, except, strength, textures)));
        return Codec.either(ONE_OR_MORE, full).xmap(
                either -> either.map(oresOf -> new StoneType(block, oresOf, Optional.empty(), List.of(), Optional.empty(), Optional.empty()), stone -> stone),
                Either::right
        );
    }

    public boolean accepts(OreType ore) {
        return this.oresOf.contains(ore.host())
                && !ore.host().equals(this.block)
                && !ore.block().equals(this.block)
                && this.only.map(only -> only.contains(ore.block())).orElse(true)
                && !this.except.contains(ore.block());
    }

    /**
     * Either one texture for every face, or {@code {"side": ..., "end": ...}} for pillars.
     */
    public record StoneTextures(Optional<Identifier> all, Optional<Identifier> side, Optional<Identifier> end) {
        private static final Codec<StoneTextures> PILLAR = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("side").forGetter(textures -> textures.side().orElseThrow()),
                Identifier.CODEC.fieldOf("end").forGetter(textures -> textures.end().orElseThrow())
        ).apply(instance, (side, end) -> new StoneTextures(Optional.empty(), Optional.of(side), Optional.of(end))));
        public static final Codec<StoneTextures> CODEC = Codec.either(Identifier.CODEC, PILLAR).xmap(
                either -> either.map(all -> new StoneTextures(Optional.of(all), Optional.empty(), Optional.empty()), pillar -> pillar),
                textures -> textures.all().<Either<Identifier, StoneTextures>>map(Either::left).orElseGet(() -> Either.right(textures))
        );
    }
}
