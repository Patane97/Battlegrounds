package com.Patane.Battlegrounds.playerData;

import org.bukkit.entity.Player;

public class PlayerData {

	public static void saveData(Player player){
		GameModes.save(player);
		Locations.save(player);
		Inventories.save(player);
		Wellbeing.save(player);
	}
	public static void restoreData(Player player){
		GameModes.restore(player);
		Locations.restore(player);
		Inventories.restore(player);
		Wellbeing.restore(player);
	}
}
