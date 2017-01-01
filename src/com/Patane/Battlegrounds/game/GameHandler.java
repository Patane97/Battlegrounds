package com.Patane.Battlegrounds.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.collections.*;
import com.Patane.Battlegrounds.playerData.PlayerData;
import com.Patane.Battlegrounds.util.Randoms;

public class GameHandler implements GameManager, Runnable{
//	String hostName;
//	String gameName;
	World world;
	Plugin plugin;
	ArenaHandler arena;
	RoundHandler roundHandler;
	
	GameListeners gameListener;
	
	// map of players <String name, Boolean alive>
	HashMap<String, Boolean> players = new HashMap<String, Boolean>();
	
	public GameHandler (Plugin plugin, ArenaHandler arena, GameListeners listener) {
		this.plugin			= plugin;
		this.arena 			= arena;
		this.world			= arena.getWorld();
		this.roundHandler 	= new RoundHandler(plugin, this);
		this.gameListener	= listener;
		
		listener.setGame(this);
		GameInstances.add(this);
		arena.setInGame(true);
		
	}
	
	
	public void run() {
		roundHandler.startRound();
	}
	public ArenaHandler getArena(){
		return arena;
	}
	public String getName(){
		return arena.getName();
	}
	public World getWorld(){
		return world;
	}
	public RoundHandler getRoundHandler(){
		return roundHandler;
	}
	// returns all player names in current game
	public ArrayList<String> getPlayerNames(){
		ArrayList<String> playerStrings = new ArrayList<String>();
		for(String selectedPlayer : players.keySet())
			playerStrings.add(selectedPlayer);
		return playerStrings;
	}
	// returns all player objects in current game
	public ArrayList<Player> getPlayers(){
		ArrayList<Player> playerObjects = new ArrayList<Player>();
		for(String selectedPlayer : players.keySet())
			playerObjects.add(Bukkit.getPlayerExact(selectedPlayer));
		return playerObjects;
	}
	// adds player to THIS game
	public void addPlayer(Player player){
		players.put(player.getDisplayName(), true);
		player.teleport(arena.getPlayerSpawn(Randoms.integer(0, arena.getPlayerSpawns().size()-1)));
		// make a method to refresh their items depending on what class they have selected.
	}
	public boolean playerLeave(String player, Boolean check, Boolean silent){
		return playerLeave(Bukkit.getPlayerExact(player), check, silent);
	}
	// checks if a player has been killed in a game
	public boolean playerKilled(Player player){
		if(players.containsKey(player.getDisplayName())){
			players.put(player.getDisplayName(), false);
			player.teleport(arena.getSpectatorSpawn(Randoms.integer(0, arena.getSpectatorSpawns().size()-1)));
			Messenger.gameCast(this, "&6" + player.getPlayerListName() + " has been eliminated!");
			return true;
		}
		return false;
	}
	public boolean hasPlayer(Player player) {
		if(players.containsKey(player.getDisplayName()))
			return true;
		return false;
	}
	// checks if the game should be over. Runs gameOver() if it is over
	public boolean checkGameEnd(){
		// if players does not contain a player that is alive=true
		if(!players.containsValue(true) || players.isEmpty()){
			gameOver();
			return true;
		}
		return false;
	}
	// code run when a game has ended
	public void gameOver() { // add an input to say how the game ended (people leaving, last man death, reload, etc.)
		// do other reset stuff here
		// 
		//
		////
		Messenger.gameCast(this, "All players have been eliminated!");
		// actions to be performed on each active player in the active game
		for(String selectedPlayer : players.keySet()){
			playerLeave(selectedPlayer, false, true);
		}
		roundHandler.clearMobs();
		Messenger.broadcast("A game has just finished! Type /bg join [arena name] to start a new BattleGround!");
		RoundInstances.remove(roundHandler);
		GameInstances.remove(this);
		Bukkit.getServer().getScheduler().cancelTask(roundHandler.getSpawnTaskID());
		HandlerList.unregisterAll(gameListener);
		
		arena.setInGame(false);
	}
}
