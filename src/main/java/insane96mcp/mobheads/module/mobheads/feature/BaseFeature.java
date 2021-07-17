package insane96mcp.mobheads.module.mobheads.feature;

import insane96mcp.insanelib.base.Feature;
import insane96mcp.insanelib.base.Label;
import insane96mcp.insanelib.base.Module;
import insane96mcp.mobheads.data.Fixer;
import insane96mcp.mobheads.data.HeadReloadListener;
import insane96mcp.mobheads.data.MobHead;
import insane96mcp.mobheads.setup.MHConfig;
import insane96mcp.mobheads.setup.Strings;
import net.minecraft.block.SkullPlayerBlock;
import net.minecraft.block.SkullWallPlayerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;
import java.util.UUID;

@Label(name = "General")
public class BaseFeature extends Feature {

	public ForgeConfigSpec.ConfigValue<HeadsFromSpawners> mobsFromSpawnersDropNoHeadConfig;
	public ForgeConfigSpec.ConfigValue<Boolean> chargedCreeperOnlyConfig;

	public HeadsFromSpawners mobsFromSpawnersDropNoHead = HeadsFromSpawners.CREEPER_ONLY;
	public boolean chargedCreeperOnly = false;

	public BaseFeature(Module module) {
		super(MHConfig.builder, module);
		MHConfig.builder.comment(this.getDescription()).push(this.getName());
		mobsFromSpawnersDropNoHeadConfig = MHConfig.builder
				.comment("Set how mobs from spawners will behave when killed.\n" +
						"NONE will make mobs spawned from spawners drop no head when killed (except for vanilla mobs when killed by charged creepers).\n" +
						"CREEPER_ONLY (by default) will make mobs from spawners drop their head only when killed by Charged Creepers.\n" +
						"NO_CHANGE will make mobs from spawners drop heads like normal.")
				.defineEnum("Mobs From Spawners Head Drop Behaviour", this.mobsFromSpawnersDropNoHead);
		chargedCreeperOnlyConfig = MHConfig.builder
				.comment("Set to true to make mobs drop their heads only via Charged creepers instead of having a percentage chance to drop it when killed.")
				.define("Charged Creeper Only", this.chargedCreeperOnly);
		MHConfig.builder.pop();
	}

	@Override
	public void loadConfig() {
		super.loadConfig();
		this.mobsFromSpawnersDropNoHead = this.mobsFromSpawnersDropNoHeadConfig.get();
		this.chargedCreeperOnly = this.chargedCreeperOnlyConfig.get();
	}

	@SubscribeEvent
	public void onMobDrop(LivingDropsEvent event) {
		Entity trueSource = event.getSource().getEntity();
		if (!(trueSource instanceof PlayerEntity) && !(trueSource instanceof CreeperEntity))
			return;

		if (trueSource instanceof CreeperEntity){
			CreeperEntity creeper = (CreeperEntity) trueSource;
			if (!canCauseSkullDrop(creeper, event.getEntityLiving()))
				return;
		}

		if (!(trueSource instanceof CreeperEntity) && this.chargedCreeperOnly)
			return;

		LivingEntity entity = event.getEntityLiving();
		CompoundNBT entityNBT = entity.getPersistentData();
		if (entityNBT.getBoolean(Strings.Tags.SPAWNED_FROM_SPAWNER) && mobsFromSpawnersDropNoHead == HeadsFromSpawners.NONE)
			return;
		else if (entityNBT.getBoolean(Strings.Tags.SPAWNED_FROM_SPAWNER) && mobsFromSpawnersDropNoHead == HeadsFromSpawners.CREEPER_ONLY && !(trueSource instanceof CreeperEntity))
			return;

		ResourceLocation mobId = entity.getType().getRegistryName();
		if (mobId == null)
			throw new NullPointerException("killed Mob's ID is null ... somehow");

		for (MobHead mobHead : HeadReloadListener.INSTANCE.getMobHeads()) {
			if (!mobId.equals(mobHead.mobId))
				continue;
			World world = entity.level;
			Random rand = world.random;

			boolean nbtMatches = true;
			if (!mobHead.nbt.isEmpty()) {
				entity.addAdditionalSaveData(entityNBT);
				nbtMatches = NBTUtil.compareNbt(mobHead.nbt, entityNBT, true);
			}

			if (!nbtMatches)
				continue;

			MobHead.Head head = mobHead.getRandomHead(entity.getRandom());

			float chance = (float) (head.chance + (head.lootingChance * event.getLootingLevel()));
			if (trueSource instanceof CreeperEntity) {
				chance = 1f;
				CreeperEntity creeper = (CreeperEntity) trueSource;
				creeper.increaseDroppedSkulls();
			}
			if (rand.nextFloat() > chance)
				continue;
			ItemStack headStack = head.getStack();
			ItemEntity itemEntity = new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), headStack);
			itemEntity.setDefaultPickUpDelay();
			event.getDrops().add(itemEntity);
			break;
		}
	}

	@SubscribeEvent
	public void onMobSpawn(EntityJoinWorldEvent event) {
		if (event.getWorld().isClientSide)
			return;
		Fixer.fixHorseColorTag(event);
	}

	@SubscribeEvent
	public void onSpawnFromSpawner(LivingSpawnEvent.CheckSpawn event) {
		if (event.getSpawnReason() == SpawnReason.SPAWNER)
			event.getEntityLiving().getPersistentData().putBoolean(Strings.Tags.SPAWNED_FROM_SPAWNER, true);
	}

	@SubscribeEvent
	public void onBlockBreaking(PlayerEvent.BreakSpeed event) {
		if (!(event.getState().getBlock() instanceof SkullPlayerBlock) && !(event.getState().getBlock() instanceof SkullWallPlayerBlock))
			return;

		World world = event.getPlayer().level;

		if (world.isClientSide)
			return;

		SkullTileEntity skullTileEntity = (SkullTileEntity) world.getBlockEntity(event.getPos());
		CompoundNBT nbt = new CompoundNBT();
		skullTileEntity.save(nbt);
		CompoundNBT owner = nbt.getCompound("SkullOwner");
		UUID headUUID = owner.getUUID("Id");

		boolean isModHead = false;

		for (MobHead mobHead : HeadReloadListener.INSTANCE.getMobHeads()) {
			for (MobHead.Head head : mobHead.getHeads()) {
				if (!(head instanceof MobHead.Head.NBTHead))
					continue;

				if (((MobHead.Head.NBTHead) head).uuid.equals(headUUID)) {
					ItemStack headStack = head.getStack();
					ItemEntity itemEntity = new ItemEntity(world, event.getPos().getX() + .5, event.getPos().getY() + .5, event.getPos().getZ() + .5, headStack);
					itemEntity.setDefaultPickUpDelay();
					world.addFreshEntity(itemEntity);
					isModHead = true;
					break;
				}
			}

			if (isModHead)
				break;
		}

		if (!isModHead)
			return;

		world.destroyBlock(event.getPos(), false, event.getPlayer());
	}

	public static boolean canCauseSkullDrop(CreeperEntity creeper, LivingEntity killed) {
		//Workaround since zombie subtypes (husks, etc) count towards dropped skulls even if they don't drop one ...
		return creeper.isPowered() && ((creeper.droppedSkulls < 1) || (creeper.droppedSkulls < 2 && killed instanceof ZombieEntity && !killed.getType().getRegistryName().toString().equals("minecraft:zombie")));
	}

	public enum HeadsFromSpawners {
		NONE,
		CREEPER_ONLY,
		NO_CHANGE
	}
}
