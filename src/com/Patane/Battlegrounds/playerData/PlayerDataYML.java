package com.Patane.Battlegrounds.playerData;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
		
		if(!isRootSection()){
			createRootSection();
		} else{
			// checks and restores the data of all online players in the yml file
			header = getRootSection();
			Player player;
			for(String stringUUID : header.getKeys(false)){
				player = Bukkit.getPlayer(UUID.fromString(stringUUID));
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
	 * @param stringUUID UUID of the player to create (in string format).
	 */
	public static void checkCreatePlayer(String stringUUID){
		if(!checkSection(stringUUID)){
			createSection(stringUUID);
		}
		setHeader(stringUUID);
	}
	/**
	 * Saves the players data into PlayerData.yml
	 * 
	 * @param player Player to save.
	 */
	public static void saveAllData(Player player){
		String stringUUID = player.getUniqueId().toString();
		checkCreatePlayer(stringUUID);
		saveWellbeing(stringUUID, false);
		saveGameMode(stringUUID, false);
		saveLocation(stringUUID, false);
		saveInventory(stringUUID, false);
		playerDataConfig.save();
	}
	/**
	 * Load the players data from PlayerData.yml
	 *  
	 * @param player Player to load.
	 */
	public static void loadAllData(Player player){
		String stringUUID = player.getUniqueId().toString();
		if(checkSection(stringUUID)){
			if(checkSection(stringUUID + ".wellbeing.health")){
				double health = getHealth(stringUUID);
				if(health > 0)
					player.setHealth(health);
			}
			if(checkSection(stringUUID + ".wellbeing.food")){
				int food = getFoodLevel(stringUUID);
				if(food > 0)
					player.setFoodLevel(food);
			}
			if(checkSection("playerUUID." + stringUUID + ".gamemode"))
				player.setGameMode(getGameMode(stringUUID));
			if(checkSection("playerUUID." + stringUUID + ".location"))
				player.teleport(getLocation(stringUUID));
			if(checkSection(stringUUID + ".inventory")){
				player.getInventory().clear();
				player.getInventory().setContents(getInventory(stringUUID));
			}
			deletePlayer(player.getUniqueId().toString());
		}
	}
	public static void saveWellbeing(String stringUUID, boolean save){
		checkCreatePlayer(stringUUID);
		Player player = Bukkit.getPlayer(UUID.fromString(stringUUID));
		header.set("wellbeing.health", player.getHealth());
		header.set("wellbeing.food", player.getFoodLevel());
		header.set("wellbeing.exp", player.getExp());
		header.set("wellbeing.level", player.getLevel());
		if(save)
			playerDataConfig.save();
	}
	public static void saveLocation(String stringUUID, boolean save){
		checkCreatePlayer(stringUUID);
		Player player = Bukkit.getPlayer(UUID.fromString(stringUUID));
		header.set("location", YML.locationFormat(player.getLocation()));
		if(save)
			playerDataConfig.save();
	}
	public static void saveInventory(String stringUUID, boolean save){
		checkCreatePlayer(stringUUID);
		Player player = Bukkit.getPlayer(UUID.fromString(stringUUID));
		List<ItemStack> itemList = Arrays.asList(player.getInventory().getContents());
		header.set("inventory", itemList);
		if(save)
			playerDataConfig.save();
	}
	public static void saveGameMode(String stringUUID, boolean save){
		checkCreatePlayer(stringUUID);
		Player player = Bukkit.getPlayer(UUID.fromString(stringUUID));
		header.set("gamemode", player.getGameMode().toString());
		if(save)
			playerDataConfig.save();
	}
	public static Location getLocation(String stringUUID){
		if(checkSection(stringUUID + ".location")){
			setHeader(stringUUID);
			Location location = YML.getLocation(header.getString("location"));
			return location;
		}
		return null;
	}
	public static ItemStack[] getInventory(String stringUUID){
		if(checkSection(stringUUID + ".inventory")){
			setHeader(stringUUID);
			@SuppressWarnings("unchecked")
			List<ItemStack> itemList = (List<ItemStack>) header.get("inventory");
			ItemStack[] inventoryContents = (ItemStack[]) itemList.toArray(new ItemStack[0]);
			return inventoryContents;
		}
		return null;
	}
	public static GameMode getGameMode(String stringUUID){
		if(checkSection(stringUUID + ".gamemode")){
			setHeader(stringUUID);
			String gameMode = header.getString("gamemode");
			return GameMode.valueOf(gameMode);
		}
		return null;
	}
	public static double getHealth(String stringUUID){
		if(checkSection(stringUUID + ".wellbeing")){
			setHeader(stringUUID + ".wellbeing");
			if(header.getKeys(false).isEmpty()){
				setHeader(stringUUID);
				header.set("wellbeing", null);
				return -1;
			}
			return header.getDouble("health");
		}
		return -1;
	}
	public static int getFoodLevel(String stringUUID){
		if(checkSection(stringUUID + ".wellbeing")){
			setHeader(stringUUID + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				getSection(stringUUID).set("wellbeing", null);
			return header.getInt("food");
		}
		return -1;
	}
	public static float getExp(String stringUUID) {
		if(checkSection(stringUUID + ".wellbeing")){
			setHeader(stringUUID + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				getSection(stringUUID).set("wellbeing", null);
			return (float) header.getDouble("exp");
		}
		return -1;
	}
	public static int getLevel(String stringUUID){
		if(checkSection(stringUUID + ".wellbeing")){
			setHeader(stringUUID + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				getSection(stringUUID).set("wellbeing", null);
			return header.getInt("level");
		}
		return -1;
	}
	public static void deletePlayer(String stringUUID){
		getRootSection().set(stringUUID, null);
		playerDataConfig.save();
	}
	public static void deletePlayer(String stringUUID, String type){
		getSection(stringUUID).set(type, null);
		if(type.contains("wellbeing")){
			checkWellbeing(stringUUID);
		}
		if(getSection(stringUUID).getKeys(false).isEmpty())
			getRootSection().set(stringUUID, null);
		playerDataConfig.save();
	}
	public static void checkWellbeing(String stringUUID) {
		setHeader(stringUUID + ".wellbeing");
		if(header.getKeys(false).isEmpty())
			getSection(stringUUID).set("wellbeing", null);
	}

	private static void createRootSection() {
		playerDataConfig.createSection("playerUUID");
	}
	private static void createSection(String section) {
		playerDataConfig.createSection("playerUUID." + section);
	}
	private static void setHeader(String newHeader) {
		header = getSection(newHeader);
	}
	private static boolean isRootSection() {
		return playerDataConfig.isConfigurationSection("playerUUID");
	}
	private static boolean checkSection(String section) {
		return playerDataConfig.isConfigurationSection("playerUUID." + section);
	}
	private static ConfigurationSection getRootSection() {
		return playerDataConfig.getConfigurationSection("playerUUID");
	}
	private static ConfigurationSection getSection(String section) {
		return playerDataConfig.getConfigurationSection("playerUUID." + section);
	}
}


