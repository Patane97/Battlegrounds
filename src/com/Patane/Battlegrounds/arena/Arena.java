package com.Patane.Battlegrounds.arena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.modes.ArenaMode;
import com.Patane.Battlegrounds.arena.modes.Standby;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.playerData.PlayerData;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.AbstractRegion;

public class Arena {
	protected Plugin plugin;
	
	protected String name;
	protected World world;	
	protected AbstractRegion region;
	protected ArrayList<Location> gameSpawns;
	protected ArrayList<Location> creatureSpawns;
	protected ArrayList<Location> lobbySpawns;
	protected ArrayList<Location> spectatorSpawns;
	
	protected ArrayList<BGClass> classes;
	
	// <PlayerName, ClassName>
	public HashMap<String, String> playerClasses = new HashMap<String, String>();
	
	ArenaMode mode;
	// map of players <String name, Boolean value>
	// 'value' is used for different things in different modes.
	// Lobby: value = ready/not ready
	// Game: value = alive/dead
	public HashMap<String, Boolean> players = new HashMap<String, Boolean>();
	
	
	public Arena(){
		
	}
	/**
	 * Mostly used for creating new arenas in-game (before spawns have been saved)
	 */
	public Arena(Plugin plugin, String name, World world, AbstractRegion region){
		this(plugin, name, world, region, null, null, null, null, null);
	}
	/**
	 * Mostly used with loading arenas from arenas.yml
	 */
	public Arena(Plugin plugin, String name, World world, AbstractRegion region, 
			ArrayList<Location> gameSpawns, ArrayList<Location> lobbySpawns, 
			ArrayList<Location> creatureSpawns, ArrayList<Location> spectatorSpawns,
			ArrayList<BGClass> classes){
		if(Arenas.contains(name)){
			throw new NullPointerException("Tried to create an arena that already exists! (" + name + ")");
		}
		this.plugin 			= plugin;
		this.name 				= name;
		this.world 				= world;
		this.region 			= region;
		this.gameSpawns 		= (gameSpawns 		== null ? new ArrayList<Location>() : gameSpawns);
		this.lobbySpawns 		= (lobbySpawns 		== null ? new ArrayList<Location>() : lobbySpawns);
		this.creatureSpawns		= (creatureSpawns 	== null ? new ArrayList<Location>() : creatureSpawns);
		this.spectatorSpawns 	= (spectatorSpawns 	== null ? new ArrayList<Location>() : spectatorSpawns);
		this.classes 			= (classes 			== null ? new ArrayList<BGClass>() : classes);
		this.mode 				= new Standby(plugin, this);
		Arenas.add(this);
	}
	
	public String getName(){
		return name;
	}
	public World getWorld(){
		return world;
	}
	public AbstractRegion getRegion(){
		return region;
	}
	public ArrayList<BGClass> getClasses() {
		return classes;
	}
	public ArenaMode getMode() {
		return mode;
	}
	public ArenaMode setMode(ArenaMode mode) {
		try{
			this.mode.getListener().unregister();
		} catch (NullPointerException e){}
		this.mode = mode;
		return this.mode;
	}
	/**
	 * @return List of players active in this arena
	 */
	public ArrayList<String> getPlayers(){
		ArrayList<String> playerStrings = new ArrayList<String>();
		playerStrings.addAll(players.keySet());
		return playerStrings;
	}
	/**
	 * 
	 * @param player to change value
	 * @param value
	 */
	public void putPlayer(Player player, boolean value){
		players.put(player.getDisplayName(), value);
	}
	/**
	 * 
	 * @param player to get value
	 * @return true if player is currently alive in the arena
	 */
	public boolean getPlayerStatus(Player player){
		return players.get(player.getDisplayName());
	}
	/**
	 * @return true if any players have the inputted value (hashmap)
	 */
	public boolean anyPlayers(boolean value){
		if(players.containsValue(value))
			return true;
		return false;
	}
	/**
	 * 
	 * @param player removed
	 * @param check if session over
	 * @return true if player was removed from Hashmap
	 */
	public boolean removePlayer(String player, boolean check){
		if(players.remove(player) != null){
			playerClasses.remove(player);
			PlayerData.restoreData(Bukkit.getPlayerExact(player));
			if(check && mode.checkSessionOver())
				mode.sessionOver();
			return true;
		}
		return false;
	}
	/**
	 * Finds & removes respective spawns. Returns name of spawn type removed.
	 * 
	 * @param location of possible spawn block
	 * @return Name of spawn type at location. Null if there is no spawn block at location.
	 */
	public String findRemoveSpawn(Location location){
		for(Location selectedLocation : getAllSpawns()){
			if(location.getBlockX() == selectedLocation.getBlockX() && location.getBlockY() == selectedLocation.getBlockY() && location.getBlockZ() == selectedLocation.getBlockZ()){
				String blockName = null;
				if(gameSpawns.remove(selectedLocation))
					blockName = "&7Game Spawn";
				else if(lobbySpawns.remove(selectedLocation))
					blockName = "&9Lobby Spawn";
				else if(getCreatureSpawns().remove(selectedLocation))
					blockName = "&4Creature Spawn";
				else if(spectatorSpawns.remove(selectedLocation))
					blockName = "&2Spectator Spawn";

				return blockName;
			}
		}
		return null;
	}
	/**
	 * @return All spawns for this arena
	 */
	public ArrayList<Location> getAllSpawns() {
		ArrayList<Location> allSpawns = new ArrayList<Location>();
		allSpawns.addAll(gameSpawns);
		allSpawns.addAll(lobbySpawns);
		allSpawns.addAll(getCreatureSpawns());
		allSpawns.addAll(spectatorSpawns);
		return allSpawns;
	}
	/**
	 * 
	 * @return true if there are any empty spawn lists
	 */
	public boolean hasEmptySpawns() {
		if(!getEmptySpawnLists().isEmpty())
			return true;
		return false;
	}
	/**
	 * Checks if an spawn lists are empty and returns their name if they are
	 * 
	 * @return ArrayList of strings representing which spawns are empty.
	 */
	public ArrayList<String> getEmptySpawnLists(){
		ArrayList<String> emptySpawns = new ArrayList<String>();
		if(gameSpawns.isEmpty())
			emptySpawns.add("&7Game");
		if(lobbySpawns.isEmpty())
			emptySpawns.add("&9Lobby");
		if(getCreatureSpawns().isEmpty())
			emptySpawns.add("&4Creatures");
		if(spectatorSpawns.isEmpty())
			emptySpawns.add("&2Spectators");
		
		return emptySpawns;
	}
	
