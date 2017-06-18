package com.Patane.Battlegrounds.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.playerData.PlayerData;


public class GlobalListeners implements Listener{
	// making playerData.yml crash-proof
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		Arenas.removePlayer(player);
		PlayerData.YML().loadAllData(player);
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		Player player = event.getPlayer();
		Arenas.removePlayer(player);
	}
	// DEBUG FOR CUSTOM MOBS
//	@EventHandler
//	public void spawnCustom(PlayerInteractEvent event){
//		if(!event.hasBlock())
//			return;
//		Location loc = event.getClickedBlock().getLocation();
//		loc = new Location (loc.getWorld(), loc.getX()+0.5, loc.getY()+1, loc.getZ()+0.5);
//		EquipmentSlot e = event.getHand();
//        if (e.equals(EquipmentSlot.HAND) && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.COAL) {
//    		Entity entity = Spawning.spawnEntity(null, BGEntityType.ZOMBIE_SERVANT, loc);
//    		entity.setCustomNameVisible(true);
//        }
//	}
}
