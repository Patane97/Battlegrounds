package com.Patane.Battlegrounds.playerData;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.util.Config;
import com.Patane.Battlegrounds.util.YML;

public class PlayerDataYML {
	static Plugin plugin;

	static Config playerDataConfig;
	static ConfigurationSection header;
	
	public static void load(Plugin Battlegrounds){
		plugin = Battlegrounds;
		playerDataConfig = new Config(plugin, "playerData.yml");
		
		if(!playerDataConfig.isConfigurationSection("player")){
			playerDataConfig.createSection("player");
		} else{
			// checks and restores the data of all online players in the yml file
			header = playerDataConfig.getConfigurationSection("player");
			Player player;
			for(String playerName : header.getKeys(false)){
				player = Bukkit.getPlayerExact(playerName);
				if(Bukkit.getOnlinePlayers().contains(player)){
					loadAllData(player);
				}
			}
		}
		playerDataConfig.save();
	}
	/**
	 * Checks if the player is in the playerData.yml and if not, creates them.
	 * Then sets header to their section in yml.
	 * 
	 * @param playerName Name of the player to create.
	 */
	public static void checkCreatePlayer(String playerName){
		if(!playerDataConfig.isConfigurationSection("player." + playerName)){
			playerDataConfig.createSection("player." + playerName);
		}
		header = playerDataConfig.getConfigurationSection("player." + playerName);
	}
	/**
	 * Saves the players data into PlayerData.yml
	 * 
	 * @param player Player to save.
	 */
	public static void saveAllData(Player player){
		String playerName = player.getDisplayName();
		checkCreatePlayer(playerName);
		saveWellbeing(playerName, false);
		saveGameMode(playerName, false);
		saveLocation(playerName, false);
		saveInventory(playerName, false);
		playerDataConfig.save();
	}
	/**
	 * Load the players data from PlayerData.yml
	 *  
	 * @param player Player to load.
	 */
	public static void loadAllData(Player player){
		String playerName = player.getDisplayName();
		if(playerDataConfig.isConfigurationSection("player." + playerName)){
			if(playerDataConfig.isConfigurationSection("player." + playerName + ".wellbeing.health")){
				double health = getHealth(playerName);
				if(health > 0)
					player.setHealth(health);
			}
			if(playerDataConfig.isConfigurationSection("player." + playerName + ".wellbeing.food")){
				int food = getFoodLevel(playerName);
				if(food > 0)
					player.setFoodLevel(food);
			}
			if(playerDataConfig.isConfigurationSection("player." + playerName + ".gamemode"))
				player.setGameMode(getGameMode(playerName));
			if(playerDataConfig.isConfigurationSection("player." + playerName + ".location"))
				player.teleport(getLocation(playerName));
			if(playerDataConfig.isConfigurationSection("player." + playerName + ".inventory")){
				player.getInventory().clear();
				player.getInventory().setContents(getInventory(playerName));
			}
			deletePlayer(player.getDisplayName());
		}
	}
	public static void saveWellbeing(String playerName, boolean save){
		Player player = Bukkit.getPlayerExact(playerName);
		header.set("wellbeing.health", player.getHealth());
		header.set("wellbeing.food", player.getFoodLevel());
		header.set("wellbeing.exp", player.getExp());
		header.set("wellbeing.level", player.getLevel());
		if(save)
			playerDataConfig.save();
	}
	public static void saveLocation(String playerName, boolean save){
		checkCreatePlayer(playerName);
		Player player = Bukkit.getPlayerExact(playerName);
		header.set("location", YML.locationFormat(player.getLocation()));
		if(save)
			playerDataConfig.save();
	}
	public static void saveInventory(String playerName, boolean save){
		checkCreatePlayer(playerName);
		Player player = Bukkit.getPlayerExact(playerName);
		List<ItemStack> itemList = Arrays.asList(player.getInventory().getContents());
		header.set("inventory", itemList);
		if(save)
			playerDataConfig.save();
	}
	public static void saveGameMode(String playerName, boolean save){
		checkCreatePlayer(playerName);
		Player player = Bukkit.getPlayerExact(playerName);
		header.set("gamemode", player.getGameMode().toString());
		if(save)
			playerDataConfig.save();
	}
	public static Location getLocation(String playerName){
		if(playerDataConfig.isConfigurationSection("player." + playerName)){
			header = playerDataConfig.getConfigurationSection("player." + playerName);
			Location location = YML.getLocation(header.getString("location"));
			return location;
		}
		return null;
	}
	public static ItemStack[] getInventory(String playerName){
		if(playerDataConfig.isConfigurationSection("player." + playerName)){
			header = playerDataConfig.getConfigurationSection("player." + playerName);
			@SuppressWarnings("unchecked")
			List<ItemStack> itemList = (List<ItemStack>) header.get("inventory");
			ItemStack[] inventoryContents = (ItemStack[]) itemList.toArray(new ItemStack[0]);
			return inventoryContents;
		}
		return null;
	}
	public static GameMode getGameMode(String playerName){
		if(playerDataConfig.isConfigurationSection("player." + playerName)){
			header = playerDataConfig.getConfigurationSection("player." + playerName);
			String gameMode = header.getString("gamemode");
			return GameMode.valueOf(gameMode);
		}
		return null;
	}
	public static double getHealth(String playerName){
		if(playerDataConfig.isConfigurationSection("player." + playerName)){
			header = playerDataConfig.getConfigurationSection("player." + playerName + ".wellbeing");
			if(header.getKeys(false).isEmpty()){
				header = playerDataConfig.getConfigurationSection("player." + playerName);
				header.set("wellbeing", null);
				return -1;
			}
			return header.getDouble("health");
		}
		return -1;
	}
	public static int getFoodLevel(String playerName){
		if(playerDataConfig.isConfigurationSection("player." + playerName)){
			header = playerDataConfig.getConfigurationSection("player." + playerName + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				playerDataConfig.getConfigurationSection("player." + playerName).set("wellbeing", null);
			return header.getInt("food");
		}
		return -1;
	}
	public static float getExp(String playerName) {
		if(playerDataConfig.isConfigurationSection("player." + playerName)){
			header = playerDataConfig.getConfigurationSection("player." + playerName + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				playerDataConfig.getConfigurationSection("player." + playerName).set("wellbeing", null);
			return (float) header.getDouble("exp");
		}
		return -1;
	}
	public static int getLevel(String playerName){
		if(playerDataConfig.isConfigurationSection("player." + playerName)){
			header = playerDataConfig.getConfigurationSection("player." + playerName + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				playerDataConfig.getConfigurationSection("player." + playerName).set("wellbeing", null);
			return header.getInt("level");
		}
		return -1;
	}
	public static void deletePlayer(String playerName){
		playerDataConfig.getConfigurationSection("player").set(playerName, null);
		playerDataConfig.save();
	}
	public static void deletePlayer(String playerName, String type){
		playerDataConfig.getConfigurationSection("player." + playerName).set(type, null);
		if(type.contains("wellbeing")){
			checkWellbeing(playerName);
		}
		if(playerDataConfig.getConfigurationSection("player." + playerName).getKeys(false).isEmpty())
			playerDataConfig.getConfigurationSection("player").set(playerName, null);
		playerDataConfig.save();
	}
	public static void checkWellbeing(String playerName) {
		header = playerDataConfig.getConfigurationSection("player." + playerName + ".wellbeing");
		if(header.getKeys(false).isEmpty())
			playerDataConfig.getConfigurationSection("player." + playerName).set("wellbeing", null);
	}
}

