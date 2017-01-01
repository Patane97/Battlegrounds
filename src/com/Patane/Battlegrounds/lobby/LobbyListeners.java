package com.Patane.Battlegrounds.lobby;

import org.bukkit.plugin.Plugin;

public class LobbyListeners {
	Plugin plugin;
	LobbyHandler lobby;
	
	public LobbyListeners(Plugin plugin){
		this.plugin = plugin;
	}
	public void setLobby(LobbyHandler lobby){
		this.lobby = lobby;
	}
}