	/**
	 * @return true if the given location is within the arena
	 */
	public boolean isWithin(Location location){
		Vector vector = new Vector(location.getX()-0.5, location.getY()-0.5, location.getZ()-0.5);
		return region.contains(vector);
	}
	/**
	 * 
	 * @param player
	 * @return true if the player is within the arena instance
	 */
	public boolean hasPlayer(Player player) {
		if(players.containsKey(player.getDisplayName()))
			return true;
		return false;
	}
	public ArrayList<Location> getGameSpawns(){
		return gameSpawns;
	}
	public ArrayList<Location> getLobbySpawns(){
		return lobbySpawns;
	}
	public ArrayList<Location> getCreatureSpawns(){
		return creatureSpawns;
	}
	public ArrayList<Location> getSpectatorSpawns(){
		return spectatorSpawns;
	}
	public void clean() {
		clearSpawnBlocks(false);
	}

	/**
	 * Clears all spawn block locations
	 */
	public void clearSpawnBlocks(boolean effect){
		for(Location spawnLocation : getAllSpawns()){
			Block block = world.getBlockAt(spawnLocation.getBlockX(),spawnLocation.getBlockY(),spawnLocation.getBlockZ());
			block.setType(Material.AIR);
			Location particleLocation = new Location(spawnLocation.getWorld(), spawnLocation.getX(), spawnLocation.getY()+0.5, spawnLocation.getZ());
			if(effect)
				particleLocation.getWorld().spawnParticle(Particle.CLOUD, particleLocation, 10, 0, 0, 0, 0.05);
		}
	}
	public BGClass newClass(ItemStack classIcon, String className) {
		BGClass newClass = new BGClass(plugin, className, classIcon);
		classes.add(newClass);
		return newClass;
	}
	public BGClass newClass(ItemStack classIcon, String className, ItemStack[] items) {
		BGClass newClass = new BGClass(plugin, className, classIcon, items);
		classes.add(newClass);
		return newClass;
	}
	public BGClass getClass(String className) {
		for(BGClass selectedClass : classes){
			if(ChatColor.stripColor(selectedClass.getName()).equalsIgnoreCase(className))
				return selectedClass;
		}
		return null;
	}
	public void equipClass(Player player, BGClass bgClass) {
		ItemStack[] inv 		= bgClass.getInventory().getContents();
		int invLength = inv.length;
		ItemStack helmet 		= inv[0];
		ItemStack chestplate 	= inv[1];
		ItemStack leggings 		= inv[2];
		ItemStack boots 		= inv[3];
		ItemStack offHand 		= inv[4];
		ItemStack[] storage 	= Arrays.copyOfRange(inv, 9, invLength-9);
		ItemStack[] hotbar 		= Arrays.copyOfRange(inv, invLength-9, invLength);

		player.getInventory().setHelmet(helmet);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setLeggings(leggings);
		player.getInventory().setBoots(boots);
		
		player.getInventory().setItemInOffHand(offHand);
		player.getInventory().setStorageContents(storage);
		for(int i = 0 ; i < storage.length ; i++){
			player.getInventory().setItem(i+9, storage[i]);
		}
		for(int i = 0 ; i < hotbar.length ; i++){
			player.getInventory().setItem(i, hotbar[i]);
		}
		
		playerClasses.put(player.getDisplayName(), bgClass.getName());
		
	}
	public boolean hasClass(Player player) {
		if(playerClasses.get(player.getDisplayName()) != null)
			return true;
		return false;
	}
	public void removeClass(BGClass bgClass) {
		classes.remove(bgClass);
		ArenaYML.removeClass(name, bgClass.getName());
	}
}
