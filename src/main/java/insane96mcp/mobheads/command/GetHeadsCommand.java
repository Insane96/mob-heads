package insane96mcp.mobheads.command;

import com.mojang.brigadier.CommandDispatcher;
import insane96mcp.mobheads.data.HeadReloadListener;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.SortedSet;
import java.util.TreeSet;

public class GetHeadsCommand {

	public GetHeadsCommand() {

	}

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
				Commands.literal("getheads")
				.requires(source -> source.hasPermissionLevel(2))
				.executes(context -> summonChests(context.getSource()))
		);
	}

	private static int summonChests(CommandSource source) {
		BlockState chest = Blocks.CHEST.getDefaultState();
		ChestTileEntity chestTE = new ChestTileEntity();
		chestTE.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
			.ifPresent(itemHandler -> {
				int slot = 0;
				SortedSet<ResourceLocation> keys = new TreeSet<>(HeadReloadListener.INSTANCE.mobHeads.keySet());
				for (ResourceLocation key : keys){
					itemHandler.insertItem(slot++, HeadReloadListener.INSTANCE.mobHeads.get(key).getStack(),false);
					if (slot > itemHandler.getSlots())
						slot = 0;
				}
			});

		source.getWorld().setBlockState(new BlockPos(source.getPos()), chest, 2);
		source.getWorld().setTileEntity(new BlockPos(source.getPos()), chestTE);

		return 1;
	}
}
