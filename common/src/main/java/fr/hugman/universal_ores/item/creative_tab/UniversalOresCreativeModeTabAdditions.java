package fr.hugman.universal_ores.item.creative_tab;

import fr.hugman.universal_ores.platform.UniversalOresPlatform;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.*;

public class UniversalOresCreativeModeTabAdditions {
    /**
     * Each original ore is followed by its variants, sorted like their stones are registered (vanilla's first).
     * Variants come after the deepslate version of the ore when there is one, as in vanilla.
     */
    public static void appendItemGroups() {
        UniversalOresPlatform.INSTANCE.modifyCreativeModeTab(CreativeModeTabs.NATURAL_BLOCKS, entries -> {
            Map<Block, List<RegisteredOreVariant>> variantsByOre = new LinkedHashMap<>();
            OreVariantRegistrar.registered().stream()
                    .sorted(Comparator.comparingInt(variant -> BuiltInRegistries.BLOCK.getId(variant.stone())))
                    .forEach(variant -> variantsByOre.computeIfAbsent(variant.ore(), ore -> new ArrayList<>()).add(variant));
            variantsByOre.forEach((ore, variants) -> entries.insertAfter(anchor(ore), variants.stream().map(RegisteredOreVariant::block).toArray(ItemLike[]::new)));
        });
    }

    private static ItemLike anchor(Block ore) {
        Identifier id = BuiltInRegistries.BLOCK.getKey(ore);
        Item deepslate = BuiltInRegistries.ITEM.getValue(id.withPrefix("deepslate_"));
        return deepslate != Items.AIR ? deepslate : ore;
    }
}
