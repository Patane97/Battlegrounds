package com.Patane.Battlegrounds.arena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.classes.BGClass;
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
		if(!arenasConfig.isConfigurationSection("arenas"))
			arenasConfig.createSection("arenas");
		Arenas.addAll(loadAllArenas());

	}
	
	public static ArrayList<Arena> loadAllArenas(){

		ArrayList<Arena> arenas = new ArrayList<Arena>();
		ConfigurationSection arenaHeader = arenasConfig.getConfigurationSection("arenas");
		for(String arenaName : arenaHeader.getKeys(false)){
			Arena arena = loadArena(arenaName);
			if(arena == null){
				Messenger.warning("Arena '" + arenaName + "' not recognised in arenas.yml!");
			}
		}
		return arenas;
	}
	public static void save(Arena arena) {
		String arenaName = arena.getName();
		saveAllSpawns(arenaName);
		saveRegion(arenaName, arena.getWorld(), arena.getRegion());
	}
	public static Arena loadArena (String arenaName){

		ConfigurationSection arenaSection = arenasConfig.getConfigurationSection("arenas." + arenaName);
		// getting the world
		World world = Bukkit.getWorld(arenaSection.getString("world"));
		// getting the region
		AbstractRegion region = grabRegion(world, arenaName);
		ArrayList<Location> gameSpawns = grabSpawns(arenaName, "Game");
		ArrayList<Location> lobbySpawns = grabSpawns(arenaName, "Lobby");
		ArrayList<Location> creatureSpawns = grabSpawns(arenaName, "Creature");
		ArrayList<Location> spectatorSpawns = grabSpawns(arenaName, "Spectator");
		ArrayList<BGClass> classes = loadClasses(arenaName);
		
		Arena loadedArena = new Arena(plugin, arenaName, world, region, gameSpawns, lobbySpawns, creatureSpawns, spectatorSpawns, classes);
		Messenger.info("Arena '" + arenaName +"' successfully loaded!");
		return loadedArena;
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

	public static boolean saveRegion(String arenaName, World world, AbstractRegion selectedRegion) {
		try{
			ConfigurationSection arenaSection = arenasConfig.createSection("arenas." + arenaName);
			arenaSection.set("Poly", null);
			arenaSection.set("Cuboid", null);
			arenaSection.set("world", world.getName());
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
					String blockVectorString = arenaSection.getString(blockVectorNumber);
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
			ConfigurationSection arenaSection = arenasConfig.getConfigurationSection(path);
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

	public static void saveClasses(String arenaName){
		Arena arena = Arenas.grab(arenaName);
		if(arena == null)
			return;
		
		ConfigurationSection arenaSection = arenasConfig.getConfigurationSection("arenas." + arenaName);
		arenaSection.set("classes", null);
		arenaSection = arenasConfig.createSection("arenas." + arenaName + ".classes");
		for(BGClass selectedClass : arena.getClasses()){
			ItemStack[] inv 		= selectedClass.getInventory().getContents();
			ItemStack[] armor 		= new ItemStack[4];
			armor 					= Arrays.copyOfRange(inv, 0, 4);
			ItemStack offHand 		= inv[4];
//			ItemStack[] armor 		= {inv[0], inv[1], inv[2], inv[3]};
			ItemStack[] contents 	= new ItemStack[36];
			contents 				= Arrays.copyOfRange(inv, 9, inv.length);
			
			arenaSection.set(selectedClass.getName() + ".Icon", selectedClass.getIcon());
			arenaSection.set(selectedClass.getName() + ".Armor", armor);
			arenaSection.set(selectedClass.getName() + ".OffHand", offHand);
			Messenger.info("INV SIZE: "+inv.length);
			Messenger.info("ARMOR SIZE: "+armor.length);
			Messenger.info("CONTENTS SIZE: "+contents.length);
			for(Entry<Integer, ItemStack> entry : YML.playerInventoryContentsFormat(contents).entrySet())
				arenaSection.set(selectedClass.getName() + ".Contents." + entry.getKey(), entry.getValue());
		}
		arenasConfig.save();
	}
	public static ArrayList<BGClass> loadClasses(String arenaName){
		ArrayList<BGClass> classes = new ArrayList<BGClass>();
		ConfigurationSection arenaSection = arenasConfig.getConfigurationSection("arenas." + arenaName + ".classes");
		if(arenaSection == null)
			return null;
		String path;
		for(String selectedClass : arenaSection.getKeys(false)){
			path = "arenas." + arenaName + ".classes." + selectedClass;
			arenaSection = arenasConfig.getConfigurationSection(path);
			ItemStack icon = arenaSection.getItemStack("Icon");

			@SuppressWarnings("unchecked")
			List<ItemStack> itemList = (List<ItemStack>) arenaSection.get("Armor");
			ItemStack[] armor = (ItemStack[]) itemList.toArray(new ItemStack[0]);
			
			ItemStack offHand = arenaSection.getItemStack("OffHand");
			
			ItemStack[] effects = new ItemStack[4];
			
			arenaSection = arenasConfig.getConfigurationSection(path + ".Contents");
			arenaSection = (arenaSection == null ? arenasConfig.createSection(path + ".Contents") : arenaSection);
			HashMap<Integer, ItemStack> printedItems = new HashMap<Integer, ItemStack>();
			for(String slotNo : arenaSection.getKeys(false))
				printedItems.put(Integer.parseInt(slotNo), arenaSection.getItemStack(slotNo));
			ItemStack[] contents 	= YML.getPlayerInventoryContents(printedItems);
			
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			items.addAll(Arrays.asList(armor));
			items.add(offHand);
			items.addAll(Arrays.asList(effects));
			items.addAll(Arrays.asList(contents));
			
			ItemStack[] classInventory = items.toArray(new ItemStack[0]);
			
			BGClass newClass = new BGClass(plugin, selectedClass, icon, classInventory);
			classes.add(newClass);
			Messenger.info("Loaded Class: " + newClass.getName());
		}
		return classes;
	}
	public static void removeClass(String arenaName, String className){
		ConfigurationSection arenaSection = arenasConfig.getConfigurationSection("arenas." + arenaName + ".classes");
		if(arenaSection == null || arenaSection.get(className) == null)
			return;
		arenaSection.set(className, null);
	}
	
}
