package fr.hugman.universal_ores.variant;

import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OreVariantDefinitionsTest {
    private static void read(OreVariants.Definitions definitions, String json) {
        definitions.read("test", new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
    }

    private static Set<String> variantIds(OreVariants.Definitions definitions) {
        return definitions.combine().keySet().stream().map(Identifier::toString).collect(java.util.stream.Collectors.toSet());
    }

    @Test
    void stonesGetTheOresOfTheirStones() {
        var definitions = new OreVariants.Definitions();
        read(definitions, """
                {
                  "stones": {
                    "minecraft:granite": "minecraft:stone",
                    "minecraft:blackstone": { "ores_of": "minecraft:netherrack", "texture": "minecraft:block/blackstone_top" },
                    "mod:mixed": ["minecraft:stone", "minecraft:netherrack"]
                  },
                  "ores": {
                    "minecraft:coal_ore": "minecraft:stone",
                    "minecraft:nether_gold_ore": { "in": "minecraft:netherrack", "name": "gold" }
                  }
                }
                """);
        assertEquals(Set.of(
                "universal_ores:granite_coal_ore",
                "universal_ores:blackstone_gold_ore",
                "universal_ores:mod/mixed/minecraft/coal_ore",
                "universal_ores:mod/mixed/minecraft/nether_gold_ore"
        ), variantIds(definitions));
        assertEquals(Identifier.parse("minecraft:block/blackstone_top"), definitions.stones().get(Identifier.parse("minecraft:blackstone")).textures().orElseThrow().all().orElseThrow());
    }

    @Test
    void laterFilesOverrideAndRemove() {
        var definitions = new OreVariants.Definitions();
        read(definitions, """
                { "stones": { "minecraft:granite": "minecraft:stone", "minecraft:diorite": "minecraft:stone" },
                  "ores": { "minecraft:coal_ore": "minecraft:stone", "minecraft:iron_ore": "minecraft:stone" } }
                """);
        read(definitions, """
                { "stones": { "minecraft:diorite": false, "minecraft:granite": { "ores_of": "minecraft:stone", "except": ["minecraft:iron_ore"] } } }
                """);
        assertEquals(Set.of("universal_ores:granite_coal_ore"), variantIds(definitions));
    }

    @Test
    void onlyKeepsTheListedOres() {
        var definitions = new OreVariants.Definitions();
        read(definitions, """
                { "stones": { "promenade:blunite": { "ores_of": "minecraft:stone", "only": ["minecraft:coal_ore"] } },
                  "ores": { "minecraft:coal_ore": "minecraft:stone", "minecraft:iron_ore": "minecraft:stone" } }
                """);
        assertEquals(Set.of("universal_ores:promenade/blunite/minecraft/coal_ore"), variantIds(definitions));
    }

    @Test
    void wrongEntriesAreSkipped() {
        var definitions = new OreVariants.Definitions();
        read(definitions, """
                { "stones": { "minecraft:granite": "minecraft:stone", "Not An ID": "minecraft:stone", "minecraft:diorite": { "strength": 1 }, "minecraft:tuff": true },
                  "ores": { "minecraft:coal_ore": "minecraft:stone" } }
                """);
        assertEquals(Set.of(Identifier.parse("minecraft:granite")), definitions.stones().keySet());
        assertEquals(Set.of("universal_ores:granite_coal_ore"), variantIds(definitions));
    }

    @Test
    void anOreIsntItsOwnVariant() {
        var definitions = new OreVariants.Definitions();
        read(definitions, """
                { "stones": { "minecraft:stone": "minecraft:stone" }, "ores": { "minecraft:coal_ore": "minecraft:stone" } }
                """);
        assertTrue(definitions.combine().isEmpty());
    }
}
