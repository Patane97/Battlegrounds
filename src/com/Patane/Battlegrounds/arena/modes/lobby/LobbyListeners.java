package com.Patane.Battlegrounds.arena.modes.lobby;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.Arena;
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
		if(arena.getPlayers().contains(player.getDisplayName())){
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
}
