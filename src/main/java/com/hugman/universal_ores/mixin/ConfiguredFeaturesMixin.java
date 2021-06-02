package com.hugman.universal_ores.mixin;

import com.google.common.collect.ImmutableList;
import com.hugman.universal_ores.init.UniversalOresBlocks;
import com.hugman.universal_ores.init.world.UniversalOresRuleTests;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConfiguredFeatures.class)
public class ConfiguredFeaturesMixin {
	@Shadow
	@Final
	@Mutable
	public static ImmutableList<OreFeatureConfig.Target> COAL_ORE_TARGETS, IRON_ORE_TARGETS, GOLD_ORE_TARGETS, COPPER_ORE_TARGETS, LAPIS_ORE_TARGETS, REDSTONE_ORE_TARGETS, EMERALD_ORE_TARGETS, DIAMOND_ORE_TARGETS;

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/ConfiguredFeatures;COAL_ORE_TARGETS:Lcom/google/common/collect/ImmutableList;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addCoalOreTargets(CallbackInfo ci) {
		COAL_ORE_TARGETS = ImmutableList.<OreFeatureConfig.Target>builder()
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.ANDESITE_RULE, UniversalOresBlocks.ANDESITE_ORES.coal().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.DIORITE_RULE, UniversalOresBlocks.DIORITE_ORES.coal().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.GRANITE_RULE, UniversalOresBlocks.GRANITE_ORES.coal().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.TUFF_RULE, UniversalOresBlocks.TUFF_ORES.coal().getDefaultState()))
				.addAll(COAL_ORE_TARGETS)
				.build();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/ConfiguredFeatures;IRON_ORE_TARGETS:Lcom/google/common/collect/ImmutableList;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addIronOreTargets(CallbackInfo ci) {
		IRON_ORE_TARGETS = ImmutableList.<OreFeatureConfig.Target>builder()
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.ANDESITE_RULE, UniversalOresBlocks.ANDESITE_ORES.iron().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.DIORITE_RULE, UniversalOresBlocks.DIORITE_ORES.iron().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.GRANITE_RULE, UniversalOresBlocks.GRANITE_ORES.iron().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.TUFF_RULE, UniversalOresBlocks.TUFF_ORES.iron().getDefaultState()))
				.addAll(IRON_ORE_TARGETS)
				.build();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/ConfiguredFeatures;GOLD_ORE_TARGETS:Lcom/google/common/collect/ImmutableList;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addGoldOreTargets(CallbackInfo ci) {
		GOLD_ORE_TARGETS = ImmutableList.<OreFeatureConfig.Target>builder()
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.ANDESITE_RULE, UniversalOresBlocks.ANDESITE_ORES.gold().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.DIORITE_RULE, UniversalOresBlocks.DIORITE_ORES.gold().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.GRANITE_RULE, UniversalOresBlocks.GRANITE_ORES.gold().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.TUFF_RULE, UniversalOresBlocks.TUFF_ORES.gold().getDefaultState()))
				.addAll(GOLD_ORE_TARGETS)
				.build();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/ConfiguredFeatures;COPPER_ORE_TARGETS:Lcom/google/common/collect/ImmutableList;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addCopperOreTargets(CallbackInfo ci) {
		COPPER_ORE_TARGETS = ImmutableList.<OreFeatureConfig.Target>builder()
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.ANDESITE_RULE, UniversalOresBlocks.ANDESITE_ORES.copper().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.DIORITE_RULE, UniversalOresBlocks.DIORITE_ORES.copper().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.GRANITE_RULE, UniversalOresBlocks.GRANITE_ORES.copper().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.TUFF_RULE, UniversalOresBlocks.TUFF_ORES.copper().getDefaultState()))
				.addAll(COPPER_ORE_TARGETS)
				.build();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/ConfiguredFeatures;LAPIS_ORE_TARGETS:Lcom/google/common/collect/ImmutableList;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addLapisOreTargets(CallbackInfo ci) {
		LAPIS_ORE_TARGETS = ImmutableList.<OreFeatureConfig.Target>builder()
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.ANDESITE_RULE, UniversalOresBlocks.ANDESITE_ORES.lapis().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.DIORITE_RULE, UniversalOresBlocks.DIORITE_ORES.lapis().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.GRANITE_RULE, UniversalOresBlocks.GRANITE_ORES.lapis().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.TUFF_RULE, UniversalOresBlocks.TUFF_ORES.lapis().getDefaultState()))
				.addAll(LAPIS_ORE_TARGETS)
				.build();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/ConfiguredFeatures;REDSTONE_ORE_TARGETS:Lcom/google/common/collect/ImmutableList;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addRedstoneOreTargets(CallbackInfo ci) {
		REDSTONE_ORE_TARGETS = ImmutableList.<OreFeatureConfig.Target>builder()
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.ANDESITE_RULE, UniversalOresBlocks.ANDESITE_ORES.redstone().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.DIORITE_RULE, UniversalOresBlocks.DIORITE_ORES.redstone().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.GRANITE_RULE, UniversalOresBlocks.GRANITE_ORES.redstone().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.TUFF_RULE, UniversalOresBlocks.TUFF_ORES.redstone().getDefaultState()))
				.addAll(REDSTONE_ORE_TARGETS)
				.build();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/ConfiguredFeatures;EMERALD_ORE_TARGETS:Lcom/google/common/collect/ImmutableList;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addEmeraldOreTargets(CallbackInfo ci) {
		EMERALD_ORE_TARGETS = ImmutableList.<OreFeatureConfig.Target>builder()
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.ANDESITE_RULE, UniversalOresBlocks.ANDESITE_ORES.emerald().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.DIORITE_RULE, UniversalOresBlocks.DIORITE_ORES.emerald().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.GRANITE_RULE, UniversalOresBlocks.GRANITE_ORES.emerald().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.TUFF_RULE, UniversalOresBlocks.TUFF_ORES.emerald().getDefaultState()))
				.addAll(EMERALD_ORE_TARGETS)
				.build();
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/ConfiguredFeatures;DIAMOND_ORE_TARGETS:Lcom/google/common/collect/ImmutableList;", opcode = Opcodes.PUTSTATIC, ordinal = 0, shift = At.Shift.AFTER))
	private static void addDiamondOreTargets(CallbackInfo ci) {
		DIAMOND_ORE_TARGETS = ImmutableList.<OreFeatureConfig.Target>builder()
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.ANDESITE_RULE, UniversalOresBlocks.ANDESITE_ORES.diamond().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.DIORITE_RULE, UniversalOresBlocks.DIORITE_ORES.diamond().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.GRANITE_RULE, UniversalOresBlocks.GRANITE_ORES.diamond().getDefaultState()))
				.add(OreFeatureConfig.createTarget(UniversalOresRuleTests.TUFF_RULE, UniversalOresBlocks.TUFF_ORES.diamond().getDefaultState()))
				.addAll(DIAMOND_ORE_TARGETS)
				.build();
	}
}