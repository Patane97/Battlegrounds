package com.Patane.Battlegrounds.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.collections.*;

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
		
		arena.isInGame(true);
		
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
		Messenger.gameCast(this, player.getDisplayName() + " has joined your game!");
		players.put(player.getDisplayName(), true);
		player.teleport(arena.getPlayerSpawn());
		player.setGameMode(GameMode.SURVIVAL);
		Messenger.send(player, "You have joined the game!");
	}
	// kicks player from the game with or without a message
	public boolean kickPlayer(String player, Boolean silent){
		if(players.remove(player)){
			if(!silent)
				Messenger.send(Bukkit.getPlayerExact(player), "You have been kicked from the game.");
			return true;
		}
		return false;
	}
	// checks if a player has been killed in a game
	public boolean playerKilled(Player player){
		if(players.containsKey(player.getDisplayName())){
			players.put(player.getDisplayName(), false);
			player.teleport(arena.getSpectatorSpawn());
			Messenger.gameCast(this, "&6" + player.getPlayerListName() + " has been eliminated!");
			return true;
		}
		return false;
	}
	public boolean playerLeave(Player player, Boolean check){
		// backup checking if player is in game
		if(players.remove(player.getDisplayName()) != null){
			Messenger.send(player, "You have left the game.");
			Messenger.gameCast(this, player.getPlayerListName() + " has left the current game!");
			if(check)
				checkGameEnd();
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
			kickPlayer(selectedPlayer, true);
		}
		roundHandler.clearMobs();
		Messenger.broadcast("A game has just finished! Type /bg join [arena name] to start a new BattleGround!");
		RoundInstances.remove(roundHandler);
		GameInstances.remove(this);
		
		arena.isInGame(false);
	}
}
