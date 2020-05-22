package insane96mcp.mobheads.event;

import insane96mcp.mobheads.MobHeads;
import insane96mcp.mobheads.command.GetHeadsCommand;
import insane96mcp.mobheads.data.HeadReloadListener;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = MobHeads.MOD_ID)
public class FMLEventForge {
	@SubscribeEvent
	public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		event.getServer().getResourceManager().addReloadListener(HeadReloadListener.INSTANCE);
	}

	@SubscribeEvent
	public static void onServerStarting(FMLServerStartingEvent event) {
		GetHeadsCommand.register(event.getCommandDispatcher());
	}
}
