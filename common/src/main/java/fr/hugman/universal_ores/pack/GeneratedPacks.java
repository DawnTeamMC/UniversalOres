package fr.hugman.universal_ores.pack;

import fr.hugman.universal_ores.UniversalOres;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.flag.FeatureFlagSet;

import java.util.*;
import java.util.function.Consumer;

/**
 * The resource pack and the data pack that Universal Ores writes at runtime: the models, loot tables and so on of
 * the ore variants, which depend on the mods that are installed.
 * <p>
 * Their files are regular vanilla JSON files, so any resource pack or data pack can still replace them.
 * The packs sit below every other pack and can't be turned off.
 */
public final class GeneratedPacks {
    private static final Map<PackType, List<Consumer<GeneratedPackResources.Output>>> GENERATORS = new EnumMap<>(PackType.class);

    private GeneratedPacks() {
    }

    private static synchronized List<Consumer<GeneratedPackResources.Output>> generators(PackType type) {
        return List.copyOf(GENERATORS.getOrDefault(type, List.of()));
    }

    /**
     * @param generator called every time the pack is opened, to write its files
     */
    public static synchronized void addGenerator(PackType type, Consumer<GeneratedPackResources.Output> generator) {
        GENERATORS.computeIfAbsent(type, t -> new ArrayList<>()).add(generator);
    }

    /**
     * The pack is always listed, since a repository can be made before every generator is added (as the client's is,
     * on Fabric). Its files are only written once it is opened, during a resource reload.
     */
    public static RepositorySource source(PackType type) {
        return output -> {
            PackLocationInfo location = new PackLocationInfo(
                    UniversalOres.MOD_ID + "/generated",
                    Component.translatable("pack.universal_ores.generated.title"),
                    PackSource.BUILT_IN,
                    Optional.empty()
            );
            Pack.ResourcesSupplier resources = new Pack.ResourcesSupplier() {
                @Override
                public PackResources openPrimary(PackLocationInfo info) {
                    return new GeneratedPackResources(info, type, generators(type));
                }

                @Override
                public PackResources openFull(PackLocationInfo info, Pack.Metadata metadata) {
                    return this.openPrimary(info);
                }
            };
            Pack.Metadata metadata = new Pack.Metadata(
                    Component.translatable("pack.universal_ores.generated.description"),
                    PackCompatibility.COMPATIBLE,
                    FeatureFlagSet.of(),
                    List.of()
            );
            output.accept(new Pack(location, resources, metadata, new PackSelectionConfig(true, Pack.Position.BOTTOM, false)));
        };
    }
}
