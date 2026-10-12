package fr.hugman.universal_ores.neoforge;

import com.mojang.logging.LogUtils;
import fr.hugman.universal_ores.platform.CreativeModeTabOutput;
import fr.hugman.universal_ores.platform.UniversalOresPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforgespi.language.IModFileInfo;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Registrations that NeoForge delivers through events are kept here until those events fire.
 */
public class NeoForgeUniversalOresPlatform implements UniversalOresPlatform {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<ResourceKey<CreativeModeTab>, List<Consumer<CreativeModeTabOutput>>> creativeModeTabModifiers = new HashMap<>();

    void subscribe(IEventBus modBus) {
        modBus.addListener(BuildCreativeModeTabContentsEvent.class, event -> {
            for (var modifier : this.creativeModeTabModifiers.getOrDefault(event.getTabKey(), List.of())) {
                modifier.accept((anchor, items) -> {
                    insertAfter(event, event.getParentEntries(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY, anchor, items);
                    insertAfter(event, event.getSearchEntries(), CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY, anchor, items);
                });
            }
        });
    }

    @Override
    public void modifyCreativeModeTab(ResourceKey<CreativeModeTab> tab, Consumer<CreativeModeTabOutput> modifier) {
        this.creativeModeTabModifiers.computeIfAbsent(tab, key -> new ArrayList<>()).add(modifier);
    }

    @Override
    public Path configDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public void forEachModFile(String directory, BiConsumer<String, InputStream> visitor) {
        for (IModFileInfo modFile : ModList.get().getModFiles()) {
            // Read while visiting: the resources can't be used afterwards
            Map<String, byte[]> files = new TreeMap<>();
            modFile.getFile().getContents().visitContent(directory, (relativePath, resource) -> {
                if (!relativePath.endsWith(".json")) {
                    return;
                }
                try {
                    files.put(relativePath, resource.readAllBytes());
                } catch (IOException e) {
                    LOGGER.error("Couldn't read {} in {}", relativePath, modFile.getFile().getFileName(), e);
                }
            });
            files.forEach((path, bytes) -> visitor.accept(modFile.getFile().getFileName() + ":" + path, new ByteArrayInputStream(bytes)));
        }
    }

    /**
     * NeoForge's {@code insertAfter} throws when the anchor is missing or the entry is already there, so both are checked first.
     */
    private static void insertAfter(BuildCreativeModeTabContentsEvent event, SequencedSet<ItemStack> entries, CreativeModeTab.TabVisibility visibility, ItemLike anchor, ItemLike... items) {
        ItemStack previous = new ItemStack(anchor);
        for (ItemLike item : items) {
            ItemStack stack = new ItemStack(item);
            if (entries.contains(stack)) {
                continue;
            }
            if (entries.contains(previous)) {
                event.insertAfter(previous, stack, visibility);
            } else {
                event.accept(stack, visibility);
            }
            previous = stack;
        }
    }
}
