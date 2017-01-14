package com.Patane.Battlegrounds.arena.modes.lobby;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.modes.Standby;
import com.Patane.Battlegrounds.arena.modes.game.Game;

public class Lobby extends Standby{
	
	public Lobby(Plugin plugin, Arena arena){
		super(plugin, arena);
		
		this.listener = new LobbyListeners(plugin, this.getArena(), this);
		
		this.defaultLocations = arena.getLobbySpawns();
	}
	public boolean checkStartGame(){
		if(!arena.anyPlayers(false))
			return true;
		return false;
	}
	public void startGame(){
		arena.setMode(new Game(plugin, arena));
	}
	public void playerReady(Player player) {
		if(!arena.hasClass(player)){
			Messenger.send(player, "&cYou must select a class before being ready!");
			return;
		}
		arena.putPlayer(player, true);
		Messenger.arenaCast(arena, player.getDisplayName() + " &ais ready!");
		if(checkStartGame())
			startGame();
	}
}
