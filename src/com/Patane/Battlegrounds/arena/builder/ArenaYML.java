package com.Patane.Battlegrounds.arena.builder;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.Patane.Battlegrounds.Battlegrounds;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.util.Config;
import com.Patane.Battlegrounds.util.util;

public class ArenaYML {
	static Config arenasConfig;
	public static void load(Battlegrounds plugin) {
		arenasConfig = new Config(plugin, "arenas.yml");
		Arenas.addAll(loadAllArenas());
	}
	
	public static ArrayList<ArenaHandler> loadAllArenas(){
		ArrayList<ArenaHandler> arenas = new ArrayList<ArenaHandler>();
		for(String arenaName : arenasConfig.getConfigurationSection("arenas").getKeys(false)){
			ArenaHandler arena = loadArena(arenaName);
			if(arena != null){
				arenas.add(arena);
			} else {
				Messenger.warning("Tried to add an arena not in arenas.yml! Please check the file and make sure all values are in tact.");
			}
		}
		return arenas;
	}
	
	public static ArenaHandler loadArena (String name){
		try{
			String prefix = "arenas." + name + ".world";
			World world = Bukkit.getServer().getWorld(arenasConfig.get(prefix).toString());
			prefix = "arenas." + name + ".points.arena1";
			Location arena1 = util.getLocationYMLFormat(arenasConfig, prefix);
			prefix = "arenas." + name + ".points.arena2";
			Location arena2 = util.getLocationYMLFormat(arenasConfig, prefix);
			prefix = "arenas." + name + ".points.lobby1";
			Location lobby1 = util.getLocationYMLFormat(arenasConfig, prefix);
			prefix = "arenas." + name + ".points.lobby2";
			Location lobby2 = util.getLocationYMLFormat(arenasConfig, prefix);
			prefix = "arenas." + name + ".points.playerSpawn";
			Location playerSpawn = util.getLocationYMLFormat(arenasConfig, prefix);
			prefix = "arenas." + name + ".points.lobbySpawn";
			Location lobbySpawn = util.getLocationYMLFormat(arenasConfig, prefix);
			prefix = "arenas." + name + ".points.spectatorSpawn";
			Location spectatorSpawn = util.getLocationYMLFormat(arenasConfig, prefix);
			prefix = "arenas." + name + ".points.creatureSpawn";
			Location creatureSpawn = util.getLocationYMLFormat(arenasConfig, prefix);
	
			ArenaHandler newArena = new ArenaHandler(name, world, arena1, arena2, lobby1, lobby2, playerSpawn, lobbySpawn, spectatorSpawn, creatureSpawn);
			Messenger.info("Successfully loaded arena '" + newArena.getName() +"'...");
			return newArena;
		} catch (NullPointerException e){
			return null;
		}
	}
	
	public static void saveArena(ArenaBuilder builder){
		String prefix = "arenas." + builder.arenaName;
		arenasConfig.set(prefix + ".world", builder.world.getName());
		
		prefix = "arenas." + builder.arenaName + ".points.arena1";
		util.setLocationYMLFormat(arenasConfig, prefix, builder.arena1);
		prefix = "arenas." + builder.arenaName + ".points.arena2";
		util.setLocationYMLFormat(arenasConfig, prefix, builder.arena2);
		prefix = "arenas." + builder.arenaName + ".points.lobby1";
		util.setLocationYMLFormat(arenasConfig, prefix, builder.lobby1);
		prefix = "arenas." + builder.arenaName + ".points.lobby2";
		util.setLocationYMLFormat(arenasConfig, prefix, builder.lobby2);
		prefix = "arenas." + builder.arenaName + ".points.playerSpawn";
		util.setLocationYMLFormat(arenasConfig, prefix, builder.playerSpawn);
		prefix = "arenas." + builder.arenaName + ".points.lobbySpawn";
		util.setLocationYMLFormat(arenasConfig, prefix, builder.lobbySpawn);
		prefix = "arenas." + builder.arenaName + ".points.spectatorSpawn";
		util.setLocationYMLFormat(arenasConfig, prefix, builder.spectatorSpawn);
		prefix = "arenas." + builder.arenaName + ".points.creatureSpawn";
		util.setLocationYMLFormat(arenasConfig, prefix, builder.creatureSpawn);
		
		Messenger.info("Saving arena '" + builder.arenaName + "' to arenas.yml");
		arenasConfig.save();
	}
	
	public static Config get(){
		return arenasConfig;
	}

	public static void remove(ArenaHandler arena) {
		arenasConfig.getConfigurationSection("arenas").set(arena.getName(), null);
		arenasConfig.save();
	}

	
	
}
