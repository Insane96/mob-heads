package insane96mcp.mobheads.event;

import insane96mcp.mobheads.MobHeads;
import insane96mcp.mobheads.data.HeadReloadListener;
import insane96mcp.mobheads.data.MobHead;
import insane96mcp.mobheads.setup.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = MobHeads.MOD_ID)
public class MobDrop {

	@SubscribeEvent
	public static void onMobDrop(LivingDropsEvent event) {
		Entity trueSource = event.getSource().getTrueSource();
		if (!(trueSource instanceof PlayerEntity) && !(trueSource instanceof CreeperEntity))
			return;

		if (trueSource instanceof CreeperEntity){
			CreeperEntity creeper = (CreeperEntity) trueSource;
			if (!Utils.canCauseSkullDrop(creeper, event.getEntityLiving()))
				return;
		}

		LivingEntity entity = event.getEntityLiving();
		ResourceLocation mobId = entity.getType().getRegistryName();
		for (MobHead head : HeadReloadListener.INSTANCE.getMobHeads()) {
			if (mobId == null)
				throw new NullPointerException("killed Mob's ID is null ... somehow");
			if (!mobId.equals(head.mobId))
				continue;
			World world = entity.getEntityWorld();
			Random rand = world.rand;

			boolean nbtMatches = true;
			if (!head.nbt.isEmpty()) {
				CompoundNBT entityNBT = entity.getPersistentData();
				entity.writeAdditional(entityNBT);
				nbtMatches = NBTUtil.areNBTEquals(head.nbt, entityNBT, true);
			}

			if (!nbtMatches)
				continue;

			float chance = (float) (head.chance + (head.lootingChance * event.getLootingLevel()));
			if (trueSource instanceof CreeperEntity) {
				chance = 1f;
				CreeperEntity creeper = (CreeperEntity) trueSource;
				creeper.incrementDroppedSkulls();
			}
			if (rand.nextFloat() > chance)
				continue;
			ItemStack headStack = head.getStack();
			ItemEntity itemEntity = new ItemEntity(entity.getEntityWorld(), entity.getPosX(), entity.getPosY(), entity.getPosZ(), headStack);
			itemEntity.setDefaultPickupDelay();
			event.getDrops().add(itemEntity);
		}
	}
}
