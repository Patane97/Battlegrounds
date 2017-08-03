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
import com.Patane.Battlegrounds.Messenger.ChatType;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.util.YML;

public class PlayerDataYML extends BasicYML{
	
	public PlayerDataYML(Plugin plugin) {
		super(plugin, "playerData.yml", "playerUUID");
	}
	
	@Override
	public void load(){
		Player player;
		for(String stringUUID : header.getKeys(false)){
			checkEmptyClear(stringUUID);
			player = Bukkit.getPlayer(UUID.fromString(stringUUID));
			if(Bukkit.getOnlinePlayers().contains(player))
				load(player);
		}
	}
	
	@Override
	public void save(){
		for(Arena selectedArena : Arenas.get()){
			for(String playerName : selectedArena.getPlayers()){
				try{
					save(Bukkit.getPlayerExact(playerName), false);
				} catch (NullPointerException e){
					Messenger.warning("Failed to find and save Player: " + playerName);
					e.printStackTrace();
				}
			}
		}
		config.save();
	}
	/**
	 * Saves the players data into PlayerData.yml
	 * 
	 * @param player Player to save.
	 */
	public void save(Player player, boolean save){
		setHeader(player.getUniqueId().toString());
		saveWellbeing(player, false);
		saveGameMode(player, false);
		saveLocation(player, false);
		saveInventory(player, false);
		if(save)
			config.save();
	}
	/**
	 * Load the players data that is saved either within the plugin or within PlayerData.yml
	 *  
	 * @param player Player to load.
	 */
	public void load(Player player){
		String stringUUID = player.getUniqueId().toString();
		if(isSection(stringUUID)){				
			Messenger.debug(ChatType.INFO, "Found " + stringUUID + ".");
			Wellbeing.restore(player);
			GameModes.restore(player);
			Locations.restore(player);
			Inventories.restore(player);
		}
	}
	 void saveWellbeing(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("wellbeing.health", player.getHealth());
		header.set("wellbeing.food", player.getFoodLevel());
		header.set("wellbeing.exp", player.getExp());
		header.set("wellbeing.level", player.getLevel());
		if(save) config.save();
	}
	public void saveHealth(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("wellbeing.health", player.getHealth());
		if(save) config.save();
	}
	public void saveFood(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("wellbeing.food", player.getFoodLevel());
		if(save) config.save();
	}
	public void saveExp(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("wellbeing.exp", player.getExp());
		if(save) config.save();
	}
	public void saveLevel(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("wellbeing.level", player.getLevel());
		if(save) config.save();
	}
	public void saveLocation(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("location", YML.locationFormat(player.getLocation()));
		if(save) config.save();
	}
	public void saveInventory(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		List<ItemStack> itemList = Arrays.asList(player.getInventory().getContents());
		header.set("inventory", itemList);
		if(save) config.save();
	}
	public void saveGameMode(Player player, boolean save){
		if(save) setHeader(player.getUniqueId().toString());
		header.set("gamemode", player.getGameMode().toString());
		if(save) config.save();
	}
	public Location getLocation(String stringUUID){
		if(isSection(stringUUID + ".location")){
			setHeader(stringUUID);
			Location location = YML.getLocation(header.getString("location"));
			if (location == null)
				Messenger.debug(ChatType.WARNING, "Failed to grab player's location.");
			return location;
		}
		Messenger.debug(ChatType.WARNING, "Failed to find player's location in YML.");
		return null;
	}
	public ItemStack[] getInventory(String stringUUID){
		if(isSection(stringUUID + ".inventory")){
			setHeader(stringUUID);
			@SuppressWarnings("unchecked")
			List<ItemStack> itemList = (List<ItemStack>) header.get("inventory");
			ItemStack[] inventoryContents = (ItemStack[]) itemList.toArray(new ItemStack[0]);
			if (inventoryContents == null)
				Messenger.debug(ChatType.WARNING, "Failed to grab player's inventory.");
			return inventoryContents;
		}
		Messenger.debug(ChatType.WARNING, "Failed to find player's inventory in YML.");
		return null;
	}
	public GameMode getGameMode(String stringUUID){
		if(isSection(stringUUID + ".gamemode")){
			setHeader(stringUUID);
			String gameMode = header.getString("gamemode");
			if (gameMode == null)
				Messenger.debug(ChatType.WARNING, "Failed to grab player's gamemode.");
			return GameMode.valueOf(gameMode);
		}
		Messenger.debug(ChatType.WARNING, "Failed to find player's gamemode in YML.");
		return null;
	}
	@SuppressWarnings("null")
	public double getHealth(String stringUUID){
		if(isSection(stringUUID + ".wellbeing")){
			setHeader(stringUUID + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				getSection(stringUUID).set("wellbeing", null);
			return header.getDouble("health");
		}
		Messenger.debug(ChatType.WARNING, "Failed to find player's health in YML.");
		return (Double) null;
	}
	@SuppressWarnings("null")
	public int getFoodLevel(String stringUUID){
		if(isSection(stringUUID + ".wellbeing")){
			setHeader(stringUUID + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				getSection(stringUUID).set("wellbeing", null);
			return header.getInt("food");
		}
		Messenger.debug(ChatType.WARNING, "Failed to find player's food-level in YML.");
		return (Integer) null;
	}
	@SuppressWarnings("null")
	public float getExp(String stringUUID) {
		if(isSection(stringUUID + ".wellbeing")){
			setHeader(stringUUID + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				getSection(stringUUID).set("wellbeing", null);
			return (float) header.getDouble("exp");
		}
		Messenger.debug(ChatType.WARNING, "Failed to find player's exp in YML.");
		return (Float) null;
	}
	@SuppressWarnings("null")
	public int getLevel(String stringUUID){
		if(isSection(stringUUID + ".wellbeing")){
			setHeader(stringUUID + ".wellbeing");
			if(header.getKeys(false).isEmpty())
				getSection(stringUUID).set("wellbeing", null);
			return header.getInt("level");
		}
		Messenger.debug(ChatType.WARNING, "Failed to find player's level in YML.");
		return (Integer) null;
	}
	public void deletePlayer(String stringUUID){
		getRootSection().set(stringUUID, null);
		config.save();
	}
	public void deletePlayer(String stringUUID, String type){
		if(getSection(stringUUID) != null){
			getSection(stringUUID).set(type, null);
			if(getSection(stringUUID).getKeys(false).isEmpty())
				getRootSection().set(stringUUID, null);
			config.save();
		}
	}
	public void checkWellbeing(String stringUUID) {
		setHeader(stringUUID + ".wellbeing");
		if(header.getKeys(false).isEmpty())
			getSection(stringUUID).set("wellbeing", null);
	}
}


