package com.hugman.universal_ores.init;

import com.hugman.dawn.api.creator.Creator;
import com.hugman.dawn.api.creator.SimpleCreator;
import com.hugman.dawn.api.creator.bundle.Bundle;
import com.hugman.universal_ores.UniversalOres;

public abstract class UniversalOresBundle extends Bundle {
	protected static <O, V extends SimpleCreator<O>> O add(V creator) {
		UniversalOres.MOD_DATA.addCreator(creator);
		return creator.getValue();
	}

	protected static <V extends Creator> V creator(V creator) {
		UniversalOres.MOD_DATA.addCreator(creator);
		return creator;
	}
}
