package fr.hugman.universal_ores.fabric.mixin;

import fr.hugman.universal_ores.pack.GeneratedPacks;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Arrays;

/**
 * Adds the generated packs to the resource pack and data pack repositories, which are told apart by their built-in
 * (vanilla) source. NeoForge does this through {@code AddPackFindersEvent} instead.
 */
@Mixin(PackRepository.class)
public class PackRepositoryMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static RepositorySource[] addGeneratedPack(RepositorySource[] sources) {
        for (RepositorySource source : sources) {
            if (source instanceof BuiltInPackSource builtIn) {
                RepositorySource[] withGenerated = Arrays.copyOf(sources, sources.length + 1);
                withGenerated[sources.length] = GeneratedPacks.source(((BuiltInPackSourceAccessor) builtIn).universal_ores$getPackType());
                return withGenerated;
            }
        }
        return sources;
    }
}
