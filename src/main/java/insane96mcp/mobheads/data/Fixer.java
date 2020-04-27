package insane96mcp.mobheads.data;

import insane96mcp.mobheads.MobHeads;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class Fixer {

	//Adds a "color" nbt tag to horses that stores their color in a less numeric way
	public static void fixHorseColorTag(EntityJoinWorldEvent event) {
		if (!(event.getEntity() instanceof HorseEntity))
			return;

		HorseEntity horse = (HorseEntity) event.getEntity();

		CompoundNBT nbt = horse.getPersistentData();

		if (nbt.contains("color"))
			return;

		String color = "";

		if (horse.getHorseTexture().contains("hwh"))
			color = "white";
		else if (horse.getHorseTexture().contains("hcr"))
			color = "creamy";
		else if (horse.getHorseTexture().contains("hch"))
			color = "chestnut";
		else if (horse.getHorseTexture().contains("hbr"))
			color = "brown";
		else if (horse.getHorseTexture().contains("hbl"))
			color = "black";
		else if (horse.getHorseTexture().contains("hgr"))
			color = "gray";
		else if (horse.getHorseTexture().contains("hdb"))
			color = "dark_brown";
		else {
			MobHeads.LOGGER.warn("Could not find horse's color");
			return;
		}

		nbt.putString("color", color);
	}


}
