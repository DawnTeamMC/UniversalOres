package fr.hugman.universal_ores.mixin;

import fr.hugman.universal_ores.variant.OreVariantData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(TagLoader.class)
public class TagLoaderMixin {
    @Shadow
    @Final
    private String directory;

    @Inject(method = "load", at = @At("RETURN"))
    private void addOreVariants(ResourceManager resourceManager, CallbackInfoReturnable<Map<Identifier, List<TagLoader.EntryWithSource>>> cir) {
        OreVariantData.addToTags(this.directory, cir.getReturnValue());
    }
}
