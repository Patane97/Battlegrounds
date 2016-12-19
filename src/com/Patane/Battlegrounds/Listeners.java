package com.Patane.Battlegrounds;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.Patane.Battlegrounds.collections.*;

public class Listeners implements Listener{
	@EventHandler
	public void EntityDeath(EntityDeathEvent event){
		RoundInstances.checkRoundEndAll(event.getEntity());
	}
	@EventHandler
	public void PlayerDeath(PlayerDeathEvent event){
		Player deadPlayer = event.getEntity();
		(ActivePlayers.getGame(deadPlayer)).playerDeadGameCheck(deadPlayer);
	}
}
