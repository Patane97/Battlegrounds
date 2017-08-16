package com.Patane.Battlegrounds.arena;

import java.util.Iterator;

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

import com.Patane.Battlegrounds.arena.Arena.RelativePoint;
import com.Patane.Battlegrounds.listeners.BGListener;

public abstract class ArenaListener extends BGListener{
	protected Arena arena;
	
	public ArenaListener (Plugin plugin, Arena arena){
		super(plugin);
		this.arena 	= arena;
	}
//	
//	@EventHandler(priority = EventPriority.HIGHEST)
//	public void onBlockBreak(BlockBreakEvent event){
//		if(arena.isWithin(event.getBlock()) != RelativePoint.OUTSIDE){
//			event.setCancelled(true);
//			Messenger.send(event.getPlayer(), "&cYou can not break blocks in Arena &7" + arena.getName());
//		}
//	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event){
		if(arena.isWithin(event.getBlock()) != RelativePoint.OUTSIDE){
			event.setCancelled(ifBlockBreak(event));
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event){
		if(arena.isWithin(event.getBlock()) != RelativePoint.OUTSIDE){
			event.setCancelled(ifBlockPlace(event));
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingBreak(HangingBreakByEntityEvent event){
		if(arena.isWithin(event.getEntity()) != RelativePoint.OUTSIDE){
			event.setCancelled(ifHangingBreak(event));
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingPlace(HangingPlaceEvent event){
		if(arena.isWithin(event.getEntity()) != RelativePoint.OUTSIDE){
			event.setCancelled(ifHangingPlace(event));
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onItemFrameHit(EntityDamageEvent event) {
		if(arena.isWithin(event.getEntity()) != RelativePoint.OUTSIDE){
			if (event.getEntity() instanceof ItemFrame) {
				event.setCancelled(ifItemFrameHit(event));
	        }
		}
    }
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplosion(EntityExplodeEvent event){
		// Dealing with ifExplosion logic to cancel event or not
		if(arena.isWithin(event.getLocation()) != RelativePoint.OUTSIDE){
			if(ifExplosion(event))
				event.blockList().clear();
			// Dealing with explosions made within Grounds & Lobby
			else {
				Iterator<Block> blockIterator = event.blockList().iterator();
				while(blockIterator.hasNext())
					if(arena.isWithin(blockIterator.next()) != RelativePoint.GROUNDS_INNER)
							blockIterator.remove();
			}
		}
		// Dealing with explosions made outside Grounds & Lobby
		else {
			Iterator<Block> blockIterator = event.blockList().iterator();
			while(blockIterator.hasNext())
				if(arena.isWithin(blockIterator.next()) != RelativePoint.OUTSIDE)
						blockIterator.remove();
		} 
	}
	/**
	 * @return True if event is to be cancelled, False otherwise.
	 */
	protected abstract boolean ifBlockBreak(BlockBreakEvent event);
	protected abstract boolean ifBlockPlace(BlockPlaceEvent event);
	protected abstract boolean ifHangingBreak(HangingBreakByEntityEvent event);
	protected abstract boolean ifHangingPlace(HangingPlaceEvent event);
	protected abstract boolean ifItemFrameHit(EntityDamageEvent event);
	protected abstract boolean ifExplosion(EntityExplodeEvent event);
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if(arena.isWithin(event.getEntity()) != RelativePoint.OUTSIDE){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		if(arena.isWithin(event.getEntity()) != RelativePoint.OUTSIDE){
			event.getDrops().clear();
			event.setDroppedExp(0);
		}
	}
	@EventHandler
	public void onEntityTeleport(EntityTeleportEvent event){
		RelativePoint getToPoint = arena.isWithin(event.getTo());
		RelativePoint getFromPoint = arena.isWithin(event.getFrom());
		//teleporting from outside to inside OR from inside to outside arena area.
		if(getToPoint != RelativePoint.OUTSIDE && getFromPoint == RelativePoint.OUTSIDE
				|| getToPoint == RelativePoint.OUTSIDE && getFromPoint != RelativePoint.OUTSIDE)
			event.setCancelled(true);
	}
}
