package com.Patane.Battlegrounds.arena.modes.editor.types;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.modes.editor.Editor;
import com.Patane.Battlegrounds.arena.modes.editor.EditorListeners;

public class BuildEditListeners extends EditorListeners{
	BuildEditor buildEditor;
	public BuildEditListeners(Plugin plugin, Arena arena, BuildEditor buildEditor, Editor editor) {
		super(plugin, arena, editor);
		this.buildEditor = buildEditor;
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		String playerName = player.getDisplayName();
		Location blockLocation = event.getBlockPlaced().getLocation();
		if(playerName.equals(creatorName)){
			if(spawnBelow(blockLocation)){
				event.setCancelled(true);
				Messenger.send(player, "&cBlocks cannot be placed above spawn Locations");
				return;
			}
			if(arena.isWithin(blockLocation) && event.isCancelled())
				event.setCancelled(false);
		}
				
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		String playerName = player.getDisplayName();
		Location blockLocation = event.getBlock().getLocation();
		if(playerName.equals(creatorName) && 
				arena.isWithin(blockLocation) && 
				event.isCancelled()){
				event.setCancelled(false);
		}
	}

}
