package com.Patane.Battlegrounds.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;

public class ArenaListener extends BGListener{
	protected Arena arena;
	
	public ArenaListener (Plugin plugin, Arena arena){
		super(plugin);
		this.arena 	= arena;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event){
		if(arena.isWithin(event.getBlock()) != 0){
			event.setCancelled(true);
			Messenger.send(event.getPlayer(), "&cYou can not break blocks in Arena &7" + arena.getName());
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event){
		if(arena.isWithin(event.getBlock()) != 0){
			event.setCancelled(true);
			Messenger.send(event.getPlayer(), "&cYou can not place blocks in Arena &7" + arena.getName());
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingBreak(HangingBreakByEntityEvent event){
		if(arena.isWithin(event.getEntity()) != 0){
			event.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingPlace(HangingPlaceEvent event){
		if(arena.isWithin(event.getEntity()) != 0){
			event.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onItemFrameHit(EntityDamageEvent event) {
		if(arena.isWithin(event.getEntity()) != 0){
			if (event.getEntity() instanceof ItemFrame) {
				event.setCancelled(true);
	        }
		}
    }
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplosion(EntityExplodeEvent event){
		for(Block block : event.blockList()){
			if(arena.isWithin(block) != 0){
				event.blockList().clear();
				break;
			}
		}
	}
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if(arena.isWithin(event.getEntity()) != 0){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		if(arena.isWithin(event.getEntity()) != 0){
			event.getDrops().clear();
			event.setDroppedExp(0);
		}
	}
	@EventHandler
	public void onEntityTeleport(EntityTeleportEvent event){
		//teleporting from outside to inside OR from inside to outside arena area.
		if(arena.isWithin(event.getTo()) != 0 && arena.isWithin(event.getFrom()) == 0
				|| arena.isWithin(event.getTo()) == 0 && arena.isWithin(event.getFrom()) != 0)
			event.setCancelled(true);
	}
}
