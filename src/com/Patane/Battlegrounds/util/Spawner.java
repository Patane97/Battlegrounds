package com.Patane.Battlegrounds.util;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

import com.Patane.Battlegrounds.game.GameHandler;

public class Spawner {
	/**
	 * 
	 * Runs all calculations to spawn mobs for the round
	 * 
	 * @param maxWeight
	 * @param world
	 * @param location
	 * @return an ArrayList of Creature's that were spawned
	 */
	public static ArrayList<Creature> roundSpawn(int roundNo, GameHandler game, Location location){
		// adjusting this changes the growth of mobs per round (lower=slower, higher=faster)
		int maxWeight = roundNo*5;
		// creating ArrayList of spawned mobs to eventually send back to RoundHandler
		ArrayList<Creature> spawnedMobs = new ArrayList<Creature>();
		// max radius for mobs to randomly spawn
		int spawnRadius = 3;
		
		CreatureWeights mobWeights = new CreatureWeights();
		ArrayList<EntityType> allowedTypes = mobWeights.getAllowedTypes(maxWeight);
		
		// keeps looping/spawning new mobs from "allowedTypes" and subtracting their weight to maxWeight until maxWeight is < 0
		while(maxWeight > 0){
			// random locations set
			location.setX(location.getX() + Randoms.integer(spawnRadius, -(spawnRadius)));
			location.setZ(location.getZ() + Randoms.integer(spawnRadius, -(spawnRadius)));
			EntityType entityType = Randoms.entityType(allowedTypes);
			if(entityType == null) return null;
			// spawns creature
			Creature newCreature = (Creature) game.getWorld().spawnEntity(location, entityType);
			newCreature.setCustomNameVisible(true);
			newCreature.setTarget(Locations.findClosestPlayer(newCreature, game.getPlayers()));
			
			maxWeight -= mobWeights.getWeight(entityType);
			spawnedMobs.add(newCreature);
		}
		return spawnedMobs;
	}
}
