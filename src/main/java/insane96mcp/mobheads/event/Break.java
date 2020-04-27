package insane96mcp.mobheads.event;

import insane96mcp.mobheads.MobHeads;
import insane96mcp.mobheads.data.HeadReloadListener;
import insane96mcp.mobheads.data.MobHead;
import net.minecraft.block.Blocks;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.SkullPlayerBlock;
import net.minecraft.block.SkullWallPlayerBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MobHeads.MOD_ID)
public class Break {

	@SubscribeEvent
	public static void onBlockBreaking(PlayerEvent.BreakSpeed event) {
		if (!(event.getState().getBlock() instanceof SkullPlayerBlock) && !(event.getState().getBlock() instanceof SkullWallPlayerBlock))
			return;

		World world = event.getPlayer().world;
		SkullTileEntity skullTileEntity = (SkullTileEntity) world.getTileEntity(event.getPos());
		CompoundNBT nbt = new CompoundNBT();
		skullTileEntity.write(nbt);
		CompoundNBT owner = nbt.getCompound("Owner");
		UUID headUUID = UUID.fromString(owner.getString("Id"));

		if (!world.isRemote) {
			for (MobHead mobHead : HeadReloadListener.INSTANCE.getMobHeads()) {
				if (mobHead.uuid == null)
					continue;
				if (mobHead.uuid.equals(headUUID)) {
					ItemStack headStack = mobHead.getStack();
					ItemEntity itemEntity = new ItemEntity(world, event.getPos().getX() + .5, event.getPos().getY() + .5, event.getPos().getZ() + .5, headStack);
					itemEntity.setDefaultPickupDelay();
					world.addEntity(itemEntity);
					break;
				}
			}
		}
		world.playEvent(event.getPlayer(), 2001, event.getPos(), SkullBlock.getStateId(event.getState()));
		world.setBlockState(event.getPos(), Blocks.AIR.getDefaultState());
	}
}
