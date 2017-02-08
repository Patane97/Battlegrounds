package com.Patane.Battlegrounds.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.game.Game;
import com.Patane.Battlegrounds.arena.game.RoundHandler;


public class Spawner implements Runnable{
	RoundHandler roundHandler;
	public Spawner(RoundHandler roundHandler){
		this.roundHandler = roundHandler;
	}
	/**
	 * Runs all calculations to spawn mobs for the round
	 * 
	 * @param maxWeight
	 * @param world
	 * @param creatureSpawns
	 * @return an ArrayList of Creature's that were spawned
	 */
	public static HashMap<EntityType, Integer> h(int roundNo, Game game){
		// adjusting this changes the growth of mobs per round (lower=slower, higher=faster)
		int maxWeight = roundNo*5;
		// creating ArrayList of spawned mobs to eventually send back to RoundHandler
//		ArrayList<Creature> spawnedMobs = new ArrayList<Creature>();
		HashMap<EntityType, Integer> creatures = new HashMap<EntityType, Integer>();
		
		ArrayList<Location> creatureSpawns = game.getArena().getCreatureSpawns();
		CreatureWeights mobWeights = new CreatureWeights();
		ArrayList<EntityType> allowedTypes = mobWeights.getAllowedTypes(maxWeight);
		if (allowedTypes.size() <= 0) return null;
		// keeps looping/spawning new mobs from "allowedTypes" and subtracting their weight to maxWeight until maxWeight is < 0
		while(maxWeight > 0){
			// random locations set
			int spawnNo = Randoms.integer(0, creatureSpawns.size()-1);
//			Location randomSpawn = creatureSpawns.get(spawnLocNumber);
			EntityType entityType = Randoms.entityType(allowedTypes);
//			if(entityType == null) return null;
			// spawns creature
//			game.setSpawning(true);
//			Creature newCreature = (Creature) game.getArena().getWorld().spawnEntity(randomSpawn, entityType);
//			game.setSpawning(false);
//			newCreature.setCustomNameVisible(true);
//			newCreature.setTarget(Locating.findClosestPlayer(newCreature, game.getArena().getPlayers()));
			
			maxWeight -= mobWeights.getWeight(entityType);
			creatures.put(entityType, spawnNo);
		}
		return creatures;
	}
	public ArrayList<EntityType> creatureList(){
		// adjusting this changes the growth of mobs per round (lower=slower, higher=faster)
		int maxWeight = roundHandler.getRoundNo()*5;
		ArrayList<EntityType> creatures = new ArrayList<EntityType>();
		CreatureWeights mobWeights = new CreatureWeights();
		ArrayList<EntityType> allowedTypes = mobWeights.getAllowedTypes(maxWeight);
		if (allowedTypes.size() <= 0) return null;
		while(maxWeight > 0){
			EntityType entityType = Randoms.entityType(allowedTypes);
			maxWeight -= mobWeights.getWeight(entityType);
			creatures.add(entityType);
		}
		return creatures;
	}
	@Override
	public void run() {
		int roundNo = roundHandler.getRoundNo();
		Game game = roundHandler.getGame();
		ArrayList<EntityType> creatures = creatureList();
//		HashMap<EntityType, Integer> creatureMap = Spawner.creaturesHashMap(roundNo, game);
		if(creatures == null){
			roundHandler.startRound();
			return;
		}
		roundHandler.getActiveCreatures().clear();
		Messenger.arenaCast(game.getArena(), "&2Round &a" + roundNo + "&2!");
		for(EntityType entityType : creatures){
			// WHAT TO DO WITH SPAWN LOCATION:
			/* Before loop, a list is created with a randomly selected order of unique spawnNo's
			 * each loop, 'i' is checked to be larger than the above list.size()
			 * if it is larger then i = i-list.size()
			 * list.get(i) is set as the creatures spawnNo
			 * spawns creature
			 * continue loop.
			 */
			int spawnNo = Randoms.integer(0, roundHandler.getCreatureSpawns().size()-1);
			Messenger.arenaCast(game.getArena(), entityType + " | " + spawnNo);
			Location spawnLocation = roundHandler.getCreatureSpawns().get(spawnNo);
			game.setSpawning(true);
			Creature newCreature = (Creature) game.getArena().getWorld().spawnEntity(spawnLocation, entityType);
			game.setSpawning(false);
			// EFFECT //
			spawnLocation.getWorld().spawnParticle(Particle.CLOUD, spawnLocation, 40, 0, 1, 0, 0.02);
			newCreature.setCustomNameVisible(true);
			newCreature.setTarget(Locating.findClosestPlayer(newCreature, game.getArena().getPlayers()));
			roundHandler.getActiveCreatures().add(newCreature);
		}
		roundHandler.checkRoundEnd();
		roundHandler.updateTotalMobs();
		game.updateExp();
	}
}
