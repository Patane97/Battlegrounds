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
	 * @param creatureSpawns
	 * @return an ArrayList of Creature's that were spawned
	 */
	public synchronized static ArrayList<Creature> roundSpawn(int roundNo, GameHandler game, ArrayList<Location> creatureSpawns){
		// adjusting this changes the growth of mobs per round (lower=slower, higher=faster)
		int maxWeight = roundNo*5;
		// creating ArrayList of spawned mobs to eventually send back to RoundHandler
		ArrayList<Creature> spawnedMobs = new ArrayList<Creature>();
		
		CreatureWeights mobWeights = new CreatureWeights();
		ArrayList<EntityType> allowedTypes = mobWeights.getAllowedTypes(maxWeight);
		
		// keeps looping/spawning new mobs from "allowedTypes" and subtracting their weight to maxWeight until maxWeight is < 0
		while(maxWeight > 0){
			// random locations set
			int spawnLocNumber = Randoms.integer(0, creatureSpawns.size()-1);
			Location randomSpawn = creatureSpawns.get(spawnLocNumber);
			EntityType entityType = Randoms.entityType(allowedTypes);
			if(entityType == null) return null;
			// spawns creature
			Creature newCreature = (Creature) game.getWorld().spawnEntity(randomSpawn, entityType);
			newCreature.setCustomNameVisible(true);
			newCreature.setTarget(Locating.findClosestPlayer(newCreature, game.getPlayers()));
			
			maxWeight -= mobWeights.getWeight(entityType);
			spawnedMobs.add(newCreature);
		}
		return spawnedMobs;
	}
}
