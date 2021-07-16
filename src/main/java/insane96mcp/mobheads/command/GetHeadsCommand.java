package insane96mcp.mobheads.command;

import com.mojang.brigadier.CommandDispatcher;
import insane96mcp.mobheads.MobHeads;
import insane96mcp.mobheads.data.HeadReloadListener;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.ResourceLocation;

import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

public class GetHeadsCommand {

	public GetHeadsCommand() {

	}

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
				Commands.literal(MobHeads.RESOURCE_PREFIX + "getHeads")
						.requires(source -> source.hasPermission(2))
						.executes(context -> summonChests(context.getSource()))
		);
	}

	private static int summonChests(CommandSource source) {
		BlockState chest = Blocks.CHEST.defaultBlockState();

		int chestsRequired = (int)Math.ceil(HeadReloadListener.INSTANCE.mobHeads.size() / 27d);
		ResourceLocation[] keys = new ResourceLocation[HeadReloadListener.INSTANCE.mobHeads.size()];
		keys = new TreeSet<>(HeadReloadListener.INSTANCE.mobHeads.keySet()).toArray(keys);
		AtomicInteger headCount = new AtomicInteger(0);

		/*for (int i = chestsRequired - 1; i >= 0; i--) {
			source.getLevel().setBlock(new BlockPos(source.getPosition().add(0, i, 0)), chest, 2);

			TileEntity chestTE = source.getLevel().getBlockEntity(new BlockPos(source.getPosition().add(0, i, 0)));
			ResourceLocation[] finalKeys = keys;
			chestTE.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
					.ifPresent(itemHandler -> {
						int slot = 0;
						do {
							itemHandler.insertItem(slot++, HeadReloadListener.INSTANCE.mobHeads.get(finalKeys[headCount.getAndIncrement()]).getStack(),false);

							if (slot >= itemHandler.getSlots())
								break;
						} while(headCount.get() < HeadReloadListener.INSTANCE.mobHeads.size());
					});
		}*/


		return 1;
	}
}