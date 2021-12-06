package com.hugman.universal_ores.mixin;

import com.google.common.collect.ImmutableList;
import com.hugman.universal_ores.creator.bundle.block.OverworldOreBundle;
import com.hugman.universal_ores.init.UniversalOresBlocks;
import com.hugman.universal_ores.util.OreUtil;
import net.minecraft.world.gen.feature.OreConfiguredFeatures;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(OreConfiguredFeatures.class)
public class ConfiguredFeaturesMixin {
	@Shadow
	@Final
	@Mutable
	public static List<OreFeatureConfig.Target> COAL_ORES, IRON_ORES, OVERWORLD_GOLD_ORES, COPPER_ORES, LAPIS_ORES, DIAMOND_ORES;

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OreConfiguredFeatures;COAL_ORES:Ljava/util/List;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addCoalOreTargets(CallbackInfo ci) {
		COAL_ORES = ImmutableList.<OreFeatureConfig.Target>builder()
				.addAll(OreUtil.getNewOverworldTargets(OverworldOreBundle::coal))
				.addAll(COAL_ORES)
				.build();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OreConfiguredFeatures;IRON_ORES:Ljava/util/List;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addIronOreTargets(CallbackInfo ci) {
		IRON_ORES = ImmutableList.<OreFeatureConfig.Target>builder()
				.addAll(OreUtil.getNewOverworldTargets(OverworldOreBundle::iron))
				.addAll(IRON_ORES)
				.build();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OreConfiguredFeatures;OVERWORLD_GOLD_ORES:Ljava/util/List;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addGoldOreTargets(CallbackInfo ci) {
		OVERWORLD_GOLD_ORES = ImmutableList.<OreFeatureConfig.Target>builder()
				.addAll(OreUtil.getNewOverworldTargets(OverworldOreBundle::gold))
				.addAll(OVERWORLD_GOLD_ORES)
				.build();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OreConfiguredFeatures;COPPER_ORES:Ljava/util/List;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addCopperOreTargets(CallbackInfo ci) {
		COPPER_ORES = ImmutableList.<OreFeatureConfig.Target>builder()
				.addAll(OreUtil.getNewOverworldTargets(OverworldOreBundle::copper))
				.addAll(COPPER_ORES)
				.build();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OreConfiguredFeatures;LAPIS_ORES:Ljava/util/List;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addLapisOreTargets(CallbackInfo ci) {
		LAPIS_ORES = ImmutableList.<OreFeatureConfig.Target>builder()
				.addAll(OreUtil.getNewOverworldTargets(OverworldOreBundle::lapis))
				.addAll(LAPIS_ORES)
				.build();
	}

	@ModifyArg(
			method = "<clinit>",
			slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=ore_redstone")),
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/OreFeatureConfig;<init>(Ljava/util/List;I)V", ordinal = 0)
	)
	private static List<OreFeatureConfig.Target> modifyRedstoneTargets(List<OreFeatureConfig.Target> original) {
		return ImmutableList.<OreFeatureConfig.Target>builder()
				.addAll(OreUtil.getNewOverworldTargets(OverworldOreBundle::redstone))
				.addAll(original)
				.build();
	}

	@ModifyArg(
			method = "<clinit>",
			slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=ore_emerald")),
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/OreFeatureConfig;<init>(Ljava/util/List;I)V", ordinal = 0)
	)
	private static List<OreFeatureConfig.Target> modifyEmeraldTargets(List<OreFeatureConfig.Target> original) {
		return ImmutableList.<OreFeatureConfig.Target>builder()
				.addAll(OreUtil.getNewOverworldTargets(OverworldOreBundle::emerald))
				.addAll(original)
				.build();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OreConfiguredFeatures;DIAMOND_ORES:Ljava/util/List;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addDiamondOreTargets(CallbackInfo ci) {
		DIAMOND_ORES = ImmutableList.<OreFeatureConfig.Target>builder()
				.addAll(OreUtil.getNewOverworldTargets(OverworldOreBundle::diamond))
				.addAll(DIAMOND_ORES)
				.build();
	}
}