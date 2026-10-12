package fr.hugman.universal_ores.data;

import fr.hugman.universal_ores.UniversalOres;
import fr.hugman.universal_ores.data.provider.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.Nullable;

public class UniversalOresDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // Resource Pack
        pack.addProvider(UniversalOresModelProvider::new);
        pack.addProvider(UniversalOresEnglishLangProvider::new);

        // Data Pack
        pack.addProvider(UniversalOresBlockLootTableProvider::new);
        pack.addProvider(UniversalOresBlockTagProvider::new);
        pack.addProvider(UniversalOresItemTagProvider::new);
        pack.addProvider(UniversalOresRecipeProvider::new);
    }

    @Override
    @Nullable
    public String getEffectiveModId() {
        return UniversalOres.MOD_ID;
    }
}
