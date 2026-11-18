package fr.hugman.universal_ores.mixin;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.DropExperienceBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DropExperienceBlock.class)
public interface DropExperienceBlockAccessor {
    @Accessor("xpRange")
    IntProvider getXpRange();
}
