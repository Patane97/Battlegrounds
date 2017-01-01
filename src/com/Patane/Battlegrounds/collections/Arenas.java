package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;

//import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.arena.builder.ArenaYML;

public class Arenas {
	private static ArrayList<ArenaHandler> arenas = new ArrayList<ArenaHandler>();
	
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
	public static ArrayList<ArenaHandler> get(){
		return arenas;
	}
	public static boolean alreadyArena(String arenaName){
		for(ArenaHandler selectedArena : arenas){
			if(arenaName.equals(selectedArena.getName())){
				return true;
			}
		}
		return false;
	}
}
