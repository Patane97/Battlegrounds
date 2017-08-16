       package com.Patane.Battlegrounds.arena.standby;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.ArenaListener;
import com.Patane.Battlegrounds.playerData.PlayerData;
import com.Patane.Battlegrounds.util.Randoms;
public class Standby implements ArenaMode{
	
	protected ArenaListener listener;
	protected String colorCode;
	protected Arena arena;
	protected Plugin plugin;
	protected ArrayList<Location> defaultLocations = new ArrayList<Location>();
	
	public Standby(){}
	public Standby(Plugin plugin, Arena arena){
		this(plugin, arena, new StandbyListener(plugin, arena));
	}
	public Standby(Plugin plugin, Arena arena, ArenaListener listener){
		this.plugin		= plugin;
		this.arena 		= arena;
		this.listener	= listener;
		this.colorCode	= "&a";
		
		initilizeMessage();
	}
	public Arena getArena(){
		return arena;
	}
	protected void initilizeMessage(){}
	/**
	 * Checks if defaultLocations is empty and attempts to teleport the player
	 * 
	 * @param player to teleport
	 * @return true if player successfully gets teleported
	 */
	public boolean randomTeleport(Player player, ArrayList<Location> locations){
		if(!locations.isEmpty()){
			Location location = locations.get(Randoms.integer(0, locations.size()-1));
			player.teleport(location);
			return true;
		}
		return false;
	}
	public void updateExp(){}
	
	public void setAllExp(float exp){
		for(String playerName : arena.getPlayers()){
			Player player = Bukkit.getPlayerExact(ChatColor.stripColor(playerName));
			player.setExp(exp);
		}
	}
	public void setAllLevel(int lvl){
		for(String playerName : arena.getPlayers()){
			Player player = Bukkit.getPlayerExact(ChatColor.stripColor(playerName));
			player.setLevel(lvl);
		}
	}
	@Override
	public boolean addPlayer(Player player){
		arena.putPlayer(player, false);
		return true;
	}
	public void playerSetupValues(Player player){
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setExp(0);
		player.setLevel(0);
		player.getInventory().clear();
		player.setGameMode(GameMode.SURVIVAL);
	}
	/**
	 * Removes player from the arena and checks if player being removed will cause arena session to end.
	 * This will also restore all player data that was stored when entering the arena.
	 */
	@Override
	public boolean removePlayer(String playerName, boolean check){
		if(arena.removePlayerFromList(playerName)){
			arena.removePlayerClass(playerName);
			PlayerData.restoreData(Bukkit.getPlayerExact(ChatColor.stripColor(playerName)));
			updateExp(); 
			if(check && checkSessionOver())
				sessionOver();
			return true;
		}
		return false;
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
		arena.setMode(newMode);
	}
	@Override
	public void sessionOver() {
		for(String selectedPlayer : arena.getPlayers()){
			removePlayer(selectedPlayer, false);
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
	@Override
	public String getColor() {
		return colorCode;
	}

}
