package fr.hugman.universal_ores.data.provider;

import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.TexturedModel;
import org.jetbrains.annotations.NotNull;

public class UniversalOresModelProvider extends FabricModelProvider {
    public UniversalOresModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NotNull BlockModelGenerators gen) {
        for (var ores : UniversalOresBlocks.OVERWORLD_ORE_BLOCKS) {
            ores.forEach(gen::createTrivialCube);
        }
        UniversalOresBlocks.BLACKSTONE_ORES.forEach(gen::createTrivialCube);
        UniversalOresBlocks.BASALT_ORES.forEach(block -> gen.createAxisAlignedPillarBlock(block, TexturedModel.COLUMN));
    }

    @Override
    public void generateItemModels(@NotNull ItemModelGenerators gen) {
        // Nothing to do here :D
    }
}
