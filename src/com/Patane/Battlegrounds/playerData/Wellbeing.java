package com.Patane.Battlegrounds.playerData;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Messenger;

public class Wellbeing {

	private static HashMap<String, Double> savedHealth = new HashMap<String, Double>();
	private static HashMap<String, Integer> savedFoodLevel = new HashMap<String, Integer>();
	
	public static void save(Player player){
		savedHealth.put(player.getDisplayName(), player.getHealth());
		savedFoodLevel.put(player.getDisplayName(), player.getFoodLevel());
		PlayerDataYML.saveWellbeing(player.getDisplayName(), true);
	}
	public static boolean restore (Player player){
		String playerName = player.getDisplayName();
		boolean Bhealth = true, Bfood = true;
		if(!savedHealth.containsKey(playerName)){
			double health = PlayerDataYML.getHealth(playerName);
			if(health < 0){
				Messenger.warning("Failed to find " + playerName + "'s health from yml.");
				Bhealth = false;
			}
			savedHealth.put(playerName, health);
		}
		if(!savedFoodLevel.containsKey(playerName)){
			int food = PlayerDataYML.getFoodLevel(playerName);
			if(food < 0){
				Messenger.warning("Failed to find " + playerName + "'s food from yml.");
				Bfood = false;
			}
		}
		try{
			if(Bhealth){
				player.setHealth(savedHealth.remove(playerName));
				PlayerDataYML.deletePlayer(playerName, "wellbeing.health");
			}
			if(Bfood){
				player.setFoodLevel(savedFoodLevel.remove(playerName));
				PlayerDataYML.deletePlayer(playerName, "wellbeing.food");
			}
		} catch (Exception e){
			Messenger.warning("Failed to set " + playerName + "'s health.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
