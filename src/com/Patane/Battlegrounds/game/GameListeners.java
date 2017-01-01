package com.Patane.Battlegrounds.game;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class GameListeners implements Listener{
	GameHandler gameHandler;
	Plugin plugin;
	
	public GameListeners(Plugin plugin){
		this.plugin	= plugin;
	}
	
	public void setGame(GameHandler game){
		this.gameHandler = game;
	}

	@EventHandler
	public void onMonsterCombust(EntityCombustEvent event){
		if(gameHandler.getRoundHandler().hasCreature((Creature) event.getEntity())){
			if(!(event instanceof EntityCombustByBlockEvent || event instanceof EntityCombustByEntityEvent))
				event.setCancelled(true);
		}
	}
	@EventHandler
	public void onCreatureDeath(EntityDeathEvent event){
		if(event.getEntity() instanceof Creature){
			if(gameHandler.getRoundHandler().creatureKilled((Creature) event.getEntity())){
				gameHandler.getRoundHandler().checkRoundEnd();
				event.getDrops().clear();
			}
		}
	}
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			final Player player = (Player) event.getEntity();
			if(event.getDamage() > player.getHealth()){
				if(gameHandler.playerKilled(player)){
					event.setCancelled(true);
					player.setHealth(20);
					gameHandler.checkGameEnd();
				}
			}
		}
	}
	@EventHandler
	public void onFoodLevelChange (FoodLevelChangeEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(gameHandler.hasPlayer(player))
				event.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		gameHandler.playerLeave(event.getPlayer().getDisplayName(), true, false);
	}
}
