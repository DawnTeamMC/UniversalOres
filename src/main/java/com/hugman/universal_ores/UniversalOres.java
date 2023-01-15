package com.hugman.universal_ores;

import com.hugman.universal_ores.init.UniversalOresBlocks;
import fr.hugman.dawn.Registrar;
import net.fabricmc.api.ModInitializer;

public class UniversalOres implements ModInitializer {
	public static final Registrar REGISTRAR = new Registrar("universal_ores");

	@Override
	public void onInitialize() {
		UniversalOresBlocks.init(REGISTRAR);
	}
}