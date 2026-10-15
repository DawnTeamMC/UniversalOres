package fr.hugman.universal_ores.client;

import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Names the ore variants that no language file names, from the names of their stone and of their original ore:
 * "Blunite" and "Coal Ore" make "Blunite Coal Ore". Each language sets how they combine with the
 * {@value #FORMAT_KEY} key.
 */
public final class OreVariantNames {
    public static final String FORMAT_KEY = "block.universal_ores.ore_variant";

    private OreVariantNames() {
    }

    public static void addMissing(Map<String, String> translations) {
        String format = translations.getOrDefault(FORMAT_KEY, "%1$s %2$s");
        for (RegisteredOreVariant variant : OreVariantRegistrar.registered()) {
            String key = variant.block().getDescriptionId();
            if (translations.containsKey(key)) {
                continue;
            }
            String stone = nameOf(translations, variant.stone());
            String ore = nameOf(translations, variant.ore());
            try {
                translations.put(key, String.format(Locale.ROOT, format, stone, ore));
            } catch (IllegalFormatException e) {
                translations.put(key, stone + " " + ore);
            }
        }
    }

    private static String nameOf(Map<String, String> translations, Block block) {
        Item item = block.asItem();
        return nameOf(translations, block.getDescriptionId(), item == Items.AIR ? null : item.getDescriptionId(), BuiltInRegistries.BLOCK.getKey(block).getPath());
    }

    /**
     * The name of a block, from its own key. Some mods only name the item of their blocks (like More Ores'
     * {@code item.more-ores.tin_ore}), so the item's key comes next, and the block's ID last ("tin_ore" makes
     * "Tin Ore"), so that a variant never shows a translation key.
     *
     * @param itemKey the translation key of the block's item, if it has one
     */
    static String nameOf(Map<String, String> translations, String blockKey, @Nullable String itemKey, String path) {
        String name = translations.get(blockKey);
        if (name == null && itemKey != null) {
            name = translations.get(itemKey);
        }
        return name != null ? name : Arrays.stream(path.split("[_/]"))
                .filter(word -> !word.isEmpty())
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
