package com.Patane.Battlegrounds.arena.standby;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.ArenaListener;

public class StandbyListener extends ArenaListener{
	final String BREAK_MSG = "&cYou can not break blocks in Arena &7" + arena.getName();
	final String PLACE_MSG = "&cYou can not place blocks in Arena &7" + arena.getName();
	
	public StandbyListener(Plugin plugin, Arena arena) {
		super(plugin, arena);
	}
	@Override
	protected boolean ifBlockBreak(BlockBreakEvent event) {
		Messenger.send(event.getPlayer(), BREAK_MSG);
		return true;
	}
	@Override
	protected boolean ifBlockPlace(BlockPlaceEvent event) {
		Messenger.send(event.getPlayer(), PLACE_MSG);
		return true;
	}
	@Override
	protected boolean ifHangingBreak(HangingBreakByEntityEvent event) {
		if(event.getRemover() instanceof Player)
			Messenger.send(event.getRemover(), BREAK_MSG);
		return true;
	}
	@Override
	protected boolean ifHangingPlace(HangingPlaceEvent event) {
		Messenger.send(event.getPlayer(), PLACE_MSG);
		return true;
	}
	@Override
	protected boolean ifItemFrameHit(EntityDamageEvent event) {
		if(event instanceof EntityDamageByEntityEvent ){
			EntityDamageByEntityEvent tempEvent = (EntityDamageByEntityEvent) event;
			if(tempEvent.getDamager() instanceof Player)
				Messenger.send(tempEvent.getDamager(), BREAK_MSG);
		}
		return true;
	}
	@Override
	protected boolean ifExplosion(EntityExplodeEvent event) {
		return true;
	}

}
