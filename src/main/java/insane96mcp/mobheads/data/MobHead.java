package insane96mcp.mobheads.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class MobHead {
	public ResourceLocation mobId;
	public CompoundNBT nbt;
	private final ArrayList<Head> heads = new ArrayList<>();

	public MobHead(ResourceLocation mobId, CompoundNBT nbt) {
		this.mobId = mobId;
		this.nbt = nbt;
	}

	public ArrayList<Head> getHeads() {
		return this.heads;
	}

	public Head getRandomHead(Random random) {
		return this.heads.get(random.nextInt(this.heads.size()));
	}

	public void addHead(Head head) {
		this.heads.add(head);
	}

	public static MobHead deserialize(JsonElement jsonElement) {
		if (!jsonElement.isJsonObject())
			throw new JsonParseException("MobHead must be a JSON Object");

		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String m = JSONUtils.getAsString(jsonObject, "mob_id");
		ResourceLocation mobId = ResourceLocation.tryParse(m);
		if (mobId == null)
			throw new JsonParseException("mob_id is not valid");

		String n = JSONUtils.getAsString(jsonObject, "nbt", "{}");
		CompoundNBT nbt;
		try {
			nbt = JsonToNBT.parseTag(n);
		}
		catch (CommandSyntaxException e) {
			throw new JsonParseException("nbt could not be parsed\n" + e);
		}

		MobHead mobHead = new MobHead(mobId, nbt);

		JsonArray headsJSON = JSONUtils.getAsJsonArray(jsonObject, "heads");
		for (JsonElement headElement : headsJSON) {
			JsonObject headJSON = headElement.getAsJsonObject();
			String hId = JSONUtils.getAsString(headJSON, "head_id", "NULL");
			ResourceLocation headId = ResourceLocation.tryParse(hId);

			String headTexture = JSONUtils.getAsString(headJSON, "head_texture", "");

			String u = JSONUtils.getAsString(headJSON, "uuid", "");
			UUID uuid = null;
			if (!u.equals(""))
				uuid = UUID.fromString(u);

			String headName = JSONUtils.getAsString(headJSON, "head_name", "");

			double chance = JSONUtils.getAsFloat(headJSON, "chance");
			double lootingChance = JSONUtils.getAsFloat(headJSON, "looting_chance", 0f);

			if (headId != null)
				mobHead.addHead(new Head.ItemHead(headId, chance, lootingChance));
			else if (!headTexture.equals("") && uuid != null && !headName.equals(""))
				mobHead.addHead(new Head.NBTHead(uuid, headName, headTexture, chance, lootingChance));
			else
				throw new JsonParseException("head_id and head_texture, uuid, head_name are missing");
		}

		return mobHead;
	}

	@Override
	public String toString() {
		return String.format("MobHead[mobId: %s, nbt: %s, heads: %s]", this.mobId, this.nbt, this.heads);
	}

	public abstract static class Head {
		public double chance;
		public double lootingChance;

		protected Head(double chance, double lootingChance) {
			this.chance = chance;
			this.lootingChance = lootingChance;
		}

		public abstract ItemStack getStack();

		public static class ItemHead extends Head {
			public ResourceLocation headId;

			public ItemHead(ResourceLocation headId, double chance, double lootingChance) {
				super(chance, lootingChance);
				this.headId = headId;
			}

			public ItemStack getStack() {
				return new ItemStack(ForgeRegistries.ITEMS.getValue(headId), 1);
			}

			@Override
			public String toString() {
				return String.format("ItemHead[headId: %s, chance: %.4f, lootingChance: %.4f]", this.headId, this.chance, this.lootingChance);
			}
		}

		public static class NBTHead extends Head {
			public UUID uuid;
			public String headName;
			public String headTexture;

			public NBTHead(UUID uuid, String headName, String headTexture, double chance, double lootingChance) {
				super(chance, lootingChance);
				this.uuid = uuid;
				this.headName = headName;
				this.headTexture = headTexture;
			}

			public ItemStack getStack() {
				ItemStack headStack = new ItemStack(Blocks.PLAYER_HEAD, 1);
				CompoundNBT nbt = new CompoundNBT();
				CompoundNBT skullOwner = new CompoundNBT();
				CompoundNBT properties = new CompoundNBT();
				ListNBT textures = new ListNBT();
				CompoundNBT texture = new CompoundNBT();
				texture.putString("Value", this.headTexture);
				textures.add(0, texture);
				properties.put("textures", textures);
				skullOwner.putUUID("Id", this.uuid);
				skullOwner.put("Properties", properties);
				nbt.put("SkullOwner", skullOwner);
				String stackName = this.headName;
				CompoundNBT display = new CompoundNBT();
				display.putString("Name", "{\"text\":\"" + stackName + "\", \"italic\": false}");
				nbt.put("display", display);
				headStack.setTag(nbt);
				return headStack;
			}

			@Override
			public String toString() {
				return String.format("NBTHead[uuid: %s, headName: %s, headTexture: %s, chance: %.4f, lootingChance: %.4f]", this.uuid, this.headName, this.headTexture, this.chance, this.lootingChance);
			}
		}
	}
}
