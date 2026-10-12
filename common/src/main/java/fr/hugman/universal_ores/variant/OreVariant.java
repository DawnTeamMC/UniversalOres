package fr.hugman.universal_ores.variant;

import fr.hugman.universal_ores.UniversalOres;
import net.minecraft.resources.Identifier;

/**
 * An ore in another stone, e.g. coal ore in granite.
 *
 * @param id          the ID of the variant's block and item
 * @param stoneTypeId the ID of the stone type definition
 * @param oreTypeId   the ID of the ore type definition
 */
public record OreVariant(Identifier id, Identifier stoneTypeId, StoneType stoneType, Identifier oreTypeId, OreType oreType) {
    /**
     * Variants of vanilla ores in vanilla stones keep the IDs Universal Ores always gave them
     * ({@code universal_ores:granite_coal_ore}). Any other variant names both blocks with their namespaces, so that
     * two mods can't clash: {@code universal_ores:promenade/blunite/minecraft/coal_ore}.
     */
    public static Identifier idOf(StoneType stone, OreType ore) {
        Identifier stoneBlock = stone.block();
        Identifier oreBlock = ore.block();
        if (stoneBlock.getNamespace().equals(Identifier.DEFAULT_NAMESPACE) && oreBlock.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
            return UniversalOres.id(stoneBlock.getPath() + "_" + ore.shortNameOrDefault() + "_ore");
        }
        return UniversalOres.id(stoneBlock.getNamespace() + "/" + stoneBlock.getPath() + "/" + oreBlock.getNamespace() + "/" + oreBlock.getPath());
    }
}
