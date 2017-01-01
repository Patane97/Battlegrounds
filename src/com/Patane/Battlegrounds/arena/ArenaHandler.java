package com.Patane.Battlegrounds.arena;


import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.collections.GameInstances;
import com.Patane.Battlegrounds.collections.LobbyInstances;
import com.Patane.Battlegrounds.playerData.PlayerData;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.AbstractRegion;


public class ArenaHandler implements ArenaManager{
	Plugin plugin;
	
	String name;
	World world;	
	AbstractRegion region;
	ArrayList<Location> playerSpawns;
	ArrayList<Location> creatureSpawns;
	ArrayList<Location> lobbySpawns;
	ArrayList<Location> spectatorSpawns;
	boolean inGame 		= false;
	boolean inLobby 	= false;
	boolean editMode 	= false;
	
	/**
	 * Mostly used for creating new arenas in-game (before spawns have been saved)
	 * 
	 * @param plugin Server Plugin
	 * @param name Name of Arena
	 * @param world Arenas world
	 * @param region Region to save this arena into. Polynomal or Cuboid.
	 */
	public ArenaHandler(Plugin plugin, String name, World world, AbstractRegion region){
		if(Arenas.alreadyArena(name)){
			throw new NullPointerException("Tried to create an arena that already exists! (" + name + ")");
		}
		this.plugin	= plugin;
		this.name	= name;
		this.world	= world;
		this.region	= region;
		this.playerSpawns = new ArrayList<Location>();
		this.lobbySpawns = new ArrayList<Location>();
		this.creatureSpawns = new ArrayList<Location>();
		this.spectatorSpawns = new ArrayList<Location>();
		Arenas.add(this);
	}
	/**
	 * Mostly used with loading arenas from arenas.yml
	 * 
	 * @param plugin Server Plugin
	 * @param name Name of Arena
	 * @param world Arenas world
	 * @param region Region to save this arena into. Polynomal or Cuboid.
	 * @param playerSpawns List of player spawns
	 * @param creatureSpawns List of creature spawns
	 * @param lobbySpawns List of lobby spawns
	 * @param spectatorSpawns List of spectator spawns
	 */
	public ArenaHandler(Plugin plugin, String name, World world, AbstractRegion region, ArrayList<Location> playerSpawns, ArrayList<Location> lobbySpawns, ArrayList<Location> creatureSpawns, ArrayList<Location> spectatorSpawns){
		if(Arenas.alreadyArena(name)){
			throw new NullPointerException("Tried to create an arena that already exists! (" + name + ")");
		}
		this.plugin 			= plugin;
		this.name 				= name;
		this.world 				= world;
		this.region 			= region;
		this.playerSpawns 		= (playerSpawns == null ? new ArrayList<Location>() : playerSpawns);
		this.lobbySpawns 		= (lobbySpawns == null ? new ArrayList<Location>() : lobbySpawns);
		this.creatureSpawns 	= (creatureSpawns == null ? new ArrayList<Location>() : creatureSpawns);
		this.spectatorSpawns 	= (spectatorSpawns == null ? new ArrayList<Location>() : spectatorSpawns);
		Arenas.add(this);
	}
	/*
	 * Setters/getters for arena being in game.
	 */
	public void setInGame(boolean inGame){
		this.inGame 	= inGame;
	}
	public boolean getInGame(){
		return inGame;
	}
	/*
	 * Setters/getters for arena being in lobby.
	 */
	public void setInLobby(boolean inLobby) {
		this.inLobby = inLobby;
	}
	public boolean getInLobby() {
		return inLobby;
	}
	/*
	 * Setters/getters for arena being in edit mode.
	 */
	public void setEditMode(boolean editMode){
		if(inLobby){
			LobbyInstances.getLobby(this).kickAll();
		}
		if(inGame){
			GameInstances.getGame(this).gameOver();
		}
		this.editMode = editMode;
	}
	public boolean getEditMode() {
		return editMode;
	}
	public String getName() {
		return name;
	}
	public World getWorld() {
		return world;
	}
	public boolean playerLeave(Player player){
		if()
		// backup checking if player is in game
		if(players.remove(player.getDisplayName()) != null){
			PlayerData.restoreData(player);
			if(check)
				checkGameEnd();
			return true;
		}
		return false;
	}
	public ArrayList<String> getPlayers(){
		ArrayList<String> playerNames = new ArrayList<String>();
		if(inLobby){
			for(String selectedPlayer : LobbyInstances.getLobby(this).getPlayerNames()){
				playerNames.add(selectedPlayer);
			}
		} else if(inGame){
			for(String selectedPlayer : GameInstances.getGame(this).getPlayerNames()){
				playerNames.add(selectedPlayer);
			}
		}
		return playerNames;
	}
	/*
	 * Checks if the arena is missing spawns
	 */
	public boolean missingSpawns() {
		if(!getEmptySpawnLists().isEmpty())
			return true;
		return false;
	}
	public ArrayList<Location> getPlayerSpawns(){
		return playerSpawns;
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
	/**
	 * Finds & removes respective spawns. Returns name of spawn type removed.
	 * 
	 * @param location Location of possible spawn block
	 * @return Name of spawn type at location. Null if there is no spawn block at location.
	 */
	public String findRemoveSpawn(Location location){
		for(Location selectedLocation : getAllSpawns()){
			if(location.getBlockX() == selectedLocation.getBlockX() && location.getBlockY() == selectedLocation.getBlockY() && location.getBlockZ() == selectedLocation.getBlockZ()){
				String blockName = null;
				if(playerSpawns.remove(selectedLocation))
					blockName = "&7Player Spawn";
				else if(lobbySpawns.remove(selectedLocation))
					blockName = "&9Lobby Spawn";
				else if(creatureSpawns.remove(selectedLocation))
					blockName = "&4Creature Spawn";
				else if(spectatorSpawns.remove(selectedLocation))
					blockName = "&2Spectator Spawn";

				return blockName;
			}
		}
		return null;
	}
	
	public boolean isWithinArena(Location location){
		Vector vector = new Vector(location.getX()-0.5, location.getY()-0.5, location.getZ()-0.5);
		return region.contains(vector);
	}
	
	public ArrayList<Location> getAllSpawns() {
		ArrayList<Location> allSpawns = new ArrayList<Location>();
		allSpawns.addAll(playerSpawns);
		allSpawns.addAll(lobbySpawns);
		allSpawns.addAll(creatureSpawns);
		allSpawns.addAll(spectatorSpawns);
		return allSpawns;
	}
	/**
	 * Checks if an spawn lists are empty and returns their name if they are
	 * 
	 * @return ArrayList of strings representing which spawns are empty.
	 */
	public ArrayList<String> getEmptySpawnLists(){
		ArrayList<String> emptySpawns = new ArrayList<String>();
		if(playerSpawns.isEmpty())
			emptySpawns.add("&7Players");
		if(lobbySpawns.isEmpty())
			emptySpawns.add("&9Lobby");
		if(creatureSpawns.isEmpty())
			emptySpawns.add("&4Creatures");
		if(spectatorSpawns.isEmpty())
			emptySpawns.add("&2Spectators");
		
		return emptySpawns;
	}
	public Location getPlayerSpawn(int spawnLocNo) {
		return playerSpawns.get(spawnLocNo);
	}
	public Location getLobbySpawn(int spawnLocNo) {
		return lobbySpawns.get(spawnLocNo);
	}
	public Location getCreatureSpawn(int spawnLocNo) {
		return creatureSpawns.get(spawnLocNo);
	}
	public Location getSpectatorSpawn(int spawnLocNo) {
		return spectatorSpawns.get(spawnLocNo);
	}
	
}
