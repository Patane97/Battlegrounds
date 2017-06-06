package com.Patane.Battlegrounds.playerData;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Messenger;

public class Wellbeing {

	private static HashMap<String, Double> savedHealth = new HashMap<String, Double>();
	private static HashMap<String, Integer> savedFoodLevel = new HashMap<String, Integer>();
	private static HashMap<String, Float> savedExp = new HashMap<String, Float>();
	private static HashMap<String, Integer> savedLevel = new HashMap<String, Integer>();
	
	public static void save(Player player){
		String stringUUID = player.getUniqueId().toString();
		if(!savedHealth.containsKey(stringUUID)){
			savedHealth.put(stringUUID, player.getHealth());
			PlayerDataYML.saveHealth(stringUUID, true);
		}
		if(!savedFoodLevel.containsKey(stringUUID)){
			savedFoodLevel.put(stringUUID, player.getFoodLevel());
			PlayerDataYML.saveFood(stringUUID, true);
		}
		if(!savedExp.containsKey(stringUUID)){
			savedExp.put(stringUUID, player.getExp());
			PlayerDataYML.saveExp(stringUUID, true);
		}
		if(!savedLevel.containsKey(stringUUID)){
			savedLevel.put(stringUUID, player.getLevel());
			PlayerDataYML.saveLevel(stringUUID, true);
		}
//		PlayerDataYML.saveWellbeing(stringUUID, true);
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
		player.setHealth(getHealth(stringUUID, playerName));
		PlayerDataYML.deletePlayer(stringUUID, "wellbeing.health");
		player.setFoodLevel(getFood(stringUUID, playerName));
		PlayerDataYML.deletePlayer(stringUUID, "wellbeing.food");
		player.setExp(getExp(stringUUID, playerName));
		PlayerDataYML.deletePlayer(stringUUID, "wellbeing.exp");
		player.setLevel(getLevel(stringUUID, playerName));
		PlayerDataYML.deletePlayer(stringUUID, "wellbeing.level");
		return true;
	}
	public static double getHealth(String stringUUID, String playerName){
		if(savedHealth.get(stringUUID) != null)
			return savedHealth.remove(stringUUID);
		double value = PlayerDataYML.getHealth(stringUUID);
		if(value >= 0)
			return value;
		Messenger.warning("Health: " + value);
		value = 20;
		Messenger.warning("Failed to find " + playerName + "'s health from yml. Setting to '" + value + "'");
		return value;
	}
	public static int getFood(String stringUUID, String playerName){
		if(savedFoodLevel.get(stringUUID) != null)
			return savedFoodLevel.remove(stringUUID);
		int value = PlayerDataYML.getFoodLevel(stringUUID);
		if(value >= 0)
			return value;
		Messenger.warning("Food: " + value);
		value = 20;
		Messenger.warning("Failed to find " + playerName + "'s food level from yml. Setting to '" + value + "'");
		return value;
	}
	public static float getExp(String stringUUID, String playerName){
		if(savedExp.get(stringUUID) != null)
			return savedExp.remove(stringUUID);
		float value = PlayerDataYML.getExp(stringUUID);
		if(value >= 0){
			return value;
		}
		Messenger.warning("Exp: " + value);
		value = 0;
		Messenger.warning("Failed to find " + playerName + "'s exp from yml. Setting to '" + value + "'");
		return value;
	}
	public static int getLevel(String stringUUID, String playerName){
		if(savedLevel.get(stringUUID) != null)
			return savedLevel.remove(stringUUID);
		int value = PlayerDataYML.getLevel(stringUUID);
		if(value >= 0){
			return value;
		}
		Messenger.warning("Level: " + value);
		value = 0;
		Messenger.warning("Failed to find " + playerName + "'s level from yml. Setting to '" + value + "'");
		return value;
	}
}
