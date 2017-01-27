package com.Patane.Battlegrounds.arena.standby;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.listeners.ArenaListener;
import com.Patane.Battlegrounds.playerData.PlayerData;
import com.Patane.Battlegrounds.util.Randoms;

public class Standby implements ArenaMode{
	
	protected ArenaListener listener;
	
	protected Arena arena;
	protected Plugin plugin;
	protected ArrayList<Location> defaultLocations = new ArrayList<Location>();
	
	public Standby(){}
	public Standby(Plugin plugin, Arena arena){
		this(plugin, arena, new ArenaListener(plugin, arena));
	}
	public Standby(Plugin plugin, Arena arena, ArenaListener listener){
		this.plugin		= plugin;
		this.arena 		= arena;
		this.listener	= listener;
	}
	public Arena getArena(){
		return arena;
	}
	/**
	 * Checks if defaultLocations is empty and attempts to teleport the player
	 * 
	 * @param player to teleport
	 * @return true if player successfully gets teleported
	 */
	public boolean teleportPlayer(Player player){
		if(defaultLocations.size() > 0){
			Location location = defaultLocations.get(Randoms.integer(0, defaultLocations.size()-1));
			player.teleport(location);
			return true;
		}
		return false;
	}
	@Override
	public void addPlayer(Player player){
		PlayerData.saveData(player);
		if(teleportPlayer(player)){
			arena.putPlayer(player, false);
			player.setHealth(20);
			player.setFoodLevel(20);
			player.getInventory().clear();
			player.setGameMode(GameMode.SURVIVAL);
		} else{
			PlayerData.restoreData(player);
			Messenger.send(player, "&cFailed to teleport you! Reverting join...");
		}
	}

	@Override
	/**
	 * @return true if there are no players left in this session
	 */
	public boolean checkSessionOver(){
		if(arena.getPlayers().isEmpty())
			return true;
		return false;
	}

	@Override
	public void sessionOver(ArenaMode newMode) {
		unregister();
		arena.setMode(newMode);
	}
	@Override
	public void sessionOver() {
		for(String selectedPlayer : arena.getPlayers()){
			arena.removePlayer(selectedPlayer, false);
		}
		unregister();
		arena.setMode(new Standby(plugin, arena));
	}
	@Override
	public ArenaListener getListener() {
		return listener;
	}
	@Override
	public void unregister(){
		if(listener != null)
			listener.unregister();
	}

}
