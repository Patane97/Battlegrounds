package com.Patane.Battlegrounds.lobby;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.collections.LobbyInstances;
import com.Patane.Battlegrounds.playerData.PlayerData;
import com.Patane.Battlegrounds.util.Randoms;

public class LobbyHandler implements LobbyManager{
	Plugin plugin;
	ArenaHandler arena;
	LobbyListeners listener;
	
	ArrayList<String> playerNames = new ArrayList<String>();
	
	public LobbyHandler(Plugin plugin, ArenaHandler arena, LobbyListeners listener){
		this.plugin 	= plugin;
		this.arena 		= arena;
		this.listener 	= listener;
		
		listener.setLobby(this);
		LobbyInstances.add(this);
		arena.setInLobby(true);
	}
	public String getName() {
		return arena.getName();
	}
	public World getWorld() {
		return arena.getWorld();
	}
	public ArrayList<String> getPlayerNames() {
		return playerNames;
	}
	public ArrayList<Player> getPlayers() {
		return null;
	}
	public ArenaHandler getArena() {
		return arena;
	}
	public void addPlayer(Player player){
		PlayerData.saveData(player);
		playerNames.add(player.getDisplayName());
		player.setHealth(20);
		player.setFoodLevel(20);
		player.getInventory().clear();
		player.teleport(arena.getLobbySpawn(Randoms.integer(0, arena.getPlayerSpawns().size()-1)));
		player.setGameMode(GameMode.SURVIVAL);
	}

}
