package com.Patane.Battlegrounds.playerData;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.BasicYML;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.util.YML;

public class PlayerDataYML extends BasicYML{
	
	public static void load(Plugin battlegrounds){
		loadYML(battlegrounds, "playerData.yml", "playerUUID");
		Player player;
		for(String stringUUID : header.getKeys(false)){
			checkEmptyClear(stringUUID);
			player = Bukkit.getPlayer(UUID.fromString(stringUUID));
			if(Bukkit.getOnlinePlayers().contains(player))
				loadAllData(player);
		}
		config.save();
	}
	/**
	 * Saves the players data into PlayerData.yml
	 * 
	 * @param player Player to save.
	 */
	public static void saveAllData(Player player){
		setHeader(player.getUniqueId().toString());
		saveWellbeing(player, false);
		saveGameMode(player, false);
		saveLocation(player, false);
		saveInventory(player, false);
		config.save();
	}
	/**
	 * Load the players data that is saved either within the plugin or within PlayerData.yml
	 *  
	 * @param player Player to load.
	 */
	public static void loadAllData(Player player){
		String stringUUID = player.getUniqueId().toString();
		if(isSection(stringUUID)){				
			Messenger.debug("info", "Found " + stringUUID + ".");
			Wellbeing.restore(player);
			GameModes.restore(player);
			Locations.restore(player);
			Inventories.restore(player);
		}
	}
	 static void saveWellbeing(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("wellbeing.health", player.getHealth());
		header.set("wellbeing.food", player.getFoodLevel());
		header.set("wellbeing.exp", player.getExp());
		header.set("wellbeing.level", player.getLevel());
		if(save) config.save();
	}
	public static void saveHealth(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("wellbeing.health", player.getHealth());
		if(save) config.save();
	}
	public static void saveFood(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("wellbeing.food", player.getFoodLevel());
		if(save) config.save();
	}
	public static void saveExp(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("wellbeing.exp", player.getExp());
		if(save) config.save();
	}
	public static void saveLevel(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("wellbeing.level", player.getLevel());
		if(save) config.save();
	}
	public static void saveLocation(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("location", YML.locationFormat(player.getLocation()));
		if(save) config.save();
	}
	public static void saveInventory(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		List<ItemStack> itemList = Arrays.asList(player.getInventory().getContents());
		header.set("inventory", itemList);
		if(save) config.save();
	}
	public static void saveGameMode(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("gamemode", player.getGameMode().toString());
		if(save) config.save();
	}
	public static Location getLocation(String stringUUID){
		if(isSection(stringUUID + ".location")){
			setHeader(stringUUID);
			Location location = YML.getLocation(header.getString("location"));
			return location;
		}
		return null;
	}
	public static ItemStack[] getInventory(String stringUUID){
		if(isSection(stringUUID + ".inventory")){
			setHeader(stringUUID);
			@SuppressWarnings("unchecked")
			List<ItemStack> itemList = (List<ItemStack>) header.get("inventory");
			ItemStack[] inventoryContents = (ItemStack[]) itemList.toArray(new ItemStack[0]);
			return inventoryContents;
		}
		return null;
	}
	public static GameMode getGameMode(String stringUUID){
		if(isSection(stringUUID + ".gamemode")){
			setHeader(stringUUID);
			String gameMode = header.getString("gamemode");
			return GameMode.valueOf(gameMode);
		}
		return null;
	}
	@SuppressWarnings("null")
	public static double getHealth(String stringUUID){
		if(isSection(stringUUID + ".wellbeing")){
			setHeader(stringUUID + ".wellbeing");
			if(header.getKeys(false).isEmpty()){
				setHeader(stringUUID);
				header.set("wellbeing", null);
				return -1;
			}
			return header.getDouble("health");
		}
		return (Double) null;
	}
	@SuppressWarnings("null")
	public static int getFoodLevel(String stringUUID){
		if(isSection(stringUUID + ".wellbeing")){
			setHeader(stringUUID + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				getSection(stringUUID).set("wellbeing", null);
			return header.getInt("food");
		}
		return (Integer) null;
	}
	@SuppressWarnings("null")
	public static float getExp(String stringUUID) {
		if(isSection(stringUUID + ".wellbeing")){
			setHeader(stringUUID + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				getSection(stringUUID).set("wellbeing", null);
			return (float) header.getDouble("exp");
		}
		return (Float) null;
	}
	@SuppressWarnings("null")
	public static int getLevel(String stringUUID){
		if(isSection(stringUUID + ".wellbeing")){
			setHeader(stringUUID + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				getSection(stringUUID).set("wellbeing", null);
			return header.getInt("level");
		}
		return (Integer) null;
	}
	public static void deletePlayer(String stringUUID){
		getRootSection().set(stringUUID, null);
		config.save();
	}
	public static void deletePlayer(String stringUUID, String type){
		if(getSection(stringUUID) != null){
			getSection(stringUUID).set(type, null);
			if(getSection(stringUUID).getKeys(false).isEmpty())
				getRootSection().set(stringUUID, null);
			config.save();
		}
	}
	public static void checkWellbeing(String stringUUID) {
		setHeader(stringUUID + ".wellbeing");
		if(header.getKeys(false).isEmpty())
			getSection(stringUUID).set("wellbeing", null);
	}
}


