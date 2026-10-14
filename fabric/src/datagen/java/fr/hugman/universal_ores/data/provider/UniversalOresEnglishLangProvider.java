package fr.hugman.universal_ores.data.provider;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.hugman.universal_ores.client.OreVariantNames;
import fr.hugman.universal_ores.data.BuiltInOreVariants;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class UniversalOresEnglishLangProvider extends FabricLanguageProvider {
    public UniversalOresEnglishLangProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NotNull Provider registries, @NotNull TranslationBuilder builder) {
        builder.add("modmenu.descriptionTranslation.universal_ores", "Adds ore variants in andesite, diorite, granite, tuff, calcite, blackstone, basalt, smooth basalt and the stones of other mods.");
        builder.add("pack.universal_ores.generated.title", "Universal Ores");
        builder.add("pack.universal_ores.generated.description", "Ore variants for the installed mods");
        // How the variants of other mods are named, from the names of their stone and ore
        builder.add(OreVariantNames.FORMAT_KEY, "%1$s %2$s");

        // "Granite" + "Coal Ore", or "Blackstone" + "Gold Ore" for ores named in their variants' IDs
        JsonObject vanilla = vanillaEnglish();
        for (RegisteredOreVariant variant : BuiltInOreVariants.all()) {
            String stone = vanilla.get(variant.stone().getDescriptionId()).getAsString();
            String ore = variant.variant().oreType().name()
                    .map(name -> name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1) + " Ore")
                    .orElseGet(() -> vanilla.get(variant.ore().getDescriptionId()).getAsString());
            builder.add(variant.block(), stone + " " + ore);
        }
    }

    private static JsonObject vanillaEnglish() {
        try (InputStream stream = Objects.requireNonNull(UniversalOresEnglishLangProvider.class.getResourceAsStream("/assets/minecraft/lang/en_us.json"))) {
            return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
