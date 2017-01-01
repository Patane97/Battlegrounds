package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.game.GameHandler;
import com.Patane.Battlegrounds.lobby.LobbyHandler;

public class ActivePlayers {
	public static ArrayList<String> get(){
		ArrayList<String> players = new ArrayList<String>();
		for(GameHandler selectedGame : GameInstances.get()){
			players.addAll(selectedGame.getPlayerNames());
		}
		return players;
	}
	public static HashMap<String, ArenaHandler> getHash(){
		HashMap<String, ArenaHandler> players = new HashMap<String, ArenaHandler>();
		for(ArenaHandler selectedArena : Arenas.get()){
			for(String selectedPlayer : selectedArena.getPlayers())
				players.put(selectedPlayer, selectedArena);
		}
		return players;
	}
	public static GameHandler getGame(String player){
		ArenaHandler arena = getHash().get(player);
		return GameInstances.getGame(arena);
	}
	public static GameHandler getGame(Player player){
		return getGame(player.getDisplayName());
	}
	public static LobbyHandler getLobby(String player) {
		ArenaHandler arena = getHash().get(player);
		return LobbyInstances.getLobby(arena);
	}
	public static LobbyHandler getLobby(Player player){
		return getLobby(player.getDisplayName());
	}
}
