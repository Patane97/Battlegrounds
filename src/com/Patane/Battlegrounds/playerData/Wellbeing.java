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
			PlayerData.YML().saveHealth(player, true);
		}
		if(!savedFoodLevel.containsKey(stringUUID)){
			savedFoodLevel.put(stringUUID, player.getFoodLevel());
			PlayerData.YML().saveFood(player, true);
		}
		if(!savedExp.containsKey(stringUUID)){
			savedExp.put(stringUUID, player.getExp());
			PlayerData.YML().saveExp(player, true);
		}
		if(!savedLevel.containsKey(stringUUID)){
			savedLevel.put(stringUUID, player.getLevel());
			PlayerData.YML().saveLevel(player, true);
		}
//		PlayerData.YML().saveWellbeing(stringUUID, true);
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
		if(!PlayerData.YML().isSection(stringUUID, "wellbeing") || PlayerData.YML().isEmpty(stringUUID, "wellbeing")){
			Messenger.debug("info", "Wellbeing for " + playerName + " is empty. Removing from YML...");
			PlayerData.YML().clearSection(stringUUID, "wellbeing");
			return false;
		}
		try{
			double health = getHealth(stringUUID, playerName);
			if(health <= 0) throw new IllegalArgumentException();
			player.setHealth(health);
			PlayerData.YML().deletePlayer(stringUUID, "wellbeing.health");
			Messenger.debug("info", "Successfully restored " + playerName + "'s health.");
		} catch (NullPointerException|IllegalArgumentException e ){
			Messenger.debug("warning", "Failed to restore " + playerName + "'s health. Setting to 20.");
			player.setHealth(20);
			e.printStackTrace();
			fullSuccess = false;
		}
		try{
			player.setFoodLevel(getFood(stringUUID, playerName));
			PlayerData.YML().deletePlayer(stringUUID, "wellbeing.food");
			Messenger.debug("info", "Successfully restored " + playerName + "'s food level.");
		} catch (NullPointerException e){
			Messenger.debug("warning", "Failed to restore " + playerName + "'s food level. Setting to 20.");
			player.setFoodLevel(20);
			e.printStackTrace();
			fullSuccess = false;
		}
		try{
			player.setExp(getExp(stringUUID, playerName));
			PlayerData.YML().deletePlayer(stringUUID, "wellbeing.exp");
			Messenger.debug("info", "Successfully restored " + playerName + "'s exp.");
		} catch (NullPointerException e){
			Messenger.debug("warning", "Failed to restore " + playerName + "'s exp. Setting to 0.");
			player.setExp(0);
			e.printStackTrace();
			fullSuccess = false;
		}
		try{
			player.setLevel(getLevel(stringUUID, playerName));
			PlayerData.YML().deletePlayer(stringUUID, "wellbeing.level");
			Messenger.debug("info", "Successfully restored " + playerName + "'s level.");
		} catch (NullPointerException e){
			Messenger.debug("warning", "Failed to restore " + playerName + "'s level. Setting to 0.");
			player.setLevel(0);
			e.printStackTrace();
			fullSuccess = false;
		}
		if(fullSuccess)
			PlayerData.YML().deletePlayer(stringUUID, "wellbeing");
		return fullSuccess;
	}
	public static double getHealth(String stringUUID, String playerName){
		if(savedHealth.get(stringUUID) != null)
			return savedHealth.remove(stringUUID);
		return PlayerData.YML().getHealth(stringUUID);
	}
	public static int getFood(String stringUUID, String playerName){
		if(savedFoodLevel.get(stringUUID) != null)
			return savedFoodLevel.remove(stringUUID);
		return PlayerData.YML().getFoodLevel(stringUUID);
	}
	public static float getExp(String stringUUID, String playerName){
		if(savedExp.get(stringUUID) != null)
			return savedExp.remove(stringUUID);
		return PlayerData.YML().getExp(stringUUID);
	}
	public static int getLevel(String stringUUID, String playerName){
		if(savedLevel.get(stringUUID) != null)
			return savedLevel.remove(stringUUID);
		return PlayerData.YML().getLevel(stringUUID);
	}
}
