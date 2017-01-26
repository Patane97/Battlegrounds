package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.arena.Arena;

public class Arenas {
	private static HashMap<String, Arena> arenas = new HashMap<String, Arena>();
	
	public static void addAll(ArrayList<Arena> arenaList){
		for(Arena selectedArena : arenaList)
			arenas.put(selectedArena.getName(), selectedArena);
	}
	public static void add(Arena arena){
		arenas.put(arena.getName(), arena);
	}
	public static boolean remove(Arena arena){
		if(arenas.remove(arena.getName()) != null)
			return true;
		return false;
	}
	public static Arena grab(String name){
		for(String arenaName : arenas.keySet()){
			if(name.equalsIgnoreCase(arenaName))
				return arenas.get(arenaName);
		}
		return null;
	}
	public static Arena grab(Player player){
		for(Arena selectedArena : arenas.values()){
			if(selectedArena.hasPlayer(player))
				return selectedArena;
		}
		return null;
	}
	public static void allSessionsOver(){
		for(Arena selectedArena : arenas.values())
			selectedArena.getMode().sessionOver();
	}
	public static boolean contains(String arenaName){
		return arenas.containsKey(arenaName);
	}
	public static ArrayList<Arena> get(){
		ArrayList<Arena> tempArenas = new ArrayList<Arena>();
		for(Arena arena : arenas.values())
			tempArenas.add(arena);
		return tempArenas;
	}
	public static ArrayList<String> getNames(){
		ArrayList<String> tempArenas = new ArrayList<String>();
		for(String arenaName : arenas.keySet())
			tempArenas.add(arenaName);
		return tempArenas;
	}
	public static void cleanAll() {
		for(Arena selectedArena : arenas.values())
			selectedArena.clean();
	}
	public static void removePlayer(Player player) {
		Arena arena = grab(player);
		if(arena != null)
			arena.removePlayer(player.getDisplayName(), true);
	}
	public static void removeClass(String name) {
		for(Arena arena : arenas.values())
			arena.removeClass(name);			
	}
}
