package com.Patane.Battlegrounds.listeners;

import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.Arena;

public class ArenaListener extends BGListener{
	protected Arena arena;
	
	public ArenaListener (Plugin plugin, Arena arena){
		super(plugin);
		this.arena 	= arena;
	}
}
