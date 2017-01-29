package com.Patane.Battlegrounds.arena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.standby.ArenaMode;
import com.Patane.Battlegrounds.arena.standby.Standby;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.collections.Classes;
import com.Patane.Battlegrounds.listeners.ArenaListener;
import com.Patane.Battlegrounds.playerData.PlayerData;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.AbstractRegion;

import net.md_5.bungee.api.ChatColor;

public class Arena {
	protected Plugin plugin;
	
	protected String name;
	protected World world;	
	protected AbstractRegion ground;
	protected AbstractRegion lobby;
	
	protected ArrayList<Location> gameSpawns;
	protected ArrayList<Location> creatureSpawns;
	protected ArrayList<Location> lobbySpawns;
	protected ArrayList<Location> spectatorSpawns;
	
	protected ArrayList<String> classes;
	
	protected ArenaListener arenaListener;
	
	// <PlayerName, ClassName>
	public HashMap<String, String> playerClasses = new HashMap<String, String>();
	
	ArenaMode mode;
	// map of players <String name, Boolean value>
	// 'value' is used for different things in different modes.
	// Lobby: value = ready/not ready
	// Game: value = alive/dead
	public HashMap<String, Boolean> players = new HashMap<String, Boolean>();
	
	
	public Arena(){}
	/**
	 * Mostly used for creating new arenas in-game (before spawns have been saved)
	 */
	public Arena(Plugin plugin, String name, World world, AbstractRegion region){
		this(plugin, name, world, region, null, null, null, null, null, null);
	}
	/**
	 * Mostly used with loading arenas from arenas.yml
	 */
	public Arena(Plugin plugin, String name, World world, AbstractRegion ground, 
			AbstractRegion lobby, ArrayList<Location> gameSpawns, ArrayList<Location> lobbySpawns, 
			ArrayList<Location> creatureSpawns, ArrayList<Location> spectatorSpawns,
			ArrayList<String> classes){
		if(Arenas.contains(name)){
			throw new NullPointerException("Tried to create an arena that already exists! (" + name + ")");
		}
		this.plugin 			= plugin;
		this.name 				= name;
		this.world 				= world;
		this.ground 			= ground;
		this.lobby 				= lobby;
		this.gameSpawns 		= (gameSpawns 		== null ? new ArrayList<Location>() : gameSpawns);
		this.lobbySpawns 		= (lobbySpawns 		== null ? new ArrayList<Location>() : lobbySpawns);
		this.creatureSpawns		= (creatureSpawns 	== null ? new ArrayList<Location>() : creatureSpawns);
		this.spectatorSpawns 	= (spectatorSpawns 	== null ? new ArrayList<Location>() : spectatorSpawns);
		this.classes 			= (classes 			== null ? new ArrayList<String>() : classes);
		syncClasses();
		this.mode 				= new Standby(plugin, this);
		Arenas.add(this);
	}
	
	public String getName(){
		return name;
	}
	public World getWorld(){
		return world;
	}
	public AbstractRegion getGround(){
		return ground;
	}
	public AbstractRegion getLobby(){
		return lobby;
	}
	public void setGround(AbstractRegion region){
		this.ground = region;
	}
	public void setLobby(AbstractRegion region){
		this.lobby = region;
	}
	public ArenaMode getMode() {
		return mode;
	}
	public ArenaMode setMode(ArenaMode mode) {
		this.mode.unregister();
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
	public int howManyPlayers(boolean value){
		int count = 0;
		for(boolean selectedValue : players.values()){
			if(value == selectedValue)
				count++;
		}
		return count;
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
			((Standby) mode).setAllLevel(players.size());
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
	 * True if the arena is not missing any regions or spawns.
	 * 
	 */
	public boolean isActive(){
		if(!hasGround())
			return false;
		if(!hasLobby())
			return false;
		if(hasEmptySpawns())
			return false;
		if(classes.isEmpty())
			return false;
		return true;
	}
	public boolean hasGround(){
		if(ground != null)
			return true;
		return false;
	}
	public boolean hasLobby(){
		if(lobby != null)
			return true;
		return false;
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
	 * 
	 * @return 0: Outside both battleground and lobby | 1: Inside battleground | 2: Inside lobby
	 */
	public int isWithin(Block block){
		Location location = block.getLocation();
		Vector vector = new Vector(location.getX(), location.getY(), location.getZ());
		if(ground != null && ground.contains(vector))
			return 1;
		if(lobby != null && lobby.contains(vector))
			return 2;
		return 0;
	}
	public int isWithin(Entity entity){
		Location location = entity.getLocation();
		Vector vector = new Vector(location.getX(), location.getY(), location.getZ());
		if(ground != null && ground.contains(vector))
			return 1;
		if(lobby != null && lobby.contains(vector))
			return 2;
		return 0;
	}
	public int isWithin(Location location) {
		Vector vector = new Vector(location.getX(), location.getY(), location.getZ());
		if(ground != null && ground.contains(vector))
			return 1;
		if(lobby != null && lobby.contains(vector))
			return 2;
		return 0;
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
	public void equipClass(Player player, BGClass bgClass) {
		if(bgClass == null || playerClasses.get(player.getDisplayName()) == bgClass.getName())
			return;
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
		Messenger.send(player, "&aYou have chosen the &7" + bgClass.getName() + "&a class.");
		
	}
	public ArrayList<String> getClasses() {
		return classes;
	}
	public void addClass(BGClass newClass) {
		classes.add(newClass.getName());
		if(!Classes.contains(newClass.getName()))
			Classes.add(newClass, true);
	}
	public boolean hasClass(String className) {
		for(String selectedClass : classes){
			if(selectedClass.equalsIgnoreCase(ChatColor.stripColor(className)))
				return true;
		}
		return false;
	}
	public boolean playerHasClass(Player player) {
		if(playerClasses.get(player.getDisplayName()) != null)
			return true;
		return false;
	}
	public void removeClass(String className) {
		if(classes.remove(className))
			ArenaYML.saveClasses(name);
	}
	private void syncClasses() {
		List<String> temp = new ArrayList<String>();
		temp.addAll(classes);
		for(String className : temp){
			if(!Classes.contains(className)){
				Messenger.warning("Class " + className + " does not match CLASSES list");
				classes.remove(className);
			}
		} //ArenaYML.saveClasses(name);
	}
}
