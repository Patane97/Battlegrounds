package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.arena.Arena;

public class Arenas {
	private static HashMap<String, Arena> arenas = new HashMap<String, Arena>();

	public static Arena add(Arena arena){
		return arenas.put(arena.getName(), arena);
	}
	public static boolean remove(Arena arena){
		if(arenas.remove(arena.getName()) != null){
			Arena.YML().clearSection(arena.getName());
			return true;
		}
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
	public static Arena grabSpect(Player player){
		for(Arena selectedArena : arenas.values()){
			if(selectedArena.hasSpectator(player))
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
		return new ArrayList<Arena>(arenas.values());
	}
	public static ArrayList<String> getNames(boolean colour){
		ArrayList<String> tempArenas = new ArrayList<String>();
		for(String arenaName : arenas.keySet()){
			if(colour)
				arenaName = grab(arenaName).getCurrentColour() + arenaName;
			tempArenas.add(arenaName);
		}
		return tempArenas;
	}
	public static void cleanAll() {
		for(Arena selectedArena : arenas.values())
			selectedArena.clean();
	}
	public static boolean removePlayer(Player player) {
		Arena arena = grab(player);
		if(arena != null)
			return arena.getMode().removePlayer(player.getDisplayName(), true);
		return false;
	}
	public static void removeClass(String className) {
		for(Arena arena : arenas.values())
			arena.removeClass(className);			
	}
}
