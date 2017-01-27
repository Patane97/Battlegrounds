package com.Patane.Battlegrounds.arena.lobby;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.collections.Classes;
import com.Patane.Battlegrounds.listeners.ArenaListener;

public class LobbyListeners extends ArenaListener{
	Lobby lobby;
	public LobbyListeners(Plugin plugin, Arena arena, Lobby lobby){
		super(plugin, arena);
		this.lobby = lobby;
	}
	@EventHandler
	public void onAnvilHit(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(arena.hasPlayer(player)){
			EquipmentSlot e = event.getHand();
			if (e.equals(EquipmentSlot.HAND)) {
				if(event.hasBlock()){
					Block block = event.getClickedBlock();
					if(block.getType().equals(Material.ANVIL)){
						event.setCancelled(true);
						lobby.playerReady(player);
					}
				}
			}
		}
	}
	@EventHandler
	public void onItemFrameRightHit(PlayerInteractEntityEvent event){
		Player player = event.getPlayer();
		if(arena.hasPlayer(player)){
			Entity entity = event.getRightClicked();
			event.setCancelled(equipClass(entity, player));
		}
	}
//	public void onItemFrameLeftHit(HangingBreakByEntityEvent event){
//		Messenger.arenaCast(arena, "entity hit entity!");
//		if(event.getRemover() instanceof Player){
//			Messenger.arenaCast(arena, "player hit entity!");
//			Player player = (Player) event.getRemover();
//			if(arena.hasPlayer(player)){
//				Entity entity = event.getEntity();
//				event.setCancelled(equipClass(entity, player));
//			}
//		}
//	}
	private boolean equipClass(Entity entity, Player player){
		if(entity instanceof ItemFrame){
			ItemFrame itemFrame = (ItemFrame) entity;
			ItemStack itemFrameItem = itemFrame.getItem();
			if(!itemFrameItem.hasItemMeta() || !itemFrameItem.getItemMeta().hasDisplayName())
				return false;
			String itemName = itemFrameItem.getItemMeta().getDisplayName();
			arena.equipClass(player, Classes.grab(itemName));
			return true;
		}
		return false;
	}
}
