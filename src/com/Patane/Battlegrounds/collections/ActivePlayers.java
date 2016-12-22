package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.game.GameHandler;

public class ActivePlayers {
	public static ArrayList<String> get(){
		ArrayList<String> players = new ArrayList<String>();
		for(GameHandler selectedGame : GameInstances.get()){
			players.addAll(selectedGame.getPlayerNames());
		}
		return players;
	}
	public static HashMap<String, GameHandler> getHash(){
		HashMap<String, GameHandler> players = new HashMap<String, GameHandler>();
		for(GameHandler selectedGame : GameInstances.get()){
			for(String selectedPlayer : selectedGame.getPlayerNames())
				players.put(selectedPlayer, selectedGame);
		}
		return players;
	}
	public static GameHandler getGame(Player player){
		return getHash().get(player.getDisplayName());
	}
	public static GameHandler getGame(String player){
		return getHash().get(player);
	}
}
