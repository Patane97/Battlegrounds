package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;

import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.arena.builder.ArenaYML;

public class Arenas {
	static ArrayList<ArenaHandler> arenas = new ArrayList<ArenaHandler>();
	
	public static void addAll(ArrayList<ArenaHandler> arenaList){
		arenas.addAll(arenaList);
	}
	public static void add(ArenaHandler arena){
		arenas.add(arena);
	}
	public static boolean remove(ArenaHandler arena){
		ArenaYML.remove(arena);
		return arenas.remove(arena);
	}
	public static ArenaHandler get(String name){
		for(ArenaHandler selectedArena : arenas){
			if(selectedArena.getName().equals(name))
				return selectedArena;
		}
		return null;
	}
	public static ArrayList<String> listArenaNames() {
		ArrayList<String> arenaNames = new ArrayList<String>();
		for(ArenaHandler selectedArena : arenas){
			arenaNames.add(selectedArena.getName());
		}
		return arenaNames;
	}
}
