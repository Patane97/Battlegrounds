package com.Patane.Battlegrounds.arena.lobby;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.game.Game;
import com.Patane.Battlegrounds.arena.standby.Standby;

public class Lobby extends Standby{
	
	public Lobby(Plugin plugin, Arena arena){
		super(plugin, arena, null);
		
		this.listener			= new LobbyListeners(plugin, arena, this);
		this.defaultLocations 	= arena.getLobbySpawns();
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
		if(!arena.playerHasClass(player)){
			Messenger.send(player, "&cYou must select a class before being ready!");
			return;
		}
		arena.putPlayer(player, true);
		Messenger.arenaCast(arena, player.getDisplayName() + " &ais ready!");
		if(checkStartGame())
			startGame();
	}
}
