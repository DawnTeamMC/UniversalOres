package net.universal_ores.util;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.universal_ores.UniversalOres;




public class ModTags {
    public static final TagKey<Block> DIORITE_ORE_REPLACEABLES = createTag("diorite_ore_replaceables");
    public static final TagKey<Block> GRANITE_ORE_REPLACEABLES = createTag("granite_ore_replaceables");
    public static final TagKey<Block> ANDESITE_ORE_REPLACEABLES = createTag("andesite_ore_replaceables");
    public static final TagKey<Block> CALCITE_ORE_REPLACEABLES = createTag("calcite_ore_replaceables");
    public static final TagKey<Block> TUFF_ORE_REPLACEABLES = createTag("tuff_ore_replaceables");
    public static final TagKey<Block> BLACKSTONE_ORE_REPLACEABLES = createTag("blackstone_ore_replaceables");
    public static final TagKey<Block> BASALT_ORE_REPLACEABLES = createTag("basalt_ore_replaceables");
    private static TagKey<Block> createTag(String name) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(UniversalOres.MOD_ID, name));
    }
}
