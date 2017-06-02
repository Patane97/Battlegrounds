package com.Patane.Battlegrounds.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.util.Config;
import com.Patane.Battlegrounds.util.YML;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;

/*
 * NEED TO ADD "setHeader" etc. just like playerDataYML!!!
 */
public class ArenaYML {
	static Config arenasConfig;
	static Plugin plugin;
	static ConfigurationSection arenaSection;
	public static void load(Plugin battlegrounds) {
		plugin			= battlegrounds;
		arenasConfig 	= new Config(plugin, "arenas.yml");
		if(!arenasConfig.isConfigurationSection("arenas"))
			arenasConfig.createSection("arenas");
		Arenas.addAll(loadAllArenas());
	}
	
	public static ArrayList<Arena> loadAllArenas(){

		ArrayList<Arena> arenas = new ArrayList<Arena>();
		arenaSection = arenasConfig.getConfigurationSection("arenas");
		for(String arenaName : arenaSection.getKeys(false)){
			Arena arena = loadArena(arenaName);
			if(arena == null)
				continue;
			// for some reason this works WITHOUT this 'add' code. find out why.
			arenas.add(arena);
		}
		return arenas;
	}

	public static void saveAllArenas() {
		for(Arena selectedArena : Arenas.get())
			save(selectedArena);
	}
	public static void save(Arena arena) {
		String arenaName = arena.getName();
		clear(arenaName);
		saveWorld(arenaName, arena.getWorld());
		try{saveRegion(arenaName, arena.getGround(), "Ground");} catch(Exception e){}
		try{saveRegion(arenaName, arena.getLobby(), "Lobby");} catch(Exception e){}
		saveAllSpawns(arenaName);
		saveClasses(arenaName);
	}
	public static void clear(String arenaName) {
		if(arenasConfig.isConfigurationSection("arenas." + arenaName))
			arenasConfig.set("arenas." + arenaName, null);
	}

	public static Arena loadArena (String arenaName){
		try{
		ConfigurationSection arenaSection = arenasConfig.getConfigurationSection("arenas." + arenaName);
		// getting the world
		World world;
		try{ 
			world= Bukkit.getWorld(arenaSection.getString("world"));
		} catch (IllegalArgumentException e){
			Messenger.warning("Arena " + arenaName + " is missing world in arenas.yml");
			return null;
		}
		// getting the region
		AbstractRegion ground = grabRegion(world, arenaName, "Ground");
		AbstractRegion lobby = grabRegion(world, arenaName, "Lobby");
		ArrayList<Location> gameSpawns = grabSpawns(arenaName, "Game");
		ArrayList<Location> lobbySpawns = grabSpawns(arenaName, "Lobby");
		ArrayList<Location> creatureSpawns = grabSpawns(arenaName, "Creature");
		ArrayList<Location> spectatorSpawns = grabSpawns(arenaName, "Spectator");
		ArrayList<String> classes = loadClasses(arenaName);
		
		Arena loadedArena = new Arena(plugin, arenaName, world, ground, lobby, gameSpawns, lobbySpawns, creatureSpawns, spectatorSpawns, classes);
		Messenger.info("Arena '" + arenaName +"' successfully loaded!");
		return loadedArena;
		} catch (Exception e){
			Messenger.warning("Arena " + arenaName +" failed to load.");
			e.printStackTrace();
			return null;
		}
	}
	
	public static Config get(){
		return arenasConfig;
	}

