package com.Patane.Battlegrounds.playerData;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.Messenger.ChatType;

public class GameModes {
	private static HashMap<String, GameMode> savedGameModes = new HashMap<String, GameMode>();

	public static void save(Player player){
		String stringUUID = player.getUniqueId().toString();
		if(!savedGameModes.containsKey(stringUUID)){
			savedGameModes.put(stringUUID, player.getGameMode());
			PlayerData.YML().saveGameMode(player, true);
		}
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
		if(!PlayerData.YML().isSection(stringUUID, "gamemode") || PlayerData.YML().isEmpty(stringUUID, "gamemode")){
			Messenger.debug(ChatType.INFO, "Gamemode for " + playerName + " is empty. Removing from YML...");
			PlayerData.YML().clearSection(stringUUID, "gamemode");
			return false;
		}
		if (!savedGameModes.containsKey(stringUUID)){
			Messenger.debug(ChatType.INFO, "Failed to find " + playerName + "'s game mode in List. Checking yml...");
			GameMode gameMode = PlayerData.YML().getGameMode(stringUUID);
			if(gameMode == null){
				Messenger.debug(ChatType.WARNING, "Failed to find " + playerName + "'s game mode from yml. Game mode Lost.");
				return false;
			}
			savedGameModes.put(stringUUID, gameMode);
		}
		try{
			player.setGameMode(savedGameModes.remove(stringUUID));
			PlayerData.YML().deletePlayer(stringUUID, "gamemode");
			Messenger.debug(ChatType.INFO, "Successfully restored " + playerName + "'s game mode.");
		} catch (Exception e){
			Messenger.debug(ChatType.WARNING, "Failed to set " + playerName + "'s game mode.");
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
