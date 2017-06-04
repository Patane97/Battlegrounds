package com.Patane.Battlegrounds.arena.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
			addPlayer(Bukkit.getPlayerExact(ChatColor.stripColor(playerName)));
		
		this.defaultLocations = arena.getSpectatorSpawns();
		roundHandler.startRound();
	}
	@Override
	protected void initilizeMessage(){
		Messenger.arenaCast(arena, "&cGame Started. &cPrepare for battle!");
		// Maybe show some basic game information eg. max rounds (if any) or description??
		// Also leave room to allow for custom game-start msg
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
//	@Override
//	public boolean randomTeleport(Player player, ArrayList<Location> locations){
//		if(!arena.getSettings().SPECTATE_DEATH){
//			/*
//			 * Need to change this 'removePlayer' to a soft remove that removes the physical person
//			 * but keeps the player in the arena data for scoreboard and such. This data then gets
//			 * removed when game session ends.
//			 */
//			removePlayer(player.getDisplayName(), false);
//			return true;
//		}
//		return super.randomTeleport(player, locations);
//	}
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
		if(randomTeleport(player, arena.getGameSpawns())){
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
			playerExit(player);
			Messenger.arenaCast(arena, "&6" + player.getPlayerListName() + " has been eliminated!");
			return true;
		}
		return false;
	}
	public void finalRoundEnd(){
		for(String playerName : arena.getPlayers()){
			Player player = Bukkit.getPlayerExact(playerName);
			playerExit(player);
		}
		// cleaning up mobs
		roundHandler.stopAllTasks();
		roundHandler.clearMobs();
		if(arena.getSettings().GLOBAL_END_ANNOUNCE){
			Messenger.broadcast("&2A game in arena &a" + arena.getName() + "&2 has just finished! They made it past the final round (&a" + roundHandler.getRoundNo() + "&2)!"
							  + "&7 Type /bg join " + arena.getName() + " to start a new game at that arena!");
		}
	}
	private void playerExit(Player player){
		if(arena.hasPlayer(player))
			arena.putPlayer(player, false);
		if(arena.getSettings().SPECTATE_DEATH)
			arena.joinSpectator(player);
		else
			removePlayer(player.getDisplayName(), false);
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
		Messenger.arenaCast(arena, "&aAll players &2have been eliminated!");
		// running through players
		for(String selectedPlayer : arena.getPlayers()){
			arena.removePlayerFromList(selectedPlayer);
		}
		// cleaning up mobs
		roundHandler.stopAllTasks();
		roundHandler.clearMobs();
		if(arena.getSettings().GLOBAL_END_ANNOUNCE){
			Messenger.broadcast("&2A game in arena &a" + arena.getName() + "&2 has just finished! They made it to round &a" + roundHandler.getRoundNo() + "&2!"
							  + "&7 Type /bg join " + arena.getName() + " to start a new game at that arena!");
		}
		super.sessionOver(new Standby(plugin, arena));
	}
}
