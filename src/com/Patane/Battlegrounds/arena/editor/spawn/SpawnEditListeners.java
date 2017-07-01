package com.Patane.Battlegrounds.arena.editor.spawn;

import java.util.ArrayList;
import java.util.UUID;

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
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.util.RelativePoint;

public class SpawnEditListeners extends EditorListeners{
	SpawnEditor spawnEditor;
	public SpawnEditListeners(Plugin plugin, Arena arena, SpawnEditor spawnEditor, Editor editor) {
		super(plugin, arena, editor);
		this.spawnEditor = spawnEditor;
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();
		if(playerUUID.equals(creatorUUID)){
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
				RelativePoint relativePoint = arena.isWithin(block);
				if(itemInHandName.contains("Game") || itemInHandName.contains("Creature")){
					if(relativePoint != RelativePoint.GROUNDS_INNER && relativePoint != RelativePoint.GROUNDS_BORDER){
						event.setCancelled(true);
						Messenger.send(player, itemInHandName + "s &cmust be placed within the battleground.");
						return;
					}
					if(itemInHandName.contains("Game"))
						spawnEditor.addGameSpawn(location);
					else if (itemInHandName.contains("Creature"))
						spawnEditor.addCreatureSpawn(location);
				}
				else if(itemInHandName.contains("Lobby")){
					if(relativePoint != RelativePoint.LOBBY_INNER && relativePoint != RelativePoint.LOBBY_BORDER){
						event.setCancelled(true);
						Messenger.send(player, itemInHandName + "s &cmust be placed within the lobby.");
						return;
					}
					spawnEditor.addLobbySpawn(location);
				}
				else if(itemInHandName.contains("Spectator")){
					if(relativePoint != RelativePoint.OUTSIDE){
						event.setCancelled(true);
						Messenger.send(player, itemInHandName + "s &cmust not be placed inside the battleground or lobby");
						return;
					}
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
		UUID playerUUID = player.getUniqueId();
		if(playerUUID.equals(creatorUUID)){
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
		UUID playerUUID = player.getUniqueId();
		if(playerUUID.equals(creatorUUID)){
			event.setCancelled(true);
		}
	}
}
