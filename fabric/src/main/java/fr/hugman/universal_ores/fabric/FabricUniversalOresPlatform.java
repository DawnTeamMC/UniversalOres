package fr.hugman.universal_ores.fabric;

import com.mojang.logging.LogUtils;
import fr.hugman.universal_ores.platform.CreativeModeTabOutput;
import fr.hugman.universal_ores.platform.UniversalOresPlatform;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FabricUniversalOresPlatform implements UniversalOresPlatform {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void modifyCreativeModeTab(ResourceKey<CreativeModeTab> tab, Consumer<CreativeModeTabOutput> modifier) {
        CreativeModeTabEvents.modifyOutputEvent(tab).register(output -> modifier.accept(output::insertAfter));
    }

    @Override
    public Path configDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public void forEachModFile(String directory, BiConsumer<String, InputStream> visitor) {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            for (Path root : mod.getRootPaths()) {
                Path folder = root.resolve(directory);
                if (!Files.isDirectory(folder)) {
                    continue;
                }
                try (Stream<Path> files = Files.walk(folder)) {
                    for (Path file : files.filter(f -> Files.isRegularFile(f) && f.toString().endsWith(".json")).sorted().toList()) {
                        visitor.accept(mod.getMetadata().getId() + ":" + root.relativize(file).toString().replace('\\', '/'), Files.newInputStream(file));
                    }
                } catch (IOException e) {
                    LOGGER.error("Couldn't list the files of {}", mod.getMetadata().getId(), e);
                }
            }
        }
    }
}
