package insane96mcp.mobheads.setup;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.ZombieEntity;

public class Utils {
	public static boolean canCauseSkullDrop(CreeperEntity creeper, LivingEntity killed) {
		//Workaround since zombie subtypes (husks, etc) count towards dropped skulls even if they don't drop one ...
		return creeper.isCharged() && ((creeper.droppedSkulls < 1) || (creeper.droppedSkulls < 2 && killed instanceof ZombieEntity && !killed.getType().getRegistryName().toString().equals("minecraft:zombie")));
	}
}
