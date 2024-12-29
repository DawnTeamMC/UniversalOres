package net.universal_ores;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.universal_ores.block.ModBlocks;
import net.universal_ores.group.ModGroups;
import net.universal_ores.world.gen.ModOreGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniversalOres implements ModInitializer {
	public static final String MOD_ID = "universal_ores";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);






	@Override
	public void onInitialize() {
		ModBlocks.register();
		ModGroups.register();
		ModOreGeneration.register();



		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if(entity instanceof LivingEntity sheepEntity) {

				if(player.getMainHandStack().getItem() == Items.END_ROD) {
					player.sendMessage(Text.literal("The Player just hit a sheep with an END ROD! YOU SICK FRICK!"));
					player.getMainHandStack().decrement(1);
					sheepEntity.getMainHandStack().decrement(500);
					sheepEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 500));
					sheepEntity.getItemUseTime();
				}

				return ActionResult.PASS;
			}

			return ActionResult.PASS;
		});
	}
}