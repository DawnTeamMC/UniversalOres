package com.hugman.universal_ores;

import com.hugman.dawn.api.object.ModData;
import com.hugman.universal_ores.init.UniversalOresBlocks;
import net.fabricmc.api.ModInitializer;

public class UniversalOres implements ModInitializer {
	public static final ModData MOD_DATA = new ModData("universal_ores");

	@Override
	public void onInitialize() {
		UniversalOresBlocks.init();
		MOD_DATA.registerCreators();
	}
}