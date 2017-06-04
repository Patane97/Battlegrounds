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
			PlayerDataYML.saveLocation(stringUUID, true);
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
		if (!savedLocations.containsKey(stringUUID)){
			Messenger.info("Failed to find " + playerName + "'s location in List. Checking yml...");
			Location location = PlayerDataYML.getLocation(stringUUID);
			if(location == null){
				Messenger.warning("Failed to find " + playerName + "'s location from yml. Location Lost.");
				return false;
			}
			savedLocations.put(stringUUID, location);
		}
		try{
			player.teleport(savedLocations.remove(stringUUID));
			PlayerDataYML.deletePlayer(stringUUID, "location");
			Messenger.warning("Successfully restored " + playerName + "'s location.");
		} catch (Exception e){
			Messenger.warning("Failed to set " + playerName + "'s location.");
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
