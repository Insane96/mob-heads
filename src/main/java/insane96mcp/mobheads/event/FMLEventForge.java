package insane96mcp.mobheads.event;

import insane96mcp.mobheads.data.HeadReloadListener;
import insane96mcp.mobheads.MobHeads;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

@Mod.EventBusSubscriber(modid = MobHeads.MOD_ID)
public class FMLEventForge {
	@SubscribeEvent
	public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		MobHeads.LOGGER.info("onServerAboutToStart");
		event.getServer().getResourceManager().addReloadListener(HeadReloadListener.INSTANCE);
	}
}
