package com.Patane.Battlegrounds.playerData;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Messenger;

public class Inventories {
	
	private static HashMap<String, ItemStack[]> savedInventories = new HashMap<String, ItemStack[]>();

	public static void save(Player player){
		savedInventories.put(player.getDisplayName(), player.getInventory().getContents());
		PlayerDataYML.saveInventory(player.getDisplayName(), true);
	}
	public static void save(String playerName){
		save(Bukkit.getPlayerExact(playerName));
	}
	public static boolean restore (Player player){
		Inventory playerInventory = player.getInventory();
		String playerName = player.getDisplayName();
		playerInventory.clear();
		if (!savedInventories.containsKey(playerName)){
			ItemStack[] inventoryContents = PlayerDataYML.getInventory(playerName);
			if(inventoryContents == null){
				Messenger.warning("Failed to find " + playerName + "'s inventory from yml. Inventory Lost.");
				return false;
			}
			savedInventories.put(playerName, inventoryContents);
		}
		try{
			playerInventory.setContents(savedInventories.remove(playerName));
			PlayerDataYML.deletePlayer(playerName, "inventory");
		} catch (Exception e){
			Messenger.warning("Failed to set " + playerName + "'s inventory.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean restore (String playerName){
		return restore(Bukkit.getPlayerExact(playerName));
	}
}
