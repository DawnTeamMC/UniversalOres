package fr.hugman.universal_ores.block;

import com.mojang.serialization.MapCodec;
import fr.hugman.universal_ores.UniversalOres;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;

public class UniversalOresBlockTypes {
    public static final MapCodec<DropExperienceRotatedPillarBlock> DROP_EXPERIENCE_ROTATED_PILLAR = register("drop_experience_rotated_pillar", DropExperienceRotatedPillarBlock.CODEC);

    public static MapCodec<DropExperienceRotatedPillarBlock> register(String id, MapCodec<DropExperienceRotatedPillarBlock> codec) {
        return Registry.register(BuiltInRegistries.BLOCK_TYPE, UniversalOres.id(id), codec);
    }
}
