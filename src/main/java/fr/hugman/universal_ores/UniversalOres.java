package fr.hugman.universal_ores;

import com.google.common.reflect.Reflection;
import fr.hugman.universal_ores.block.UniversalOresBlocks;
import com.mojang.logging.LogUtils;
import fr.hugman.universal_ores.itemgroup.UniversalOresItemGroupAdditions;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

public class UniversalOres implements ModInitializer {
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final String MOD_ID = "universal_ores";

	@Override
	public void onInitialize() {
		Reflection.initialize(UniversalOresBlocks.class);
		UniversalOresItemGroupAdditions.appendItemGroups();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}