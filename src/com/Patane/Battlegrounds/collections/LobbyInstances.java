package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.Collection;

import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.lobby.LobbyHandler;

public class LobbyInstances {

	private static Collection<LobbyHandler> LobbyInstances = new ArrayList<LobbyHandler>();
	
	// adding or removing from the collection of game instances
	public static void add(LobbyHandler lobby){
		LobbyInstances.add(lobby);
	}
	public static void remove(LobbyHandler lobby){
		LobbyInstances.remove(lobby);
	}
	public static LobbyHandler getLobby(ArenaHandler arena) {
		for(LobbyHandler selectedLobby : LobbyInstances){
			if(arena.equals(selectedLobby.getArena())){
				return selectedLobby;
			}
		}
		return null;
	}
}
