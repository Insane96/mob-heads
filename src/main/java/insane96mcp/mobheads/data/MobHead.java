package insane96mcp.mobheads.data;

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
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class MobHead {
	public ResourceLocation mobId;
	public CompoundNBT nbt;
	public String head;
	public UUID uuid;
	public String headName;
	public ResourceLocation headId;
	public double chance;
	public double lootingChance;

	public MobHead(ResourceLocation mobId, CompoundNBT nbt, String head, UUID uuid, String headName, double chance, double lootingChance) {
		this.mobId = mobId;
		this.nbt = nbt;
		this.head = head;
		this.uuid = uuid;
		this.headName = headName;
		this.chance = chance;
		this.lootingChance = lootingChance;
	}

	public MobHead(ResourceLocation mobId, CompoundNBT nbt, ResourceLocation headId, double chance, double lootingChance) {
		this.mobId = mobId;
		this.nbt = nbt;
		this.headId = headId;
		this.chance = chance;
		this.lootingChance = lootingChance;
	}

	public static MobHead deserialize(JsonElement json) {
		if (!json.isJsonObject())
			throw new JsonParseException("MobHead must be a JSON Object");

		JsonObject jsonObject = json.getAsJsonObject();
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
			throw new JsonParseException("nbt could not be parsed\n" + e.toString());
		}

		String hId = JSONUtils.getAsString(jsonObject, "head_id", "NULL");
		ResourceLocation headId = ResourceLocation.tryParse(hId);

		String head = JSONUtils.getAsString(jsonObject, "head", "");

		String u = JSONUtils.getAsString(jsonObject, "uuid", "");
		UUID uuid = null;
		if (!u.equals(""))
			uuid = UUID.fromString(u);

		String headName = JSONUtils.getAsString(jsonObject, "head_name", "");

		double chance = JSONUtils.getAsFloat(jsonObject, "chance");
		double lootingChance = JSONUtils.getAsFloat(jsonObject, "looting_chance", 0f);

		if (headId != null)
			return new MobHead(mobId, nbt, headId, chance, lootingChance);
		else if (!head.equals("") && uuid != null && !headName.equals(""))
			return new MobHead(mobId, nbt, head, uuid, headName, chance, lootingChance);
		else
			throw new JsonParseException("head_id and head, uuid, head_name are missing");
	}

	public ItemStack getStack() {
		if (headId != null){
			return new ItemStack(ForgeRegistries.ITEMS.getValue(headId), 1);
		}
		else {
			ItemStack headStack = new ItemStack(Blocks.PLAYER_HEAD, 1);
			CompoundNBT nbt = new CompoundNBT();
			CompoundNBT skullOwner = new CompoundNBT();
			CompoundNBT properties = new CompoundNBT();
			ListNBT textures = new ListNBT();
			CompoundNBT texture = new CompoundNBT();
			texture.putString("Value", this.head);
			textures.add(0, texture);
			properties.put("textures", textures);
			skullOwner.putUUID("Id", this.uuid);
			skullOwner.put("Properties", properties);
			nbt.put("SkullOwner", skullOwner);
			String stackName = new StringTextComponent(this.headName).getContents();
			CompoundNBT display = new CompoundNBT();
			display.putString("Name", "{\"text\":\"" + stackName + "\", \"italic\": false}");
			nbt.put("display", display);
			headStack.setTag(nbt);
			return headStack;
		}
	}

	@Override
	public String toString() {
		return String.format("Head[mob_id: %s, nbt: %s, head: %s, uuid: %s, head_name: %s, head_id: %s, chance: %f, looting_chance: %f]", this.mobId, this.nbt, this.head, this.uuid, this.headName, this.headId, this.chance, this.lootingChance);
	}
}
