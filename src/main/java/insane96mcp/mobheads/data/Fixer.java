package insane96mcp.mobheads.data;

import net.minecraft.entity.passive.horse.CoatColors;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.Locale;

public class Fixer {
	//Adds a "color" nbt tag to horses that stores their color in a less numeric way
	public static void fixHorseColorTag(EntityJoinWorldEvent event) {
		if (!(event.getEntity() instanceof HorseEntity))
			return;

		HorseEntity horse = (HorseEntity) event.getEntity();

		CompoundNBT nbt = horse.getPersistentData();

		if (nbt.contains("color"))
			return;

		CoatColors horseVariant = horse.getVariant();
		String color = horseVariant.name().toLowerCase(Locale.ROOT);
		/*if (horseVariant.equals(CoatColors.WHITE))
			color = "white";
		else if (horseVariant.equals(CoatColors.CREAMY))
			color = "creamy";
		else if (horseVariant.equals(CoatColors.CHESTNUT))
			color = "chestnut";
		else if (horseVariant.equals(CoatColors.BROWN))
			color = "brown";
		else if (horseVariant.equals(CoatColors.BLACK))
			color = "black";
		else if (horseVariant.equals(CoatColors.GRAY))
			color = "gray";
		else if (horseVariant.equals(CoatColors.DARKBROWN))
			color = "dark_brown";
		else {
			MobHeads.LOGGER.warn("Could not find horse's color");
			return;
		}*/

		nbt.putString("color", color);
	}
}
