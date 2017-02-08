package com.Patane.Battlegrounds.arena.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.standby.Standby;

public class Game extends Standby{
	
	boolean spawningCreature;
	
	RoundHandler roundHandler;
	
	public Game (Plugin plugin, Arena arena) {
		super(plugin, arena, null);

		this.listener		= new GameListeners(plugin, arena, this);
		this.roundHandler 	= new RoundHandler(plugin, this);
		this.colorCode	= "&e";
		this.defaultLocations = arena.getGameSpawns();
		
		for(String playerName : arena.getPlayers())
			addPlayer(Bukkit.getPlayerExact(playerName));
		
		this.defaultLocations = arena.getSpectatorSpawns();
		Messenger.arenaCast(arena, "Prepare to begin the battle!");
		roundHandler.startRound();
	}
	
	public RoundHandler getRoundHandler(){
		return roundHandler;
	}
	public void setSpawning(boolean spawning){
		this.spawningCreature = spawning;
	}
	public boolean getSpawning(){
		return spawningCreature;
	}
	@Override
	public void updateExp(){
		setAllLevel(roundHandler.getRoundNo());
		if(roundHandler.getTotalMobs() == 0)
			setAllExp(0);
		else
			setAllExp((float)(roundHandler.getTotalMobs() 
					- roundHandler.getAmountMobs())/roundHandler.getTotalMobs());
	}
	@Override
	public boolean addPlayer(Player player){
		if(teleportPlayer(player)){
			updateExp();
			return true;
		}
		return false;
	}
	/**
	 * @param player to check and kill
	 * @return true if player was killed in HashMap
	 */
	public boolean playerKilled(Player player){
		if(arena.getPlayers().contains(player.getDisplayName())){
			arena.putPlayer(player, false);
			teleportPlayer(player);
			Messenger.arenaCast(arena, "&6" + player.getPlayerListName() + " has been eliminated!");
			return true;
		}
		return false;
	}
	@Override
	public boolean checkSessionOver(){
		// if players does not contain a player that is alive=true
		if(!arena.anyPlayers(true) || arena.getPlayers().isEmpty())
			return true;
		return false;
	}
	@Override
	public void sessionOver() {
		Messenger.arenaCast(arena, "All players have been eliminated!");
		// running through players
		for(String selectedPlayer : arena.getPlayers()){
			removePlayer(selectedPlayer, false);
		}
		// cleaning up mobs
		roundHandler.stopAllTasks();
		roundHandler.clearMobs();

		Messenger.broadcast("A game has just finished! Type /bg join [arena name] to start a new BattleGround!");

		super.sessionOver(new Standby(plugin, arena));
	}
}
