package com.Patane.Battlegrounds.arena.editor;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.standby.StandbyListener;

public class EditorListeners extends StandbyListener{
	protected Editor editor;
	protected Player creator;
	protected UUID creatorUUID;
	
	public EditorListeners(Plugin plugin, Arena arena, Editor editor){
		super(plugin, arena);
		this.editor 		= editor;
		this.creator 		= editor.getCreator();
		this.creatorUUID	= creator.getUniqueId();

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