package insane96mcp.mobheads.setup;

import insane96mcp.mobheads.MobHeads;
import insane96mcp.mobheads.data.HeadReloadListener;
import insane96mcp.mobheads.data.MobHead;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.*;

public class MHCreativeTabs {

	//Pretty ugly, but since the head list is filled via fillItemList, it's never initialized.
	public static void init() {
		ItemGroup HEADS = new ItemGroup(MobHeads.MOD_ID) {
			@Override
			public ItemStack makeIcon() {
				Random rand = new Random();
				List<MobHead> mobHeads = new ArrayList<>(HeadReloadListener.INSTANCE.getMobHeads());
				return mobHeads.get(rand.nextInt(mobHeads.size())).getRandomHead(rand).getStack();
			}

			@Override
			public ITextComponent getDisplayName() {
				return new StringTextComponent("Mob Heads");
			}

			@Override
			public void fillItemList(NonNullList<ItemStack> items) {
				SortedSet<ResourceLocation> keys = new TreeSet<>(HeadReloadListener.INSTANCE.mobHeads.keySet());
				for (ResourceLocation key : keys) {
					MobHead mobHead = HeadReloadListener.INSTANCE.mobHeads.get(key);
					for (MobHead.Head head : mobHead.getHeads()) {
						items.add(head.getStack());
					}
				}
				super.fillItemList(items);
			}
		};
	}
}
