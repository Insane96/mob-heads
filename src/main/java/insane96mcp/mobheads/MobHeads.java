package insane96mcp.mobheads;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MobHeads.MOD_ID)
public class MobHeads
{
    public static final String MOD_ID = "mobheads";
    public static final String RESOURCE_PREFIX = MOD_ID + ":";
    public static final Logger LOGGER = LogManager.getLogger();

    public MobHeads() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, insane96mcp.mobheads.setup.ModConfig.COMMON_SPEC);
    }
}