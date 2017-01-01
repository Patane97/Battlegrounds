package com.Patane.Battlegrounds.playerData;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Messenger;

public class Locations {
	private static HashMap<String, Location> savedLocations = new HashMap<String, Location>();

	public static void save(Player player){
		savedLocations.put(player.getDisplayName(), player.getLocation());
		PlayerDataYML.saveLocation(player.getDisplayName(), true);
	}
	public static void save(String playerName){
		save(Bukkit.getPlayerExact(playerName));
	}
	public static boolean restore (Player player){
		String playerName = player.getDisplayName();
		if (!savedLocations.containsKey(playerName)){
			Messenger.info("Failed to find " + playerName + "'s location in List. Checking yml...");
			Location location = PlayerDataYML.getLocation(playerName);
			if(location == null){
				Messenger.warning("Failed to find " + playerName + "'s location from yml. Location Lost.");
				return false;
			}
			savedLocations.put(playerName, location);
		}
		try{
			player.teleport(savedLocations.remove(playerName));
			PlayerDataYML.deletePlayer(playerName, "location");
			Messenger.warning("Successfully restored " + playerName + "'s location.");
		} catch (Exception e){
			Messenger.warning("Failed to set " + playerName + "'s location.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean restore (String playerName){
		return restore(Bukkit.getPlayerExact(playerName));
	}
}
