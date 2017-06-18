package com.Patane.Battlegrounds.playerData;

import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Messenger;

public class PlayerData {

	public static void saveData(Player player){
		GameModes.save(player);
		Locations.save(player);
		Inventories.save(player);
		Wellbeing.save(player);
		Messenger.debug(player, "Saved your Gamemode, Location, Inventori and Wellbeing!");
	}
	public static void restoreData(Player player){
		GameModes.restore(player);
		Locations.restore(player);
		Inventories.restore(player);
		Wellbeing.restore(player);
		if(PlayerDataYML.isEmpty(player.getUniqueId().toString()))
			PlayerDataYML.clearSection(player.getUniqueId().toString());
	}
}
