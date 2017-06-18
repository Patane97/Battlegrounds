package com.Patane.Battlegrounds.playerData;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Messenger;

public class Locations {
	private static HashMap<String, Location> savedLocations = new HashMap<String, Location>();

	public static void save(Player player){
		String stringUUID = player.getUniqueId().toString();
		if(!savedLocations.containsKey(stringUUID)){
			savedLocations.put(stringUUID, player.getLocation());
			PlayerData.YML().saveLocation(player, true);
		}
	}
	public static void save(UUID playerUUID){
		save(Bukkit.getPlayer(playerUUID));
	}
	public static void save(String stringUUID){
		save(Bukkit.getPlayer(stringUUID));
	}
	public static boolean restore (Player player){
		String stringUUID = player.getUniqueId().toString();
		String playerName = player.getDisplayName();
		if(!PlayerData.YML().isSection(stringUUID, "location") || PlayerData.YML().isEmpty(stringUUID, "location")){
			Messenger.debug("info", "Location for " + playerName + " is empty. Removing from YML...");
			PlayerData.YML().clearSection(stringUUID, "location");
			return false;
		}
		if (!savedLocations.containsKey(stringUUID)){
			Messenger.debug("info", "Failed to find " + playerName + "'s location in List. Checking yml...");
			Location location = PlayerData.YML().getLocation(stringUUID);
			if(location == null){
				Messenger.debug("warning", "Failed to find " + playerName + "'s location from yml. Location Lost.");
				return false;
			}
			savedLocations.put(stringUUID, location);
		}
		try{
			player.teleport(savedLocations.remove(stringUUID));
			PlayerData.YML().deletePlayer(stringUUID, "location");
			Messenger.debug("info", "Successfully restored " + playerName + "'s location.");
		} catch (Exception e){
			Messenger.debug("warning", "Failed to set " + playerName + "'s location.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean restore (UUID playerUUID){
		return restore(Bukkit.getPlayer(playerUUID));
	}
	public static boolean restore (String stringUUID){
		return restore(Bukkit.getPlayer(stringUUID));
	}
}
