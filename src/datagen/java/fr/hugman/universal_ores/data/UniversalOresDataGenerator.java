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

        // Data Pack
        var blockTags = pack.addProvider(UniversalOresBlockTagProvider::new);
        pack.addProvider((fabricDataOutput, completableFuture) -> new UniversalOresItemTagProvider(fabricDataOutput, completableFuture, blockTags));
        pack.addProvider(UniversalOresBlockLootTableProvider::new);
        pack.addProvider(UniversalOresRecipeGenerator::create);
    }

    @Override
    @Nullable
    public String getEffectiveModId() {
        return UniversalOres.MOD_ID;
    }
}
