package fr.hugman.universal_ores.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * An ore that gets a variant in every stone type of its category, read from
 * {@code data/<namespace>/universal_ores/ore_type/<path>.json}.
 *
 * @param block       the original ore block, whose behavior, drops, tags and recipes the variants copy
 * @param host        the stone the original ore sits in, used to tell the ore apart from the stone in its texture
 * @param category    which stone types get this ore (e.g. {@code overworld}, {@code nether})
 * @param shortName   the name used in the IDs of vanilla stone variants ({@code granite_<short_name>_ore}),
 *                    by default the block's path without its {@code _ore} suffix
 * @param texture     the texture of the original ore, by default {@code <namespace>:block/<path>}
 * @param hostTexture the texture of the host stone, by default {@code <namespace>:block/<path>} of the host
 */
public record OreType(
        Identifier block,
        Identifier host,
        String category,
        Optional<String> shortName,
        Optional<Identifier> texture,
        Optional<Identifier> hostTexture
) {
    public static final Codec<OreType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("block").forGetter(OreType::block),
            Identifier.CODEC.fieldOf("host").forGetter(OreType::host),
            Codec.STRING.fieldOf("category").forGetter(OreType::category),
            Codec.STRING.optionalFieldOf("short_name").forGetter(OreType::shortName),
            Identifier.CODEC.optionalFieldOf("texture").forGetter(OreType::texture),
            Identifier.CODEC.optionalFieldOf("host_texture").forGetter(OreType::hostTexture)
    ).apply(instance, OreType::new));

    public String shortNameOrDefault() {
        String path = this.block.getPath();
        return this.shortName.orElse(path.endsWith("_ore") ? path.substring(0, path.length() - "_ore".length()) : path);
    }

    public Identifier textureOrDefault() {
        return this.texture.orElse(this.block.withPrefix("block/"));
    }

    public Identifier hostTextureOrDefault() {
        return this.hostTexture.orElse(this.host.withPrefix("block/"));
    }
}
