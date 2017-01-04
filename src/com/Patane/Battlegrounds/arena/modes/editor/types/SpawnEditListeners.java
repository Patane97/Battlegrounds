package com.Patane.Battlegrounds.arena.modes.editor.types;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.modes.editor.Editor;
import com.Patane.Battlegrounds.arena.modes.editor.EditorListeners;

public class SpawnEditListeners extends EditorListeners{
	SpawnEditor spawnEditor;
	public SpawnEditListeners(Plugin plugin, Arena arena, SpawnEditor spawnEditor, Editor editor) {
		super(plugin, arena, editor);
		this.spawnEditor = spawnEditor;
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		String playerName = player.getDisplayName();
		
		if(playerName.equals(creatorName)){
			if(event.getBlockPlaced().getType().equals(Material.WOOL)){
				Block block = event.getBlockPlaced();
				
				Location blockLocation = block.getLocation();
				float yaw = player.getLocation().getYaw();
				Location location = new Location(blockLocation.getWorld(), blockLocation.getBlockX()+0.5, 
						blockLocation.getBlockY(), blockLocation.getBlockZ()+0.5, yaw,  0);
				String itemInHandName = event.getItemInHand().getItemMeta().getDisplayName();
				
				// checks if block placed is above or below another spawn of any kind
				ArrayList<Location> allSpawns = arena.getAllSpawns();
				if(allSpawns != null){
					if(spawnAboveOrBelow(location)){
						event.setCancelled(true);
						Messenger.send(player, itemInHandName + " &ccannot be above or below another spawn.");
						return;
					}
				}
				// checks if block above block placed is air (needs to be air so players/all mobs can spawn)
				Location locationAbove = new Location(player.getWorld(), location.getBlockX(), location.getBlockY()+1, location.getBlockZ());
				Block blockAbove = locationAbove.getBlock();
				if(!blockAbove.getType().equals(Material.AIR)){
					event.setCancelled(true);
					Messenger.send(player, "&cThere cannot be a block above a " + itemInHandName + "&c.");
					return;
				}
				if(itemInHandName.contains("Game Spawn") || itemInHandName.contains("Creature Spawn")){
					if(!arena.isWithin(location)){
						event.setCancelled(true);
						Messenger.send(player, itemInHandName + "s &cmust be placed inside the arena.");
						return;
					}
					if(itemInHandName.contains("Game Spawn"))
						spawnEditor.addGameSpawn(location);
					else if (itemInHandName.contains("Creature Spawn"))
						spawnEditor.addCreatureSpawn(location);
				}
				if(itemInHandName.contains("Lobby Spawn") || itemInHandName.contains("Spectator Spawn")){
					if(arena.isWithin(location)){
						event.setCancelled(true);
						Messenger.send(player, itemInHandName + "s &cmust be placed outside the arena.");
						return;
					}
					if(itemInHandName.contains("Lobby Spawn"))
						spawnEditor.addLobbySpawn(location);
					else if (itemInHandName.contains("Spectator Spawn"))
						spawnEditor.addSpectatorSpawn(location);
				}
				spawnEditor.refreshPlayerItems();
				Messenger.send(player, itemInHandName + "&a added at (" + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + ")");
				return;
			} else {
				// stops non-wool blocks from being places
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		String playerName = player.getDisplayName();
		if(playerName.equals(creatorName)){
			Block block = event.getBlock();
			Location location = block.getLocation();
			// if block broken is wool AND a spawn location
			String blockName = arena.findRemoveSpawn(location);
			if(blockName != null){
				Messenger.send(creator, blockName + " &aremoved at (" + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + ")");
				spawnEditor.refreshSpawnBlocks();
				spawnEditor.refreshPlayerItems();
			} else {
				Messenger.send(creator, "&cYou can only break spawn blocks.");
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event){
		Player player = event.getPlayer();
		String playerName = player.getDisplayName();
		if(playerName.equals(creatorName)){
			event.setCancelled(true);
		}
	}
}
