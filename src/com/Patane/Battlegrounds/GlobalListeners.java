package com.Patane.Battlegrounds;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.Patane.Battlegrounds.playerData.PlayerDataYML;

public class GlobalListeners implements Listener{
	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent event){
		Player player = event.getPlayer();
		PlayerDataYML.loadAllData(player);
	}
}
