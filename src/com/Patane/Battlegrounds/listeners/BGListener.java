package com.Patane.Battlegrounds.listeners;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.Messenger.ChatType;

public class BGListener implements Listener{
	protected Plugin plugin;
	
	public BGListener(){
		register();
	}
	
	public BGListener(Plugin plugin){
		this.plugin 	= plugin; // CHANGE TO Battlegrounds.get();
		register();
	}
	public void register(){
		try{
			Messenger.debug(ChatType.INFO, "+++ REGISTERING ["+this.toString()+"]");
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
		} catch (IllegalPluginAccessException e){}
	}
	public void unregister() {
		Messenger.debug(ChatType.INFO, "--- UNREGISTERING ["+this.toString()+"]");
		HandlerList.unregisterAll(this);
	}
}
