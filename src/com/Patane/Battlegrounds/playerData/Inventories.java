package com.Patane.Battlegrounds.playerData;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.Messenger.ChatType;

public class Inventories {
	private static HashMap<String, ItemStack[]> savedInventories = new HashMap<String, ItemStack[]>();

	public static void save(Player player){
		String stringUUID = player.getUniqueId().toString();
		if(!savedInventories.containsKey(stringUUID)){
			savedInventories.put(stringUUID, player.getInventory().getContents());
			PlayerData.YML().saveInventory(player, true);
		}
	}
	public static void save(UUID playerUUID){
		save(Bukkit.getPlayer(playerUUID));
	}
	public static void save(String stringUUID){
		save(Bukkit.getPlayer(stringUUID));
	}
	public static boolean isSaved(Player player){
		if(savedInventories.containsKey(player.getUniqueId().toString()))
			return true;
		return false;
	}
	public static boolean restore (Player player){
		Inventory playerInventory = player.getInventory();
		String stringUUID = player.getUniqueId().toString();
		String playerName = player.getDisplayName();
		if(!PlayerData.YML().isSection(stringUUID, "inventory") || PlayerData.YML().isEmpty(stringUUID, "inventory")){
			Messenger.debug(ChatType.INFO, "Inventory for " + playerName + " is empty. Removing from YML...");
			PlayerData.YML().clearSection(stringUUID, "inventory");
			return false;
		}
		playerInventory.clear();
		if (!savedInventories.containsKey(stringUUID)){
			ItemStack[] inventoryContents = PlayerData.YML().getInventory(stringUUID);
			if(inventoryContents == null){
				Messenger.debug(ChatType.WARNING, "Failed to find " + playerName + "'s inventory from yml. Inventory Lost.");
				return false;
			}
			savedInventories.put(stringUUID, inventoryContents);
		}
		try{
			playerInventory.setContents(savedInventories.remove(stringUUID));
			PlayerData.YML().deletePlayer(stringUUID, "inventory");
			Messenger.debug(ChatType.INFO, "Successfully restored " + playerName + "'s inventory.");
		} catch (Exception e){
			Messenger.debug(ChatType.WARNING, "Failed to restore " + playerName + "'s inventory.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean restore (UUID playerUUID){
		return restore(Bukkit.getPlayer(playerUUID));
	}
	public static boolean restore (String stringUUID){
		return restore(Bukkit.getPlayer(stringUUID));
	}
}
