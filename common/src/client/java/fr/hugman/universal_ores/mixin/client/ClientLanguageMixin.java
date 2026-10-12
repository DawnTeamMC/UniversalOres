package fr.hugman.universal_ores.mixin.client;

import fr.hugman.universal_ores.client.OreVariantNames;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {
    @ModifyArg(method = "loadFrom", at = @At(value = "INVOKE", target = "Ljava/util/Map;copyOf(Ljava/util/Map;)Ljava/util/Map;"))
    private static Map<String, String> addOreVariantNames(Map<String, String> translations) {
        OreVariantNames.addMissing(translations);
        return translations;
    }
}
