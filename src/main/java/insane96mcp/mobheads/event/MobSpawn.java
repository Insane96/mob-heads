package insane96mcp.mobheads.event;

import insane96mcp.mobheads.MobHeads;
import insane96mcp.mobheads.data.Fixer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MobHeads.MOD_ID)
public class MobSpawn {

	@SubscribeEvent
	public static void onMobSpawn(EntityJoinWorldEvent event) {
		if (event.getWorld().isRemote)
			return;
		Fixer.fixHorseColorTag(event);
	}
}
