package fr.hugman.universal_ores.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TagEntry.class)
public interface TagEntryAccessor {
    @Accessor("id")
    Identifier getId();

    @Accessor("tag")
    boolean isTag();
}
