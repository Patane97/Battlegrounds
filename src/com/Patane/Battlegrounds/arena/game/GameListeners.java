package com.Patane.Battlegrounds.arena.game;

import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.listeners.ArenaListener;
import com.Patane.Battlegrounds.util.RelativePoint;

public class GameListeners extends ArenaListener {
	Game game;

	public GameListeners(Plugin plugin, Arena arena, Game game) {
		super(plugin, arena);
		this.game = game;
	}

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		RelativePoint point = arena.isWithin(event.getBlock());
		if (point != RelativePoint.OUTSIDE) {
			if (arena.getSettings().DESTRUCTABLE && point == RelativePoint.GROUNDS_INNER) {
				event.setCancelled(false);
				return;
			}
			event.setCancelled(true);
		}
	}

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		RelativePoint point = arena.isWithin(event.getBlock());
		if (point != RelativePoint.OUTSIDE) {
			if (arena.getSettings().DESTRUCTABLE && point == RelativePoint.GROUNDS_INNER) {
				event.setCancelled(false);
				return;
			}
			event.setCancelled(true);
		}
	}

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingBreak(HangingBreakByEntityEvent event) {
		RelativePoint point = arena.isWithin(event.getEntity());
		if (point != RelativePoint.OUTSIDE) {
			if (arena.getSettings().DESTRUCTABLE && point == RelativePoint.GROUNDS_INNER) {
				event.setCancelled(false);
				return;
			}
			event.setCancelled(true);
		}
	}

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingPlace(HangingPlaceEvent event) {
		RelativePoint point = arena.isWithin(event.getEntity());
		if (point != RelativePoint.OUTSIDE) {
			if (arena.getSettings().DESTRUCTABLE && point == RelativePoint.GROUNDS_INNER) {
				event.setCancelled(false);
				return;
			}
			event.setCancelled(true);
		}
	}

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemFrameHit(EntityDamageEvent event) {
		if (arena.isWithin(event.getEntity()) != RelativePoint.OUTSIDE) {
			if (event.getEntity() instanceof ItemFrame) {
				event.setCancelled(true);
			}
		}
		RelativePoint point = arena.isWithin(event.getEntity());
		if (point != RelativePoint.OUTSIDE) {
			if (event.getEntity() instanceof ItemFrame) {
				if (arena.getSettings().DESTRUCTABLE && point == RelativePoint.GROUNDS_INNER) {
					event.setCancelled(false);
					return;
				}
				event.setCancelled(true);
			}
		}
	}

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplosion(EntityExplodeEvent event) {
		RelativePoint point = arena.isWithin(event.getLocation());
		if (point != RelativePoint.GROUNDS_INNER && point != RelativePoint.LOBBY_INNER) {
			Iterator<Block> blockIterator = event.blockList().iterator();
			while(blockIterator.hasNext())
				if(arena.isWithin(blockIterator.next()) != RelativePoint.OUTSIDE)
						blockIterator.remove();
		}
		else if (point == RelativePoint.GROUNDS_INNER) {
			if (arena.getSettings().DESTRUCTABLE) {
				Iterator<Block> blockIterator = event.blockList().iterator();
				while(blockIterator.hasNext())
					if(arena.isWithin(blockIterator.next()) != RelativePoint.GROUNDS_INNER)
							blockIterator.remove();
			}
			else
				event.blockList().clear();
		}
		else if (point == RelativePoint.LOBBY_INNER){
			event.blockList().clear();
		}
	}

	@EventHandler
	public void onMonsterCombust(EntityCombustEvent event) {
		if (event.getEntity() instanceof Creature && game.getRoundHandler().hasCreature((Creature) event.getEntity())) {
			if (!(event instanceof EntityCombustByBlockEvent || event instanceof EntityCombustByEntityEvent))
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onCreatureDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Creature) {
			if (game.getRoundHandler().creatureKilled((Creature) event.getEntity())) {
				game.updateExp();
				game.getRoundHandler().checkRoundEnd();
				event.getDrops().clear();
			}
		}
	}

	@EventHandler
	public void onCreatureExplode(EntityExplodeEvent event) {
		if (event.getEntity() instanceof Creature) {
			if (game.getRoundHandler().creatureKilled((Creature) event.getEntity())) {
				game.updateExp();
				game.getRoundHandler().checkRoundEnd();
			}
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			if (event.getDamage() > player.getHealth()) {
				if (game.playerKilled(player)) {
					event.setCancelled(true);
					player.setHealth(20);
					if (game.checkSessionOver())
						game.sessionOver();
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerByPlayerDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			if(!arena.getSettings().PVP_ENABLED)
				event.setCancelled(true);
			else
				event.setCancelled(false);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (arena.hasPlayer(player))
				event.setCancelled(true);
		}
	}

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (arena.isWithin(event.getEntity()) != RelativePoint.OUTSIDE) {
			if (game.getRoundHandler().hasCreature((Creature) event.getEntity())) {
				event.setCancelled(false);
				return;
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onArrowHit(ProjectileHitEvent event) {
		if (arena.isWithin(event.getEntity()) != RelativePoint.OUTSIDE && event.getEntity() instanceof Arrow) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					event.getEntity().remove();
				}
			}, 3 * 20);
		}
	}
}
