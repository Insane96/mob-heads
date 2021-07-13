package insane96mcp.mobheads.module.mobheads;

import insane96mcp.insanelib.base.Label;
import insane96mcp.insanelib.base.Module;
import insane96mcp.mobheads.module.mobheads.feature.BaseFeature;
import insane96mcp.mobheads.setup.MHConfig;

@Label(name = "MobHeads")
public class MHModule extends Module {
	public BaseFeature base;

	public MHModule() {
		super(MHConfig.builder);
		pushConfig(MHConfig.builder);
		base = new BaseFeature(this);
		MHConfig.builder.pop();
	}

	@Override
	public void loadConfig() {
		super.loadConfig();
		base.loadConfig();
	}
}
