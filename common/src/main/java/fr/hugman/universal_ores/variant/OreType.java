package fr.hugman.universal_ores.variant;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * An ore that gets a variant in other stones. In a definition file, under {@code "ores"}, keyed by the ore block:
 * <pre>{@code
 * "minecraft:coal_ore": "minecraft:stone",
 * "mod:ore": { "in": "minecraft:stone", "texture": "mod:block/ore_side" }
 * }</pre>
 *
 * @param block       the original ore block, whose behavior, drops, tags and recipes the variants copy
 * @param host        the stone the original ore is found in: every stone getting the ores of that stone gets a variant
 * @param name        the name used in the IDs of vanilla stone variants ({@code granite_<name>_ore}), by default the
 *                    block's path without its {@code _ore} suffix; only useful for vanilla ores
 * @param texture     the texture of the original ore, by default {@code <namespace>:block/<path>}
 * @param hostTexture the texture of the stone the ore texture is drawn on, by default {@code <namespace>:block/<path>}
 *                    of the host
 */
public record OreType(
        Identifier block,
        Identifier host,
        Optional<String> name,
        Optional<Identifier> texture,
        Optional<Identifier> hostTexture
) {
    /**
     * Either the stone the ore is found in, or an object.
     */
    public static Codec<OreType> codec(Identifier block) {
        Codec<OreType> full = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("in").forGetter(OreType::host),
                Codec.STRING.optionalFieldOf("name").forGetter(OreType::name),
                Identifier.CODEC.optionalFieldOf("texture").forGetter(OreType::texture),
                Identifier.CODEC.optionalFieldOf("host_texture").forGetter(OreType::hostTexture)
        ).apply(instance, (host, name, texture, hostTexture) -> new OreType(block, host, name, texture, hostTexture)));
        return Codec.either(Identifier.CODEC, full).xmap(
                either -> either.map(host -> new OreType(block, host, Optional.empty(), Optional.empty(), Optional.empty()), ore -> ore),
                Either::right
        );
    }

    public String nameOrDefault() {
        String path = this.block.getPath();
        return this.name.orElse(path.endsWith("_ore") ? path.substring(0, path.length() - "_ore".length()) : path);
    }

    public Identifier textureOrDefault() {
        return this.texture.orElse(this.block.withPrefix("block/"));
    }

    public Identifier hostTextureOrDefault() {
        return this.hostTexture.orElse(this.host.withPrefix("block/"));
    }
}
