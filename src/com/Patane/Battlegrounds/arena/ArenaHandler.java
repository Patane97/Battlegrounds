package com.Patane.Battlegrounds.arena;

import java.util.ArrayList;

import org.bukkit.Location;

public class ArenaHandler implements ArenaManager{
	Location arena1;
	Location arena2;
	
	Location playerSpawn;
	
	ArrayList<Location> creatureSpawn;
	
	public boolean addCreatureSpawn(Location location) {
		return false;
	}
	
}
