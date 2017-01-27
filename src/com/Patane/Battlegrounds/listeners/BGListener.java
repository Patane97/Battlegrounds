package com.Patane.Battlegrounds.listeners;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;

public class BGListener implements Listener{
	protected Plugin plugin;
	
	public BGListener(){
		register();
	}
	
	public BGListener(Plugin plugin){
		this.plugin 	= plugin;
		register();
	}
	public void register(){
		try{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		} catch (IllegalPluginAccessException e){}
	}
	public void unregister() {
		HandlerList.unregisterAll(this);
	}
}
