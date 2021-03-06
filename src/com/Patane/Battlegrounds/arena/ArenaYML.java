package com.Patane.Battlegrounds.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.BasicYML;
import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.Messenger.ChatType;
import com.Patane.Battlegrounds.arena.game.waves.Wave;
import com.Patane.Battlegrounds.arena.settings.Setting;
import com.Patane.Battlegrounds.arena.settings.Setting.SettingType;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.util.YML;
import com.Patane.Battlegrounds.util.util;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;

public class ArenaYML extends BasicYML{
	
	public ArenaYML(Plugin plugin){
		super(plugin, "arenas.yml", "arenas");
	}
	
	@Override
	public void save(){
		for(Arena selectedArena : Arenas.get())
			save(selectedArena);
		Messenger.info("Successfully saved Arenas: " + util.stringJoiner(Arenas.getNames(false), ", "));
	}
	
	@Override
	public void load(){
		for(String arenaName : header.getKeys(false))
			load(arenaName);
		Messenger.info("Successfully loaded Arenas: " + util.stringJoiner(Arenas.getNames(false), ", "));
	}

	private void save(Arena arena) {
		String arenaName = arena.getName();
		setHeader(clearCreateSection(arenaName));
		saveWorld(arenaName, arena.getWorld());
		try{saveRegion(arenaName, arena.getGround(), "Ground");} catch(Exception e){}
		try{saveRegion(arenaName, arena.getLobby(), "Lobby");} catch(Exception e){}
		saveAllSpawns(arenaName);
		saveClasses(arenaName);
		saveSettings(arenaName);
		Messenger.debug(ChatType.INFO, "Successfully saved Arena: " + arenaName + ".");
	}
	
