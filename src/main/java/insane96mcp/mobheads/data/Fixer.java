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

		int horseVariant = horse.getHorseVariant();
		if (horseVariant % 256 == 0)
			color = "white";
		else if (horseVariant % 256 == 1)
			color = "creamy";
		else if (horseVariant % 256 == 2)
			color = "chestnut";
		else if (horseVariant % 256 == 3)
			color = "brown";
		else if (horseVariant % 256 == 4)
			color = "black";
		else if (horseVariant % 256 == 5)
			color = "gray";
		else if (horseVariant % 256 == 6)
			color = "dark_brown";
		else {
			MobHeads.LOGGER.warn("Could not find horse's color");
			return;
		}

		nbt.putString("color", color);
	}


}
