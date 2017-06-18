package com.Patane.Battlegrounds.playerData;

import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Messenger;

public class PlayerData {
	/**
	 * ******************* STATIC YML SECTION *******************
	 */
	private static PlayerDataYML yml;

	public static void setYML(PlayerDataYML yml){
		PlayerData.yml = yml;
	}
	public static PlayerDataYML YML(){
		return PlayerData.yml;
	}
	/**
	 * **********************************************************
	 */
	public static void saveData(Player player){
		GameModes.save(player);
		Locations.save(player);
		Inventories.save(player);
		Wellbeing.save(player);
		Messenger.debug(player, "Saved your Gamemode, Location, Inventory and Wellbeing!");
	}
	public static void restoreData(Player player){
		GameModes.restore(player);
		Locations.restore(player);
		Inventories.restore(player);
		Wellbeing.restore(player);
		if(PlayerData.YML().isEmpty(player.getUniqueId().toString()))
			PlayerData.YML().clearSection(player.getUniqueId().toString());
		Messenger.debug(player, "Restored your Gamemode, Location, Inventory and Wellbeing!");
	}
}
