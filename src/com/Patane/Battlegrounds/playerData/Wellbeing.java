package com.Patane.Battlegrounds.playerData;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class Wellbeing {

	private static HashMap<String, Double> savedHealth = new HashMap<String, Double>();
	private static HashMap<String, Integer> savedFoodLevel = new HashMap<String, Integer>();
	private static HashMap<String, Float> savedExp = new HashMap<String, Float>();
	private static HashMap<String, Integer> savedLevel = new HashMap<String, Integer>();
	
	public static void save(Player player){
		savedHealth.put(player.getDisplayName(), player.getHealth());
		savedFoodLevel.put(player.getDisplayName(), player.getFoodLevel());
		savedExp.put(player.getDisplayName(), player.getExp());
		savedLevel.put(player.getDisplayName(), player.getLevel());
		PlayerDataYML.saveWellbeing(player.getDisplayName(), true);
	}
	public static boolean restore (Player player){
		String playerName = player.getDisplayName();
		player.setHealth(getHealth(playerName));
		player.setFoodLevel(getFood(playerName));
		player.setExp(getExp(playerName));
		player.setLevel(getLevel(playerName));
//		boolean Bhealth = true, Bfood = true, Bexp = true, Blevel = true;
//		if(!savedHealth.containsKey(playerName)){
//			double health = PlayerDataYML.getHealth(playerName);
//			if(health < 0){
//				Messenger.warning("Failed to find " + playerName + "'s health from yml.");
//				Bhealth = false;
//			}
//			savedHealth.put(playerName, health);
//		}
//		if(!savedFoodLevel.containsKey(playerName)){
//			int food = PlayerDataYML.getFoodLevel(playerName);
//			if(food < 0){
//				Messenger.warning("Failed to find " + playerName + "'s food from yml.");
//				Bfood = false;
//			}
//		}
//		if(!savedExp.containsKey(playerName)){
//			float exp = PlayerDataYML.getExp(playerName);
//			if(exp < 0){
//				Messenger.warning("Failed to find " + playerName + "'s exp from yml.");
//				Bexp = false;
//			}
//		}
//		if(!savedLevel.containsKey(playerName)){
//			float level = PlayerDataYML.getLevel(playerName);
//			if(level < 0){
//				Messenger.warning("Failed to find " + playerName + "'s level from yml.");
//				Blevel = false;
//			}
//		}
//		try{
//			if(Bhealth){
//				player.setHealth(savedHealth.remove(playerName));
//				PlayerDataYML.deletePlayer(playerName, "wellbeing.health");
//			}
//			if(Bfood){
//				player.setFoodLevel(savedFoodLevel.remove(playerName));
//				PlayerDataYML.deletePlayer(playerName, "wellbeing.food");
//			}
//			if(Bexp){
//				player.setExp(savedExp.remove(arg0));
//			}
//		} catch (Exception e){
//			Messenger.warning("Failed to set " + playerName + "'s health.");
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	public static double getHealth(String playerName){
		if(savedHealth.get(playerName) != null)
			return savedHealth.remove(playerName);
		double value = PlayerDataYML.getHealth(playerName);
		if(value >= 0){
			return value;
		}
		return 20;
	}
	public static int getFood(String playerName){
		if(savedFoodLevel.get(playerName) != null)
			return savedFoodLevel.remove(playerName);
		int value = PlayerDataYML.getFoodLevel(playerName);
		if(value >= 0){
			return value;
		}
		return 20;
	}
	public static float getExp(String playerName){
		if(savedExp.get(playerName) != null)
			return savedExp.remove(playerName);
		float value = PlayerDataYML.getExp(playerName);
		if(value >= 0){
			return value;
		}
		return 0;
	}
	public static int getLevel(String playerName){
		if(savedLevel.get(playerName) != null)
			return savedLevel.remove(playerName);
		int value = PlayerDataYML.getLevel(playerName);
		if(value >= 0){
			return value;
		}
		return 5;
	}
}
