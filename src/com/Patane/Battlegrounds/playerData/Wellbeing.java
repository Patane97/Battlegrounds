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
			PlayerDataYML.saveHealth(player, true);
		}
		if(!savedFoodLevel.containsKey(stringUUID)){
			savedFoodLevel.put(stringUUID, player.getFoodLevel());
			PlayerDataYML.saveFood(player, true);
		}
		if(!savedExp.containsKey(stringUUID)){
			savedExp.put(stringUUID, player.getExp());
			PlayerDataYML.saveExp(player, true);
		}
		if(!savedLevel.containsKey(stringUUID)){
			savedLevel.put(stringUUID, player.getLevel());
			PlayerDataYML.saveLevel(player, true);
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
		Boolean fullSuccess = true;
		if(!PlayerDataYML.isSection(stringUUID, "wellbeing") || PlayerDataYML.isEmpty(stringUUID, "wellbeing")){
			PlayerDataYML.clearSection(stringUUID, "wellbeing");
			return false;
		}
		try{
			player.setHealth(getHealth(stringUUID, playerName));
			PlayerDataYML.deletePlayer(stringUUID, "wellbeing.health");
			Messenger.debug("info", "Successfully restored " + playerName + "'s health.");
		} catch (Exception e){
			Messenger.debug("warning", "Failed to restore " + playerName + "'s health. Setting to 20.");
			player.setHealth(20);
			e.printStackTrace();
			fullSuccess = false;
		}
		try{
			player.setFoodLevel(getFood(stringUUID, playerName));
			PlayerDataYML.deletePlayer(stringUUID, "wellbeing.food");
			Messenger.debug("info", "Successfully restored " + playerName + "'s food level.");
		} catch (Exception e){
			Messenger.debug("warning", "Failed to restore " + playerName + "'s food level. Setting to 20.");
			player.setFoodLevel(20);
			e.printStackTrace();
			fullSuccess = false;
		}
		try{
			player.setExp(getExp(stringUUID, playerName));
			PlayerDataYML.deletePlayer(stringUUID, "wellbeing.exp");
			Messenger.debug("info", "Successfully restored " + playerName + "'s exp.");
		} catch (Exception e){
			Messenger.debug("warning", "Failed to restore " + playerName + "'s exp. Setting to 0.");
			player.setExp(0);
			e.printStackTrace();
			fullSuccess = false;
		}
		try{
			player.setLevel(getLevel(stringUUID, playerName));
			PlayerDataYML.deletePlayer(stringUUID, "wellbeing.level");
			Messenger.debug("info", "Successfully restored " + playerName + "'s level.");
		} catch (Exception e){
			Messenger.debug("warning", "Failed to restore " + playerName + "'s level. Setting to 0.");
			player.setLevel(0);
			e.printStackTrace();
			fullSuccess = false;
		}
		
		return fullSuccess;
	}
	public static double getHealth(String stringUUID, String playerName){
		if(savedHealth.get(stringUUID) != null)
			return savedHealth.remove(stringUUID);
		return PlayerDataYML.getHealth(stringUUID);
	}
	public static int getFood(String stringUUID, String playerName){
		if(savedFoodLevel.get(stringUUID) != null)
			return savedFoodLevel.remove(stringUUID);
		return PlayerDataYML.getFoodLevel(stringUUID);
	}
	public static float getExp(String stringUUID, String playerName){
		if(savedExp.get(stringUUID) != null)
			return savedExp.remove(stringUUID);
		return PlayerDataYML.getExp(stringUUID);
	}
	public static int getLevel(String stringUUID, String playerName){
		if(savedLevel.get(stringUUID) != null)
			return savedLevel.remove(stringUUID);
		return PlayerDataYML.getLevel(stringUUID);
	}
}