	public static void remove(Arena arena) {
		if(arena == null)
			return;
		arenasConfig.getConfigurationSection("arenas").set(arena.getName(), null);
		arenasConfig.save();
	}
	public static boolean saveWorld(String arenaName, World world){
		arenaSection = arenasConfig.createSection("arenas." + arenaName);
		arenaSection.set("world", world.getName());
		return true;
	}
	public static boolean saveRegion(String arenaName, AbstractRegion selectedRegion, String type) {
		try{
			if(type == null || !Chat.hasAlpha(type))
				return false;
			arenaSection = arenasConfig.createSection("arenas." + arenaName + "." + type);
			arenaSection.set("Poly", null);
			arenaSection.set("Cuboid", null);
//			arenaSection.set("world", world.getName());
			if(selectedRegion instanceof Polygonal2DRegion){
				Polygonal2DRegion region = (Polygonal2DRegion) selectedRegion;
				arenaSection.set("Poly.minY", region.getMinimumY());
				arenaSection.set("Poly.maxY", region.getMaximumY());
				List<BlockVector2D> tempArray = region.getPoints();
				for(int i=1 ; i <= region.getPoints().size() ; i++){
					arenaSection.set("Poly.Vectors."+i, YML.blockVector2DFormat(tempArray.get(i-1)));
				}
			} else if(selectedRegion instanceof CuboidRegion){
				CuboidRegion region = (CuboidRegion) selectedRegion;
				arenaSection.set("Cuboid.min", YML.blockVectorFormat(region.getMinimumPoint().toBlockPoint()));
				arenaSection.set("Cuboid.max", YML.blockVectorFormat(region.getMaximumPoint().toBlockPoint()));
			}
			arenasConfig.save();
			Messenger.info("Successfully saved region for arena '" + arenaName + "' to arenas.YML!");
			return true;
		} catch (Exception e){
			Messenger.severe("Failed to save arena region. Deleting Arena '" + arenaName + "'.");
			Arenas.remove(Arenas.grab(arenaName));
			ArenaYML.remove(Arenas.grab(arenaName));
			e.printStackTrace();
			return false;
		}
	}
	@SuppressWarnings("deprecation")
	public static AbstractRegion grabRegion(World world, String arenaName, String type){

			AbstractRegion region = null;
			String path = "arenas." + arenaName + "." + type;
			arenaSection = (arenasConfig.isConfigurationSection(path) 
					? arenasConfig.getConfigurationSection(path)
					: arenasConfig.createSection(path));
			///////////////////////////////////////////// Polygonal /////////////////////////////////////////////
			if(arenaSection.contains("Poly")){
				arenaSection = arenasConfig.getConfigurationSection(path + ".Poly");
				int minY = arenaSection.getInt("minY");
				int maxY = arenaSection.getInt("maxY");
				arenaSection = arenasConfig.getConfigurationSection(path + ".Poly.Vectors");
				List<BlockVector2D> blockVectors = new ArrayList<BlockVector2D>();
				for(String blockVectorNumber : arenaSection.getKeys(false)){
					String blockVectorString = arenaSection.getString(blockVectorNumber);
					BlockVector2D blockVector = YML.getBlockVector2D(blockVectorString);
					blockVectors.add(blockVector);
				}
				region = new Polygonal2DRegion(BukkitUtil.getLocalWorld(world), blockVectors, minY, maxY);
			///////////////////////////////////////////// CUBOID /////////////////////////////////////////////
			} else if (arenaSection.contains("Cuboid")){
				arenaSection = arenasConfig.getConfigurationSection(path + ".Cuboid");
				BlockVector min = YML.getBlockVector(arenaSection.getString("min"));
				BlockVector max = YML.getBlockVector(arenaSection.getString("max"));
				region = new CuboidRegion (BukkitUtil.getLocalWorld(world), min, max);
			}
			if(region == null)
				Messenger.warning("Failed to grab " + type + " region for arena " + arenaName + " in arenas.yml!");
			return region;

			//Messenger.severe("Failed to build AbstractRegion from YML in arena '" + arenaName + "'.");

	}
	public static void saveSpawns(String arenaName, String spawnType, ArrayList<Location> spawns){
		try{
			String path = "arenas." + arenaName + ".Spawns." + spawnType;
			// deleting the section
			arenasConfig.set(path, null);
			// creating the section
			arenaSection = arenasConfig.createSection(path);
			int number = 1;
			for(Location spawnLocation : spawns){
				arenaSection.set(Integer.toString(number), YML.spawnLocationFormat(spawnLocation, true));
				number ++;
			}
			Messenger.info("Saved " + spawnType + " spawns!");
			arenasConfig.save();
		} catch (Exception e){
			Messenger.severe("Error when saving " + spawnType + " spawns in arena '" + arenaName +"'.");
			e.printStackTrace();
		}
	}
	public static void saveAllSpawns(String arenaName){
		Arena arena = Arenas.grab(arenaName);
		ArenaYML.saveSpawns(arenaName, "Game" , arena.getGameSpawns());
		ArenaYML.saveSpawns(arenaName, "Lobby" , arena.getLobbySpawns());
		ArenaYML.saveSpawns(arenaName, "Creature" , arena.getCreatureSpawns());
		ArenaYML.saveSpawns(arenaName, "Spectator" , arena.getSpectatorSpawns());
	}
	public static ArrayList<Location> grabSpawns(String arenaName, String spawnType){
		try{
			ArrayList<Location> spawns = new ArrayList<Location>();
			String path = "arenas." + arenaName;
			arenaSection = arenasConfig.getConfigurationSection(path);
			World world = Bukkit.getWorld(arenaSection.getString("world"));
			path = "arenas." + arenaName + ".Spawns";
			arenaSection = arenasConfig.getConfigurationSection(path);
			if(arenaSection == null){
				Messenger.warning("Arena '" + arenaName + "' its spawns section in arenas.yml");
				return null;
			}
			if(arenaSection.contains(spawnType)){
				path = "arenas." + arenaName + ".Spawns." + spawnType;
				arenaSection = arenasConfig.getConfigurationSection(path);
				
				for(String spawnNumber : arenaSection.getKeys(false)){
					Location spawn = YML.getSpawnLocation(world, arenaSection.getString(spawnNumber));
					spawns.add(spawn);
				}
				
			} else {
				Messenger.warning("Failed to find " + spawnType + " Spawns for arena '" + arenaName + "' in arenas.yml");
			}
			
			return spawns;
		} catch (Exception e){
			Messenger.severe("Error when grabbing " + spawnType + " spawns for arena '" + arenaName +"'.");
			e.printStackTrace();
			return null;
		}
	}

	public static void sync() {
		for(Arena arena : Arenas.get())
			save(arena);
	}
	public static ArrayList<String> loadClasses(String arenaName){
		arenaSection = arenasConfig.getConfigurationSection("arenas." + arenaName);
		@SuppressWarnings("unchecked")
		ArrayList<String> temp = (ArrayList<String>) arenaSection.getList("Classes", new ArrayList<String>());
		return temp;
	}
	public static void saveClasses(String arenaName){
		arenaSection = arenasConfig.getConfigurationSection("arenas." + arenaName);
		Arena arena = Arenas.grab(arenaName);
		arenaSection.set("Classes", arena.getClasses());
		arenasConfig.save();
	}
	
}
