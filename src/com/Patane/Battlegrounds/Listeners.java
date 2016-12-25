package com.Patane.Battlegrounds;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.Patane.Battlegrounds.collections.*;
import com.Patane.Battlegrounds.game.GameHandler;
import com.Patane.Battlegrounds.game.RoundHandler;

public class Listeners implements Listener{
	
	/*
	 * Checks when a creature is killed
	 * Finds the creatures GameHandler, then RoundHandler and checks if their round has ended
	 */
	// TODO: MOVE THESE LISTENERS INTO EACH ARENA OBJECT INSTANCE!!!!
	@EventHandler
	public void onCreatureDeath(EntityDeathEvent event){
		// checks if entity dying is a creature
		if(event.getEntity() instanceof Creature){
			// creating temporary creature & RoundHandler(to avoid clutter)
			final Creature creature = (Creature) event.getEntity();
			if(ActiveCreatures.getGame(creature) == null) return;
			RoundHandler creatureRound = ActiveCreatures.getGame(creature).getRoundHandler();
			// checks if the mob is actually part of a game, and if so, checks if that games round has ended
			if(creatureRound != null && creatureRound.creatureKilled(creature)){
				creatureRound.checkRoundEnd();
				event.getDrops().clear();
			}
		}
	}
	/*
	 * Similar thing with players. Checks when one dies
	 * Then checks if them dying will end any games
	 */
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		final Player player = event.getEntity();
		GameHandler playerGame = ActivePlayers.getGame(player);
		if(playerGame != null && playerGame.playerKilled(player)){
			playerGame.checkGameEnd();
			event.getDrops().clear();
			event.setDeathMessage(null);
		}
	}
	/*
	 * Checks if player will die from damage take. If so, it "kills" them from the game.
	 */
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			final Player player = (Player) event.getEntity();
			GameHandler playerGame = ActivePlayers.getGame(player);
			// has the player died from this damage
			if(event.getDamage() > player.getHealth()){
				if(playerGame != null && playerGame.playerKilled(player)){
					event.setCancelled(true);
					player.setHealth(20);
					playerGame.checkGameEnd();
				}
			}
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		
	}
	/*
	 * This listener stops mobs from combusting in daylight
	 * first if: ensures the mob is combusting from daylight
	 * for: runs though all active mobs in all active games
	 * second if: checks if mob is same as combusting mob
	 * 
	 * NOTE: currently, mobs STILL combust whilst in their death animation (fix this)
	 * -> could have something to do with EntityDeathEvent (ABOVE). Look into this
	 * 
	 */
	@EventHandler
	public void onMonsterCombust(EntityCombustEvent event){
		if(!(event instanceof EntityCombustByBlockEvent || event instanceof EntityCombustByEntityEvent)){
			for(Entity selectedmob : ActiveCreatures.getActiveCreaturesAll()){
				if(selectedmob.equals(event.getEntity())){
					event.setCancelled(true);
				}
			}
	    }
	}
//	@EventHandler (priority = EventPriority.HIGHEST)
//	public void onEntityTarget(EntityTargetEvent event){
//		for(Creature selectedCreature : RoundInstances.getActiveCreaturesAll()){
//			if(event.getEntity().equals(selectedCreature)){
//				if(event.getTarget() instanceof Creature){
//					Creature creature = (Creature) event.getEntity();
//					event.setCancelled(true);
//					creature.setTarget(Locations.findClosestPlayer(creature, playerList));
//					
//				}
//			}
//		}
//	}
	/////////////// WORK ON THIS //////////////////////
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onEntityTarget(EntityTargetEvent event){
		if(event.getTarget() instanceof Creature){
			for(Creature selectedCreature : ActiveCreatures.getActiveCreaturesAll()){
				if(event.getEntity().equals(selectedCreature)){
					
				}
			}			
		}
	}
}
