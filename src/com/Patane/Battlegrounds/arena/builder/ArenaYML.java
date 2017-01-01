package com.Patane.Battlegrounds.arena.builder;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.util.Config;
import com.Patane.Battlegrounds.util.YML;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;

public class ArenaYML {
	static Config arenasConfig;
	static Plugin plugin;
	
	public static void load(Plugin battlegrounds) {
		plugin			= battlegrounds;
		arenasConfig 	= new Config(plugin, "arenas.yml");
		Arenas.addAll(loadAllArenas());

	}
	
	public static ArrayList<ArenaHandler> loadAllArenas(){

		ArrayList<ArenaHandler> arenas = new ArrayList<ArenaHandler>();
		ConfigurationSection arenaHeader = arenasConfig.getConfigurationSection("arenas");
		if(arenaHeader == null){
			arenaHeader = arenasConfig.createSection("arenas");
		}
		for(String arenaName : arenaHeader.getKeys(false)){
			ArenaHandler arena = loadArena(arenaName);
			if(arena == null){
				Messenger.warning("Arena '" + arenaName + "' not recognised in arenas.yml!");
			}
		}
		return arenas;
	}
	public static ArenaHandler loadArena (String arenaName){

		ConfigurationSection arenaSection = arenasConfig.getConfigurationSection("arenas." + arenaName);
		// getting the world
		World world = Bukkit.getWorld(arenaSection.getString("world"));
		// getting the region
		AbstractRegion region = grabRegion(world, arenaName);
		ArrayList<Location> playerSpawns = grabSpawns(arenaName, "Player");
		ArrayList<Location> lobbySpawns = grabSpawns(arenaName, "Lobby");
		ArrayList<Location> creatureSpawns = grabSpawns(arenaName, "Creature");
		ArrayList<Location> spectatorSpawns = grabSpawns(arenaName, "Spectator");
		
		ArenaHandler loadedArena = new ArenaHandler(plugin, arenaName, world, region, playerSpawns, lobbySpawns, creatureSpawns, spectatorSpawns);
		Messenger.info("Arena '" + arenaName +"' successfully loaded!");
		return loadedArena;
	}
	
	public static Config get(){
		return arenasConfig;
	}

	public static void remove(ArenaHandler arena) {
		arenasConfig.getConfigurationSection("arenas").set(arena.getName(), null);
		arenasConfig.save();
	}

	public static void saveRegion(String arenaName, World world, AbstractRegion selectedRegion) {
		try{
		String prefix = "arenas." + arenaName;
		arenasConfig.set(prefix+".world", world.getName());
		if(selectedRegion instanceof Polygonal2DRegion){
			Polygonal2DRegion region = (Polygonal2DRegion) selectedRegion;
			arenasConfig.set(prefix+".Poly.minY", region.getMinimumY());
			arenasConfig.set(prefix+".Poly.maxY", region.getMaximumY());
			List<BlockVector2D> tempArray = region.getPoints();
			for(int i=1 ; i <= region.getPoints().size() ; i++){
				arenasConfig.set(prefix+".Poly.Vectors."+i, YML.BlockVector2DFormat(tempArray.get(i-1)));
			}
		} else if(selectedRegion instanceof CuboidRegion){
			CuboidRegion region = (CuboidRegion) selectedRegion;
			arenasConfig.set(prefix+".Cuboid.min", YML.BlockVectorFormat(region.getMinimumPoint().toBlockPoint()));
			arenasConfig.set(prefix+".Cuboid.max", YML.BlockVectorFormat(region.getMaximumPoint().toBlockPoint()));
		}
		arenasConfig.save();
		Messenger.info("Successfully saved region for arena '" + arenaName + "' to arenas.YML!");
		} catch (Exception e){
			Messenger.severe("Failed to save arena region. Deleting Arena '" + arenaName + "'.");
			Arenas.remove(Arenas.get(arenaName));
			e.printStackTrace();
		}
	}
	@SuppressWarnings("deprecation")
	public static AbstractRegion grabRegion(World world, String arenaName){

			AbstractRegion region = null;
			ConfigurationSection arenaSection = arenasConfig.getConfigurationSection("arenas." + arenaName);
			///////////////////////////////////////////// Polygonal /////////////////////////////////////////////
			if(arenaSection.contains("Poly")){
				arenaSection = arenasConfig.getConfigurationSection("arenas." + arenaName + ".Poly");
				int minY = arenaSection.getInt("minY");
				int maxY = arenaSection.getInt("maxY");
				arenaSection = arenasConfig.getConfigurationSection("arenas." + arenaName + ".Poly.Vectors");
				List<BlockVector2D> blockVectors = new ArrayList<BlockVector2D>();
				for(String blockVectorNumber : arenaSection.getKeys(false)){
					String blockVectorString = arenasConfig.getString("arenas." + arenaName + ".Poly.Vectors." + blockVectorNumber);
					BlockVector2D blockVector = YML.getBlockVector2D(blockVectorString);
					blockVectors.add(blockVector);
				}
				region = new Polygonal2DRegion(BukkitUtil.getLocalWorld(world), blockVectors, minY, maxY);
			///////////////////////////////////////////// CUBOID /////////////////////////////////////////////
			} else if (arenaSection.contains("Cuboid")){
				arenaSection = arenasConfig.getConfigurationSection("arenas." + arenaName + ".Cuboid");
				BlockVector min = YML.getBlockVector(arenaSection.getString("min"));
				BlockVector max = YML.getBlockVector(arenaSection.getString("max"));
				region = new CuboidRegion (BukkitUtil.getLocalWorld(world), min, max);
			}
			if(region == null)
				Messenger.warning("Failed to grab region for arena '" + arenaName + "' in arenas.yml!");
			return region;

			//Messenger.severe("Failed to build AbstractRegion from YML in arena '" + arenaName + "'.");

	}
	public static void saveSpawns(String arenaName, String spawnType, ArrayList<Location> spawns){
		try{
			String path = "arenas." + arenaName + ".Spawns." + spawnType;
			// deleting the section
			arenasConfig.set(path, null);
			// creating the section
			ConfigurationSection arenaSection = arenasConfig.createSection(path);
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
	public static ArrayList<Location> grabSpawns(String arenaName, String spawnType){
		try{
			ArrayList<Location> spawns = new ArrayList<Location>();
			String path = "arenas." + arenaName;
			ConfigurationSection arenaSection = arenasConfig.getConfigurationSection(path);
			World world = Bukkit.getWorld(arenaSection.getString("world"));
			path = "arenas." + arenaName + ".Spawns";
			arenaSection = arenasConfig.getConfigurationSection(path);
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

	
	
}
