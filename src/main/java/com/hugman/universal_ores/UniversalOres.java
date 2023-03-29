package com.hugman.universal_ores;

import com.hugman.universal_ores.registry.content.UniversalOresBlocks;
import com.mojang.logging.LogUtils;
import fr.hugman.dawn.Registrar;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class UniversalOres implements ModInitializer {
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final Registrar REGISTRAR = new Registrar("universal_ores");

	@Override
	public void onInitialize() {
		UniversalOresBlocks.init(REGISTRAR);
	}
}