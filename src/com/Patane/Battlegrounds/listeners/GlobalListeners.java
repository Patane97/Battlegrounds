package com.Patane.Battlegrounds.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.playerData.PlayerDataYML;

public class GlobalListeners implements Listener{
	// making playerData.yml crash-proof
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		Arenas.removePlayer(player);
		PlayerDataYML.loadAllData(player);
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		Player player = event.getPlayer();
		Arenas.removePlayer(player);
	}
}
