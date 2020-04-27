package insane96mcp.mobheads.setup;

import insane96mcp.mobheads.MobHeads;
import insane96mcp.mobheads.data.HeadReloadListener;
import insane96mcp.mobheads.data.MobHead;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class ModTabs {
	public static final ItemGroup HEADS = new ItemGroup(MobHeads.MOD_ID) {
		@Override
		public ItemStack createIcon() {
			List<MobHead> mobHeads = new ArrayList<>(HeadReloadListener.INSTANCE.getMobHeads());

			Random rand = new Random();
			return mobHeads.get(rand.nextInt(mobHeads.size())).getStack();
		}

		@Override
		public String getTranslationKey() {
			return "Mob Heads";
		}

		@Override
		public void fill(NonNullList<ItemStack> items) {
			super.fill(items);
			SortedSet<ResourceLocation> keys = new TreeSet<>(HeadReloadListener.INSTANCE.mobHeads.keySet());
			for (ResourceLocation key : keys){
				items.add(HeadReloadListener.INSTANCE.mobHeads.get(key).getStack());
			}
		}
	};
}
