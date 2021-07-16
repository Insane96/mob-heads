package insane96mcp.mobheads;

import insane96mcp.mobheads.data.HeadReloadListener;
import insane96mcp.mobheads.setup.MHConfig;
import insane96mcp.mobheads.setup.MHCreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MobHeads.MOD_ID)
public class MobHeads
{
    public static final String MOD_ID = "mobheads";
    public static final String RESOURCE_PREFIX = MOD_ID + ":";
    public static final Logger LOGGER = LogManager.getLogger();

    public MobHeads() {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, MHConfig.COMMON_SPEC);
        MinecraftForge.EVENT_BUS.register(this);

        MHCreativeTabs.init();
    }

    @SubscribeEvent
    public void onServerAboutToStart(AddReloadListenerEvent event) {
        event.addListener(HeadReloadListener.INSTANCE);
    }

    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent event) {
        //GetHeadsCommand.register(event.getDispatcher());
    }
}
