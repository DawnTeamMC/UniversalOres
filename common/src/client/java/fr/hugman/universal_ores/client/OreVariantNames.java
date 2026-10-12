package fr.hugman.universal_ores.client;

import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;

import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.Map;

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
            String stone = translations.getOrDefault(variant.stone().getDescriptionId(), variant.stone().getDescriptionId());
            String ore = translations.getOrDefault(variant.ore().getDescriptionId(), variant.ore().getDescriptionId());
            try {
                translations.put(key, String.format(Locale.ROOT, format, stone, ore));
            } catch (IllegalFormatException e) {
                translations.put(key, stone + " " + ore);
            }
        }
    }
}
