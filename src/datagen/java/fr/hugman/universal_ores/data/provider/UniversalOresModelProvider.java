package fr.hugman.universal_ores.data.provider;


import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.TexturedModel;

public class UniversalOresModelProvider extends FabricModelProvider {
    public UniversalOresModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        for (var ores : UniversalOresBlocks.OVERWORLD_ORE_BLOCKS) {
            ores.forEach(gen::registerSimpleCubeAll);
        }
        UniversalOresBlocks.BLACKSTONE_ORES.forEach(gen::registerSimpleCubeAll);
        UniversalOresBlocks.BASALT_ORES.forEach(block -> gen.registerAxisRotated(block, TexturedModel.CUBE_COLUMN));
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
        // Nothing to do here :D
    }
}
