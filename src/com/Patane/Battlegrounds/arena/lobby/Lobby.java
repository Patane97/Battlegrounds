package com.Patane.Battlegrounds.arena.lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.game.Game;
import com.Patane.Battlegrounds.arena.standby.Standby;
import com.Patane.Battlegrounds.playerData.PlayerData;

public class Lobby extends Standby{
	Player creator;
	
	public Lobby(Plugin plugin, Arena arena){
		super(plugin, arena, null);
		
		this.listener			= new LobbyListeners(plugin, arena, this);
		this.defaultLocations 	= arena.getLobbySpawns();
		this.creator			= Bukkit.getPlayerExact(ChatColor.stripColor(arena.getPlayers().get(0)));
		addPlayer(creator);
	}
	@Override
	protected void initilizeMessage(){
		if(arena.getSettings().GLOBAL_NEW_ANNOUNCE){
			Messenger.broadcast(arena.getPlayers().get(0) + "&a has started a new Battlegrounds lobby in arena &7" + arena.getName() + "&a."
							+ "\n&a Type &7/bg join " + arena.getName() + "&a to join them!");
		}
	}
	public boolean checkStartGame(){
		if(!arena.anyPlayers(false))
			return true;
		return false;
	}
	public void startGame(){
		arena.setMode(new Game(plugin, arena));
	}
	@Override
	public void updateExp(){
		setAllLevel(arena.getPlayers().size());
		setAllExp((float)arena.howManyPlayers(true)/arena.getPlayers().size());
	}
	/**
	 * Sets up and adds a new player to the arena and Lobby as well as saving their data.
	 * If they fail to teleport, their data is restored and given an appropriate error msg.
	 * 
	 * As for player who starts the lobby, they will be removed from the arena player list if
	 * they fail to teleport. If they succeed teleport, they will not be added to arena player
	 * list as they are already there (from join command).
	 * 
	 * @param player
	 * @return
	 */
	@Override
	public boolean addPlayer(Player player){
		PlayerData.saveData(player);
		if(randomTeleport(player, arena.getLobbySpawns())){
			if(!player.equals(creator))
				arena.putPlayer(player, false);
			playerSetupValues(player);
			return true;
		} else{
			PlayerData.restoreData(player);
			if(player.equals(creator))
				arena.removePlayerFromList(player.getDisplayName());
			Messenger.send(player, "&cFailed to teleport you! Reverting join...");
			return false;
		}
	}
	@Override
	public void playerSetupValues(Player player){
		super.playerSetupValues(player);
		updateExp();
	}
	public void toggleReady(Player player) {
		if(!arena.playerHasClass(player)){
			Messenger.send(player, "&cYou must select a class before being ready!");
			return;
		}
		arena.putPlayer(player, !arena.getPlayerStatus(player));
		if(arena.getPlayerStatus(player))
			Messenger.arenaCast(arena, player.getDisplayName() + " &ais ready!");
		else
			Messenger.arenaCast(arena, player.getDisplayName() + " &7is not ready!");
		updateExp();
		if(checkStartGame())
			startGame();
	}
}
