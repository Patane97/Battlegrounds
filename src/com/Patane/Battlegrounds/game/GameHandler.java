package com.Patane.Battlegrounds.game;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.collections.*;

public class GameHandler {
	Player host;
	String gameName;
	RoundHandler rounds;
	ArrayList<Player> alive = new ArrayList<Player>();
	
	public GameHandler (Player player, String name) {
		// sets host player
		host = player; // this is a placeholder for the location for the mobs to spawn
		// sets game name
		gameName = name;
		// creates and sets a new RoundHandler as 'rounds'
		rounds = new RoundHandler(host, this); // move into match class when its made
		// adds the host to the alive list
		alive.add(host); // this will eventually be moved into a class that is initialized when the match actually starts (host replaced with all players playing the game)
		// adding this GameHandler to the GameInstances collection
		GameInstances.add(this);
		Messenger.gameCast(this, "The game has started!");
		// first round starts!
		rounds.nextRound();
	}
	// returns game name
	public String getName(){
		return gameName;
	}
	public void addPlayer(Player player){
		Messenger.gameCast(this, player.getDisplayName() + " has joined your game!");
		ActivePlayers.add(player, this);
		alive.add(player);
		Messenger.send(player, "You have joined the game!");
	}
	public void kickPlayer(Player player){
		alive.remove(player);
		ActivePlayers.remove(player);
		Messenger.send(player, "You have been kicked from the game.");
	}
	// is run when a player dies within the game. Also checks if them dying makes the game over
	public void playerDeadGameCheck(Player deadPlayer){
		if(alive.remove(deadPlayer)){
			Messenger.send(deadPlayer, "You have been eliminated!");
			Messenger.gameCast(this, deadPlayer.getPlayerListName() + " has been eliminated!");
		}
		isGameOver();
	}
	// is run when a player leaves the game. Also checks if them leaving makes the game over
	public void playerLeaveGameCheck(Player player){
		alive.remove(player);
		ActivePlayers.remove(player);
		Messenger.send(player, "You have left the game.");
		Messenger.gameCast(this, player.getPlayerListName() + " has left the current game!");
		isGameOver();
	}
	// checks if the game should be over. Runs gameOver() if it is over
	public boolean isGameOver(){
		if(alive.isEmpty()){
			gameOver();
			return true;
		}
		return false;
	}
	public void gameOver() { // add an input to say how the game ended (people leaving, last man death, reload, etc.)
		// do other reset stuff here
		// 
		//
		////
		// actions to be performed on each active player in the active game
		for(Player selectedplayer : ActivePlayers.getPlayers(this)){
			kickPlayer(selectedplayer);
		}
		rounds.clearMobs();
		Messenger.gameCast(this, "== All players have been eliminated! Game over. ==");
		Messenger.broadcast("A game has just finished! Type /bg start [game name] to start a new BattleGround!");
		RoundInstances.roundRemove(rounds);
		GameInstances.remove(this);
	}
}
