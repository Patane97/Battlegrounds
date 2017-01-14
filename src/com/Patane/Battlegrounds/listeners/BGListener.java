package com.Patane.Battlegrounds.listeners;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class BGListener implements Listener{
	Plugin plugin;
	
	public BGListener(){
		register();
	}
	
	public BGListener(Plugin plugin){
		this.plugin 	= plugin;
		register();
	}
	public void register(){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	public void unregister() {
		HandlerList.unregisterAll(this);
	}
}
