package fr.hugman.universal_ores.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DropExperienceRotatedPillarBlock extends RotatedPillarBlock {
    public static final MapCodec<DropExperienceRotatedPillarBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(IntProviders.codec(0, 10).fieldOf("experience").forGetter(block -> block.experienceDropped), propertiesCodec())
                    .apply(instance, DropExperienceRotatedPillarBlock::new)
    );

    private final IntProvider experienceDropped;

    @Override @NotNull
    public MapCodec<? extends RotatedPillarBlock> codec() {
        return CODEC;
    }

    public DropExperienceRotatedPillarBlock(IntProvider experienceDropped, Properties settings) {
        super(settings);
        this.experienceDropped = experienceDropped;
    }

    @Override
    protected void spawnAfterBreak(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull ItemStack tool, boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, tool, dropExperience);
        if (dropExperience) {
            this.tryDropExperience(level, pos, tool, this.experienceDropped);
        }
    }

    /**
     * Overrides NeoForge's {@code IBlockExtension#getExpDrop}, like NeoForge does for {@code DropExperienceBlock}:
     * NeoForge never asks {@link #spawnAfterBreak} to drop experience. Unused on Fabric.
     */
    public int getExpDrop(BlockState state, LevelAccessor level, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity breaker, ItemStack tool) {
        return this.experienceDropped.sample(level.getRandom());
    }
}
