package fr.hugman.universal_ores.fabric;

import fr.hugman.universal_ores.platform.CreativeModeTabOutput;
import fr.hugman.universal_ores.platform.UniversalOresPlatform;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Consumer;

public class FabricUniversalOresPlatform implements UniversalOresPlatform {
    @Override
    public void modifyCreativeModeTab(ResourceKey<CreativeModeTab> tab, Consumer<CreativeModeTabOutput> modifier) {
        CreativeModeTabEvents.modifyOutputEvent(tab).register(output -> modifier.accept(output::insertAfter));
    }
}
