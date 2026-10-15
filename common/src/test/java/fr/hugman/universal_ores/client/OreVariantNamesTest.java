package fr.hugman.universal_ores.client;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OreVariantNamesTest {
    @Test
    void usesTheNameOfTheBlock() {
        Map<String, String> translations = Map.of("block.mod.tin_ore", "Tin Ore", "item.mod.tin_ore", "Tin Ore Item");
        assertEquals("Tin Ore", OreVariantNames.nameOf(translations, "block.mod.tin_ore", "item.mod.tin_ore", "tin_ore"));
    }

    @Test
    void usesTheNameOfTheItemWhenTheBlockHasNone() {
        // Like More Ores, which only names the items of its ores
        Map<String, String> translations = Map.of("item.more-ores.plomb_ore", "Lead Ore");
        assertEquals("Lead Ore", OreVariantNames.nameOf(translations, "block.more-ores.plomb_ore", "item.more-ores.plomb_ore", "plomb_ore"));
    }

    @Test
    void usesTheIdWhenNothingIsNamed() {
        assertEquals("Zinc Ore", OreVariantNames.nameOf(Map.of(), "block.mod.zinc_ore", null, "zinc_ore"));
        assertEquals("Ores Deep Zinc Ore", OreVariantNames.nameOf(Map.of(), "block.mod.ores.deep_zinc_ore", null, "ores/deep_zinc_ore"));
    }
}
