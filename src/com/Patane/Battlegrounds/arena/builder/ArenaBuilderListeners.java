package com.Patane.Battlegrounds.arena.builder;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.util.Locations;

public class ArenaBuilderListeners implements Listener{
	Plugin plugin;
	ArenaBuilder builder;
	public ArenaBuilderListeners(Plugin plugin){
		this.plugin 	= plugin;
	}
	public void setBuilder(ArenaBuilder builder){
		this.builder = builder;
	}
	@EventHandler
	public void onInteraction(PlayerInteractEvent event){
		try{
			EquipmentSlot hand = event.getHand();
			Location point = event.getClickedBlock().getLocation();
			if(event.getPlayer().equals(builder.getCreator())){	
				if (hand.equals(EquipmentSlot.HAND) && event.getItem().getType().equals(Material.BONE))
					builder.setNewLocation(Locations.centreOnBlock(point));
			}
		} catch (Exception e){}
	}
	public static void unRegister(){
		
	}
}