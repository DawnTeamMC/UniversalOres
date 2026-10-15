package fr.hugman.universal_ores.pack;

import fr.hugman.universal_ores.UniversalOres;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The resource pack and the data pack that Universal Ores writes at runtime: the models, loot tables and so on of
 * the ore variants, which depend on the mods that are installed.
 * <p>
 * Their files are regular vanilla files, so any resource pack or data pack can still replace them.
 * The packs sit below every other pack and can't be turned off.
 * <p>
 * Most files are written by generators when a pack is opened. Files that depend on the other packs (like textures
 * drawn from theirs) are written later in the resource reload, as live files.
 */
public final class GeneratedPacks {
    private static final Map<PackType, List<Consumer<GeneratedPackResources.Output>>> GENERATORS = new EnumMap<>(PackType.class);
    private static final Map<PackType, Map<Identifier, byte[]>> LIVE_FILES = new EnumMap<>(Map.of(
            PackType.CLIENT_RESOURCES, new ConcurrentHashMap<>(),
            PackType.SERVER_DATA, new ConcurrentHashMap<>()
    ));

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
     * Adds or replaces a live file: it can be read from the pack as soon as it is written, even once the pack is open.
     *
     * @param path the file's location inside its namespace, with its extension (e.g. {@code mod:textures/block/x.png})
     */
    public static void putLiveFile(PackType type, Identifier path, byte[] bytes) {
        LIVE_FILES.get(type).put(path, bytes);
    }

    /**
     * Removes the live files matching a filter, like the ones written by the previous resource reload.
     */
    public static void removeLiveFiles(PackType type, Predicate<Identifier> filter) {
        LIVE_FILES.get(type).keySet().removeIf(filter);
    }

    static Map<Identifier, byte[]> liveFiles(PackType type) {
        return LIVE_FILES.get(type);
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
