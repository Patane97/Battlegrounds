package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.Hashtable;

import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.game.GameHandler;

public class ActivePlayers {
	private static Hashtable<Player, GameHandler> players = new Hashtable<Player, GameHandler>();
	
	public static void add(Player player, GameHandler game){
		players.put(player, game);
	}
	public static void remove(Player player){
		players.remove(player);
	}
	public static GameHandler getGame(Player player){
		return players.get(player);
	}
	public static ArrayList<Player> getPlayers(GameHandler game){
		ArrayList<Player> tempPlayers = new ArrayList<Player>();
		for(Player selectedplayer : players.keySet()){
			if(players.get(selectedplayer).equals(game)){
				tempPlayers.add(selectedplayer);
			}
		}
		return tempPlayers;
	}
	// returns players and their respective GameHandler games
	public static Hashtable<Player, GameHandler> getHashtable(){
		return players;
	}
}
