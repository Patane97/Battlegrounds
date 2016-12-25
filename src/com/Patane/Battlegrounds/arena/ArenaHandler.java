package com.Patane.Battlegrounds.arena;


import org.bukkit.Location;
import org.bukkit.World;

import com.Patane.Battlegrounds.arena.builder.ArenaBuilder;

public class ArenaHandler implements ArenaManager{
	String name;
	
	boolean running = false;
	
	World world;
	
	Location arena1;
	Location arena2;
	
	Location lobby1;
	Location lobby2;
	
	Location playerSpawn;
	Location lobbySpawn;
	Location spectatorSpawn;
	
	Location creatureSpawn;
	
	public ArenaHandler(ArenaBuilder builder){
		this.name			= builder.arenaName;
		this.world 			= builder.world;
		this.arena1 		= builder.arena1;
		this.arena2			= builder.arena2;
		this.lobby1 		= builder.lobby1;
		this.lobby2			= builder.lobby2;
		this.playerSpawn 	= builder.playerSpawn;
		this.lobbySpawn		= builder.lobbySpawn;
		this.spectatorSpawn	= builder.spectatorSpawn;
		this.creatureSpawn 	= builder.creatureSpawn;
	}
	public ArenaHandler(String name, World world, Location arena1, Location arena2, Location lobby1, Location lobby2, Location playerSpawn, Location lobbySpawn, Location spectatorSpawn, Location creatureSpawn){
		this.name 			= name;
		this.world 			= world;
		this.arena1 		= arena1;
		this.arena2		 	= arena2;
		this.lobby1 		= lobby1;
		this.lobby2 		= lobby2;
		this.playerSpawn 	= playerSpawn;
		this.lobbySpawn 	= lobbySpawn;
		this.spectatorSpawn = spectatorSpawn;
		this.creatureSpawn 	= creatureSpawn;
	}
	
	public String getName() {
		return name;
	}
	public void isInGame(boolean inGame){
		this.running = inGame;
	}
	public boolean isInGame(){
		return running;
	}
	public Location getCreatureSpawn(){
		return creatureSpawn;
	}
	public World getWorld() {
		return world;
	}
	public Location getPlayerSpawn() {
		return playerSpawn;
	}
	public Location getSpectatorSpawn() {
		return spectatorSpawn;
	}
	
}
