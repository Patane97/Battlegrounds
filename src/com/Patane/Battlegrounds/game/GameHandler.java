package com.Patane.Battlegrounds.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.collections.*;

public class GameHandler implements GameManager{
	String hostName;
	String gameName;
	World world;
	RoundHandler roundHandler;
	//ArrayList<String> alivePlayers = new ArrayList<Player>();
	// map of players <String name, Boolean alive>
	HashMap<String, Boolean> players = new HashMap<String, Boolean>();
	
	public GameHandler (Player player, String name) {
		this.hostName 		= player.getDisplayName();
		this.gameName 		= name;
		this.roundHandler 	= new RoundHandler(hostName, this); // move into match class when its made
		this.world 			= player.getWorld();
		
		players.put(hostName, true);
		GameInstances.add(this);
		roundHandler.startRound();
	}
	
	public String getName(){
		return gameName;
	}
	public World getWorld(){
		return world;
	}
	public GameHandler getGameHandler(){
		return this;
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
		Messenger.send(player, "You have joined the game!");
	}
	// kicks player from the game with or without a message
	public void kickPlayer(Player player, Boolean silent){
		players.remove(player.getDisplayName());
		if(!silent)
			Messenger.send(player, "You have been kicked from the game.");
	}
	// checks if a player has been killed in a game
	public boolean playerKilled(Player player){
		if(players.containsKey(player.getDisplayName())){
			players.put(player.getDisplayName(), false);
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
			kickPlayer(Bukkit.getPlayerExact(selectedPlayer), true);
		}
		roundHandler.clearMobs();
		Messenger.broadcast("A game has just finished! Type /bg start [game name] to start a new BattleGround!");
		RoundInstances.remove(roundHandler);
		GameInstances.remove(this);
	}
}
