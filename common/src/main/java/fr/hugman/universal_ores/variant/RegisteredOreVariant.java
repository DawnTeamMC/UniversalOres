package fr.hugman.universal_ores.variant;

import net.minecraft.world.level.block.Block;

/**
 * An ore variant whose block exists in this game.
 *
 * @param block the variant's block
 * @param stone the block of its stone
 * @param ore   the block of its original ore
 */
public record RegisteredOreVariant(OreVariant variant, Block block, Block stone, Block ore) {
}
