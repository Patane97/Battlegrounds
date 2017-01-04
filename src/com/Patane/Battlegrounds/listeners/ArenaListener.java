package com.Patane.Battlegrounds.listeners;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.Arena;

public class ArenaListener implements Listener{
	Plugin plugin;
	protected Arena arena;
	
	public ArenaListener(){}
	
	public ArenaListener(Plugin plugin, Arena arena){
		this.plugin 	= plugin;
		this.arena 		= arena;
		register();
	}
	public void register(){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	public void unregister() {
		HandlerList.unregisterAll(this);
	}
}