	private Arena load(String arenaName){
		try{
			setHeader(arenaName);
			World world;
			try{ 
				world = Bukkit.getWorld(header.getString("World"));
			} catch (IllegalArgumentException e){
				Messenger.warning("Failed to load Arena: " + arenaName +". Failed to grab world from 'World' in YML.");
				e.printStackTrace();
				return null;
			}
			AbstractRegion ground = grabRegion(world, arenaName, "Ground");
			AbstractRegion lobby = grabRegion(world, arenaName, "Lobby");
			ArrayList<Location> gameSpawns = grabSpawns(arenaName, "Game");
			ArrayList<Location> lobbySpawns = grabSpawns(arenaName, "Lobby");
			ArrayList<Location> creatureSpawns = grabSpawns(arenaName, "Creature");
			ArrayList<Location> spectatorSpawns = grabSpawns(arenaName, "Spectator");
			ArrayList<String> classes = loadClasses(arenaName);
			HashMap<String, Object> settings = getSettings(arenaName);
			ArrayList<Wave> waves = null;
			
			Arena loadedArena = new Arena(plugin, arenaName, world, ground, lobby, gameSpawns, lobbySpawns, creatureSpawns, spectatorSpawns, classes, settings, waves);
			Messenger.debug(ChatType.INFO, "Successfully loaded Arena: " + arenaName + ".");
			return loadedArena;
		} catch (Exception e){
			Messenger.warning("Error loading Arena: " + arenaName + ".");
			e.printStackTrace();
			return null;
		}
	}
	public boolean saveWorld(String arenaName, World world){
		header.set("World", world.getName());
		return true;
	}
	public boolean saveRegion(String arenaName, AbstractRegion selectedRegion, String type) {
		try{
			if(type == null || !Chat.hasAlpha(type))
				return false;
			setHeader(arenaName, type);
			header.set("Poly", null);
			header.set("Cuboid", null);
			if(selectedRegion instanceof Polygonal2DRegion){
				Polygonal2DRegion region = (Polygonal2DRegion) selectedRegion;
				header.set("Poly.minY", region.getMinimumY());
				header.set("Poly.maxY", region.getMaximumY());
				List<BlockVector2D> tempArray = region.getPoints();
				for(int i=1 ; i <= region.getPoints().size() ; i++){
					header.set("Poly.Vectors."+i, YML.blockVector2DFormat(tempArray.get(i-1)));
				}
			} else if(selectedRegion instanceof CuboidRegion){
				CuboidRegion region = (CuboidRegion) selectedRegion;
				header.set("Cuboid.min", YML.blockVectorFormat(region.getMinimumPoint().toBlockPoint()));
				header.set("Cuboid.max", YML.blockVectorFormat(region.getMaximumPoint().toBlockPoint()));
			}
			config.save();
			Messenger.debug(ChatType.INFO, "Saved " + arenaName + "'s " + type + " region!");
			return true;
		} catch (Exception e){
			// >>>>>>>>>>>>>>>>>>>>>>>>>>> IMPORTANT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			// >>>>>>>>>>>>>>>>>>>>>>>>>>> IMPORTANT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			// >>>>>>>>>>>>>>>>>>>>>>>>>>> IMPORTANT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			// >>>>>>>>>>>>>>>>>>>>>>>>>>> IMPORTANT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			// CHANGE BELOW TO SIMPLY DISABLE THE ARENA. ARENAS CAN NOW STILL BE UP WITHOUT REGIONS!!!!!
			// >>>>>>>>>>>>>>>>>>>>>>>>>>> IMPORTANT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			// >>>>>>>>>>>>>>>>>>>>>>>>>>> IMPORTANT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			// >>>>>>>>>>>>>>>>>>>>>>>>>>> IMPORTANT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			// >>>>>>>>>>>>>>>>>>>>>>>>>>> IMPORTANT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			Messenger.severe("[OUTDATED ACTION] Failed to save arena region. Deleting " + arenaName + ".");
			Arenas.remove(Arenas.grab(arenaName));
			e.printStackTrace();
			return false;
		}
	}
	@SuppressWarnings("deprecation")
	public AbstractRegion grabRegion(World world, String arenaName, String regionType){

			AbstractRegion region = null;
			setHeader(arenaName, regionType);
			///////////////////////////////////////////// POLYGONAL /////////////////////////////////////////////
			if(header.contains("Poly")){
				int minY = header.getInt("Poly.minY");
				int maxY = header.getInt("Poly.maxY");
				setHeader(arenaName + ".Poly", "Vectors");
				List<BlockVector2D> blockVectors = new ArrayList<BlockVector2D>();
				for(String blockVectorNumber : header.getKeys(false)){
					String blockVectorString = header.getString(blockVectorNumber);
					BlockVector2D blockVector = YML.getBlockVector2D(blockVectorString);
					blockVectors.add(blockVector);
				}
				region = new Polygonal2DRegion(BukkitUtil.getLocalWorld(world), blockVectors, minY, maxY);
			///////////////////////////////////////////// CUBOID /////////////////////////////////////////////
			} else if (header.contains("Cuboid")){
				BlockVector min = YML.getBlockVector(header.getString("Cuboid.min"));
				BlockVector max = YML.getBlockVector(header.getString("Cuboid.max"));
				region = new CuboidRegion (BukkitUtil.getLocalWorld(world), min, max);
			}
			if(region == null)
				Messenger.warning("Failed to find " + arenaName + "'s " + regionType + " region.");
			return region;
	}
	public void saveSpawns(String arenaName, String spawnType, ArrayList<Location> spawns){
		try{
			setHeader(clearCreateSection(arenaName, "Spawns", spawnType));
			int number = 1;
			for(Location spawnLocation : spawns){
				header.set(Integer.toString(number), YML.spawnLocationFormat(spawnLocation, true));
				number ++;
			}
			Messenger.debug(ChatType.INFO, "Saved " + arenaName + "'s " + spawnType + " spawns!");
			config.save();
		} catch (Exception e){
			Messenger.severe("Error whilst saving " + arenaName + "'s " + spawnType + " Spawns.");
			e.printStackTrace();
		}
	}
	public void saveAllSpawns(String arenaName){
		Arena arena = Arenas.grab(arenaName);
		saveSpawns(arenaName, "Game" , arena.getGameSpawns());
		saveSpawns(arenaName, "Lobby" , arena.getLobbySpawns());
		saveSpawns(arenaName, "Creature" , arena.getCreatureSpawns());
		saveSpawns(arenaName, "Spectator" , arena.getSpectatorSpawns());
	}
	public ArrayList<Location> grabSpawns(String arenaName, String spawnType){
		try{
			setHeader(arenaName);
			if(header.contains("Spawns." + spawnType)){
				World world = Bukkit.getWorld(header.getString("World"));
				
				setHeader(arenaName, "Spawns", spawnType);				
				ArrayList<Location> spawns = new ArrayList<Location>();
				for(String spawnNumber : header.getKeys(false)){
					Location spawn = YML.getSpawnLocation(world, header.getString(spawnNumber));
					spawns.add(spawn);
				}
				return spawns;
			}
			Messenger.warning("Failed to find " + arenaName + "'s " + spawnType + " Spawns.");
			return null;
		} catch (Exception e){
			Messenger.severe("Error whilst grabbing " + arenaName + "'s " + spawnType + " Spawns.");
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String> loadClasses(String arenaName){
		setHeader(arenaName);
		@SuppressWarnings("unchecked")
		ArrayList<String> temp = (ArrayList<String>) header.getList("Classes", new ArrayList<String>());
		return temp;
	}
	public void saveClasses(String arenaName){
		setHeader(arenaName);
		Arena arena = Arenas.grab(arenaName);
		header.set("Classes", arena.getClasses());
		Messenger.debug(ChatType.INFO, "Saved " + arenaName + "'s classes!");
		config.save();
	}
	
	private void saveBooleanSetting(String arenaName, Setting setting, boolean value){
		if((boolean) setting.getDefault() == value)
			header.set(setting.getYmlName(), null);
		else
			header.set(setting.getYmlName(), value);
		config.save();
	}
	private void saveIntSetting(String arenaName, Setting setting, int value){
		if((int) setting.getMin() >= value)
			value = (int) setting.getMin();
		if((int) setting.getDefault() == value)
			header.set(setting.getYmlName(), null);
		else
			header.set(setting.getYmlName(), value);
		config.save();
	}
	private void saveFloatSetting(String arenaName, Setting setting, float value){
		if((float) setting.getMin() >= value)
			value = (float) setting.getMin();
		if((float) setting.getDefault() == value)
			header.set(setting.getYmlName(), null);
		else
			header.set(setting.getYmlName(), value);
		config.save();
	}
	private boolean getBooleanSetting(String arenaName, Setting setting){
		return header.getBoolean(setting.getYmlName(), (boolean) setting.getDefault());
	}
	private int getIntSetting(String arenaName, Setting setting){
		return header.getInt(setting.getYmlName(), (int) setting.getDefault());
	}
	private float getFloatSetting(String arenaName, Setting setting){
		return (float) header.getDouble(setting.getYmlName(), (float) setting.getDefault());
	}

	public Object saveSetting(Arena arena, String ymlName) {
		return saveSetting(arena.getName(), Setting.getFromName(ymlName), arena.getSetting(ymlName));
	}
	public Object saveSetting(String arenaName, Setting setting, Object value) {
		setHeader(arenaName + ".Settings");
		if(setting.getType() == SettingType.BOOLEAN)
			saveBooleanSetting(arenaName, setting, (boolean) value);
		if(setting.getType() == SettingType.INTEGER)
			saveIntSetting(arenaName, setting, (int) value);
		if(setting.getType() == SettingType.FLOAT)
			saveFloatSetting(arenaName, setting, (float) value);
		Messenger.debug(ChatType.INFO, "Successfully saved Setting: "+setting.getYmlName()+" as '"+value+"'.");
		return getSetting(arenaName, setting);
	}
	public Object getSetting(String arenaName, Setting setting) {
		setHeader(arenaName + ".Settings");
		if(setting.getType() == SettingType.BOOLEAN)
			return getBooleanSetting(arenaName, setting);
		if(setting.getType() == SettingType.INTEGER)
			return getIntSetting(arenaName, setting);
		if(setting.getType() == SettingType.FLOAT)
			return getFloatSetting(arenaName, setting);
		return null;
	}
	public void saveSettings(String arenaName) {
		setHeader(arenaName + ".Settings");
		Arena arena = Arenas.grab(arenaName);
		for(String ymlName : arena.getCustomSettingsKeySet()){
			header.set(ymlName, arena.getSetting(ymlName));
		}
		config.save();
	}
	public HashMap<String, Object> getSettings(String arenaName) {
		setHeader(arenaName + ".Settings");
		HashMap<String, Object> settings = new HashMap<String, Object>();
		for(String ymlName : header.getKeys(false)){
			Setting setting = Setting.getFromName(ymlName);
			// just in case the key in yml are not a valid ymlName...
			if(setting == null) continue;
			settings.put(ymlName, getSetting(arenaName, setting));
		}
		return settings;
	}
	
}
