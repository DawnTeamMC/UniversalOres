package fr.hugman.universal_ores.data.provider;

import fr.hugman.universal_ores.data.BuiltInOreVariants;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class UniversalOresModelProvider extends FabricModelProvider {
    public UniversalOresModelProvider(FabricPackOutput output) {
        super(output);
    }

    /**
     * A cube, or a column for pillar stones like basalt, using the generated textures.
     */
    @Override
    public void generateBlockStateModels(@NotNull BlockModelGenerators gen) {
        for (RegisteredOreVariant variant : BuiltInOreVariants.all()) {
            if (variant.block().defaultBlockState().hasProperty(BlockStateProperties.AXIS)) {
                gen.createAxisAlignedPillarBlock(variant.block(), TexturedModel.COLUMN);
            } else {
                gen.createTrivialCube(variant.block());
            }
        }
    }

    @Override
    public void generateItemModels(@NotNull ItemModelGenerators gen) {
        // Block items get their block's model
    }
}
