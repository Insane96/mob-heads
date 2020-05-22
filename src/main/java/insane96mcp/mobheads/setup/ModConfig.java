package insane96mcp.mobheads.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
	public static final ForgeConfigSpec COMMON_SPEC;
	public static final CommonConfig COMMON;

	static {
		final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
		COMMON = specPair.getLeft();
		COMMON_SPEC = specPair.getRight();
	}

	public static class CommonConfig {
		public ForgeConfigSpec.ConfigValue<HeadsFromSpawners> mobsFromSpawnersDropNoHead;

		public CommonConfig(final ForgeConfigSpec.Builder builder) {
			mobsFromSpawnersDropNoHead = builder
					.comment("Set how mobs from spawners will behave when killed.\nNONE will make mobs spawned from spawners drop no head when killed (except for vanilla mobs when killed by charged creepers).\nCREEPER_ONLY (by default) will make mobs from spawners drop their head only when killed by Charged Creepers.\nNO_CHANGE will make mobs from spawners drop heads like normal.")
					.defineEnum("Mobs From Spawners Head Drop Behaviour", HeadsFromSpawners.CREEPER_ONLY);
		}
	}

	public enum HeadsFromSpawners {
		NONE,
		CREEPER_ONLY,
		NO_CHANGE
	}
}
