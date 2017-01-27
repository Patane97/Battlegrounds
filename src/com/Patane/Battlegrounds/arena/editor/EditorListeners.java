package com.Patane.Battlegrounds.arena.editor;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.listeners.ArenaListener;

public class EditorListeners extends ArenaListener{
	protected Editor editor;
	protected Player creator;
	protected String creatorName;
	
	public EditorListeners(Plugin plugin, Arena arena, Editor editor){
		super(plugin, arena);
		this.editor 		= editor;
		this.creator 		= editor.getCreator();
		this.creatorName 	= creator.getDisplayName();

	}
	protected boolean spawnAboveOrBelow(Location location){
		for(Location spawnLocation : arena.getAllSpawns()){
			if(location.getBlockX() == spawnLocation.getBlockX() 
					&& location.getBlockZ() == spawnLocation.getBlockZ()){
				if(location.getBlockY()+1 == spawnLocation.getBlockY() || location.getBlockY()-1 == spawnLocation.getBlockY())
					return true;
			}
		}
		return false;
	}
	protected boolean spawnBelow(Location location){
		for(Location spawnLocation : arena.getAllSpawns()){
			if(location.getBlockX() == spawnLocation.getBlockX() 
					&& location.getBlockZ() == spawnLocation.getBlockZ()){
				if(location.getBlockY()-1 == spawnLocation.getBlockY())
					return true;
			}
		}
		return false;
	}
	protected boolean spawnAt(Location location){
		for(Location spawnLocation : arena.getAllSpawns()){
			if(location.getBlockX() == spawnLocation.getBlockX() 
					&& location.getBlockY() == spawnLocation.getBlockY() 
					&& location.getBlockZ() == spawnLocation.getBlockZ())
				return true;
		}
		return false;
	}
}