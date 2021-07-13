package insane96mcp.mobheads.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import insane96mcp.mobheads.MobHeads;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Set;

public class HeadReloadListener extends JsonReloadListener {

	public static final HeadReloadListener INSTANCE;
	public static final Gson GSON = new GsonBuilder()
			.disableHtmlEscaping()
			.create();

	public HeadReloadListener() {
		super(GSON, "mob_heads");
		this.mobHeads = HashBiMap.create();
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> map, IResourceManager resourceManager, IProfiler profiler) {
		for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
			ResourceLocation name = entry.getKey();
			String[] split = name.getPath().split("/");
			if (split[split.length - 1].startsWith("_"))
				continue;
			JsonElement json = entry.getValue();
			try {
				MobHead head = MobHead.deserialize(json);
				mobHeads.put(name, head);
			} catch (IllegalArgumentException | JsonParseException e) {
				MobHeads.LOGGER.warn("Head '{}' failed to parse. This is most likely caused by incorrectly specified JSON.", entry.getKey());
				MobHeads.LOGGER.warn("Error: ", e);
			}
		}

		MobHeads.LOGGER.info("{} Heads loaded!", mobHeads.size());
	}

	static {
		INSTANCE = new HeadReloadListener();
	}

	public final BiMap<ResourceLocation, MobHead> mobHeads;

	public Set<MobHead> getMobHeads() {
		return mobHeads.values();
	}
}