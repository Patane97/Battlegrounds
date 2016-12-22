package com.Patane.Battlegrounds.util;

import java.util.List;

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
}
