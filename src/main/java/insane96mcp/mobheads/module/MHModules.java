package insane96mcp.mobheads.module;

import insane96mcp.mobheads.module.mobheads.MHModule;

public class MHModules {
	public static MHModule mhModule;

	public static void init() {
		mhModule = new MHModule();
	}

	public static void loadConfig() {
		mhModule.loadConfig();
	}
}
