package insane96mcp.mobheads.event;

import insane96mcp.mobheads.MobHeads;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = MobHeads.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FMLEventMod {
	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event)
	{

	}
}
