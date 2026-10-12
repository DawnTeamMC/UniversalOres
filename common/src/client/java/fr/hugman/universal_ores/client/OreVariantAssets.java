package fr.hugman.universal_ores.client;

import com.google.gson.JsonObject;
import fr.hugman.universal_ores.client.texture.OreVariantTextures;
import fr.hugman.universal_ores.pack.GeneratedPackResources;
import fr.hugman.universal_ores.pack.GeneratedPacks;
import fr.hugman.universal_ores.variant.OreVariantRegistrar;
import fr.hugman.universal_ores.variant.RegisteredOreVariant;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The block states, block models and item models of the ore variants, written in the generated resource pack.
 * They are the ones vanilla's data generators would write: a {@code cube_all} model, or a {@code cube_column} one
 * rotated along the axis for pillar stones.
 */
public final class OreVariantAssets {
    private OreVariantAssets() {
    }

    public static void init() {
        GeneratedPacks.addGenerator(PackType.CLIENT_RESOURCES, output -> OreVariantRegistrar.registered().forEach(variant -> write(variant, output)));
    }

    private static void write(RegisteredOreVariant registered, GeneratedPackResources.Output output) {
        Identifier id = registered.variant().id();
        Identifier model = id.withPrefix("block/");
        List<OreVariantTextures.Face> faces = OreVariantTextures.faces(registered);

        JsonObject textures = new JsonObject();
        JsonObject modelJson = new JsonObject();
        if (faces.size() == 2) {
            modelJson.addProperty("parent", "minecraft:block/cube_column");
            textures.addProperty("side", faces.get(0).sprite().toString());
            textures.addProperty("end", faces.get(1).sprite().toString());
        } else {
            modelJson.addProperty("parent", "minecraft:block/cube_all");
            textures.addProperty("all", faces.getFirst().sprite().toString());
        }
        modelJson.add("textures", textures);
        output.accept(id.withPath(path -> "models/block/" + path + ".json"), modelJson);

        JsonObject variants = new JsonObject();
        for (BlockState state : registered.block().getStateDefinition().getPossibleStates()) {
            JsonObject variant = new JsonObject();
            variant.addProperty("model", model.toString());
            if (state.hasProperty(BlockStateProperties.AXIS)) {
                Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                if (axis != Direction.Axis.Y) {
                    variant.addProperty("x", 90);
                }
                if (axis == Direction.Axis.X) {
                    variant.addProperty("y", 90);
                }
            }
            variants.add(variantName(state), variant);
        }
        JsonObject blockState = new JsonObject();
        blockState.add("variants", variants);
        output.accept(id.withPath(path -> "blockstates/" + path + ".json"), blockState);

        JsonObject itemModel = new JsonObject();
        itemModel.addProperty("type", "minecraft:model");
        itemModel.addProperty("model", model.toString());
        JsonObject item = new JsonObject();
        item.add("model", itemModel);
        output.accept(id.withPath(path -> "items/" + path + ".json"), item);
    }

    private static String variantName(BlockState state) {
        return state.getValues().map(Property.Value::toString).collect(Collectors.joining(","));
    }
}
