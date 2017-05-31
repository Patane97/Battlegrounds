package com.Patane.Battlegrounds.playerData;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Messenger;

public class GameModes {
	private static HashMap<UUID, GameMode> savedGameModes = new HashMap<UUID, GameMode>();

	public static void save(Player player){
		savedGameModes.put(player.getUniqueId(), player.getGameMode());
		PlayerDataYML.saveGameMode(player.getDisplayName(), true);
	}
	public static void save(String playerName){
		save(Bukkit.getPlayerExact(playerName));
	}
	public static boolean restore (Player player){
		UUID playerUUID = player.getUniqueId();
		String playerName = player.getDisplayName();
		if (!savedGameModes.containsKey(playerUUID)){
			Messenger.info("Failed to find " + playerName + "'s game mode in List. Checking yml...");
			GameMode gameMode = PlayerDataYML.getGameMode(playerUUID);
			if(gameMode == null){
				Messenger.warning("Failed to find " + playerName + "'s game mode from yml. Game mode Lost.");
				return false;
			}
			savedGameModes.put(playerUUID, gameMode);
		}
		try{
			player.setGameMode(savedGameModes.remove(playerUUID));
			PlayerDataYML.deletePlayer(playerUUID, "gamemode");
			Messenger.warning("Successfully restored " + playerName + "'s game mode.");
		} catch (Exception e){
			Messenger.warning("Failed to set " + playerName + "'s game mode.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean restore (String playerName){
		return restore(Bukkit.getPlayerExact(playerName));
	}
}
