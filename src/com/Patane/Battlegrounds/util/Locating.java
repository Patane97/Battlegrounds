package com.Patane.Battlegrounds.util;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Locating {

	public static Player findClosestPlayer(Entity entity, ArrayList<String> playerList){
		float closestDistance = Float.MAX_VALUE;
		Player player = null;
		Player temp;
		for(String selectedPlayer : playerList){
			temp = Bukkit.getPlayerExact(util.getStripDispName(selectedPlayer));
			if(temp.getLocation().distance(entity.getLocation()) <= closestDistance){
				player = temp;
			}
		}
		return player;
	}
	/**
	 * Used to get the middle and on top of a block (usually used for spawning entities correctly)
	 * 
	 * @param location
	 * @return centre/top of the parameter location's block
	 */
	public static Location centreOnBlock(Location location){
		return new Location(location.getWorld(), location.getX()+0.5, location.getY()+1, location.getZ()+0.5);
	}
}
