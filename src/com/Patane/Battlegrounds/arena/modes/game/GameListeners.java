package com.Patane.Battlegrounds.arena.modes.game;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.listeners.ArenaListener;

public class GameListeners extends ArenaListener{
	Game game;
	public GameListeners(Plugin plugin, Arena arena, Game game){
		super(plugin, arena);
		this.game = game;
	}
	@EventHandler
	public void onMonsterCombust(EntityCombustEvent event){
		if(game.getRoundHandler().hasCreature((Creature) event.getEntity())){
			if(!(event instanceof EntityCombustByBlockEvent || event instanceof EntityCombustByEntityEvent))
				event.setCancelled(true);
		}
	}
	@EventHandler
	public void onCreatureDeath(EntityDeathEvent event){
		if(event.getEntity() instanceof Creature){
			if(game.getRoundHandler().creatureKilled((Creature) event.getEntity())){
				game.getRoundHandler().checkRoundEnd();
				event.getDrops().clear();
			}
		}
	}
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			final Player player = (Player) event.getEntity();
			if(event.getDamage() > player.getHealth()){
				if(game.playerKilled(player)){
					event.setCancelled(true);
					player.setHealth(20);
					if(game.checkSessionOver())
						game.sessionOver();
				}
			}
		}
	}
	@EventHandler
	public void onFoodLevelChange (FoodLevelChangeEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(arena.hasPlayer(player))
				event.setCancelled(true);
		}
	}
}