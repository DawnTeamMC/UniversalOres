package fr.hugman.universal_ores.mixin;

import fr.hugman.universal_ores.variant.OreVariantData;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.Map;

@Mixin(TagLoader.class)
public class TagLoaderMixin {
    @Shadow
    @Final
    private String directory;

    /**
     * When the tags are built, once every mod has changed them as they were loaded (some replace them, cancelling
     * other changes made then).
     */
    @ModifyVariable(method = "build", at = @At("HEAD"), argsOnly = true)
    private Map<Identifier, List<TagLoader.EntryWithSource>> addOreVariants(Map<Identifier, List<TagLoader.EntryWithSource>> builders) {
        return OreVariantData.withOreVariants(this.directory, builders);
    }
}
