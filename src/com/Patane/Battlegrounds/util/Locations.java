package com.Patane.Battlegrounds.util;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Locations {

	public static Player findClosestPlayer(Entity entity, List<Player> playerList){
		float closestDistance = Float.MAX_VALUE;
		Player player = null;
		for(Player selectedPlayer : playerList){
			if(selectedPlayer.getLocation().distance(entity.getLocation()) <= closestDistance){
				player = selectedPlayer;
			}
		}
		return player;
	}
	public static boolean isWithin(Location location, Location point1, Location point2){
		double[] sorted = new double[2];
		
		// gets and sorts X co-ordinate into increasing order (to fit following if statement)
		sorted[0] = point1.getX();
		sorted[1] = point2.getX();
		Arrays.sort(sorted);
		if(location.getX() > sorted[1] || location.getX() < sorted[0])
			return false;

		sorted[0] = point1.getY();
		sorted[1] = point2.getY();
		Arrays.sort(sorted);
		if(location.getY() > sorted[1] || location.getY() < sorted[0])
			return false;
		
		sorted[0] = point1.getZ();
		sorted[1] = point2.getZ();
		Arrays.sort(sorted);
		if(location.getZ() > sorted[1] || location.getZ() < sorted[0])
			return false;
		
		return true;
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
