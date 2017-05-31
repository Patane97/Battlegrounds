package com.Patane.Battlegrounds.arena.editor.initialize;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.ArenaYML;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.editor.spawn.SpawnEditor;
import com.Patane.Battlegrounds.util.util;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.AbstractRegion;

public class InitializeListeners extends EditorListeners{
	Initialize initilize;
	public InitializeListeners(Plugin plugin, Arena arena, Initialize initilize, Editor editor) {
		super(plugin, arena, editor);
		this.initilize = initilize;
	}
	
	@EventHandler
	public void onAnvilPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();
		if(playerUUID.equals(creatorUUID) && event.getBlock().getType() == Material.ANVIL){
			AbstractRegion region = util.getAbstractRegion(plugin, player);
			Location location = event.getBlock().getLocation();
			Vector vector = new Vector(location.getX(), location.getY(), location.getZ());
			if(region.contains(vector)){
				arena.setLobby(region);
				ArenaYML.saveRegion(arena.getName(), region, "Lobby");
				editor.newEditorType(new SpawnEditor(plugin, arena, creator, editor));
			}
		}
	}
}
