package com.Patane.Battlegrounds.arena.modes.game;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.util.Spawner;

public class RoundHandler {
	int roundNo;
	// delay of creatures spawning in seconds
	long spawnDelay;
	long firstWaveDelay;
	long defaultDelay;
	
	Plugin plugin;
	Game game;
	ArrayList<Location> creatureSpawns;
	ArrayList<Creature> activeCreatures = new ArrayList<Creature>();
	int spawnTaskID;
	
	RoundHandler(Plugin plugin, Game game){
		this.game 	= game;
		this.roundNo 		= 1;
		this.defaultDelay	= 5;
		this.firstWaveDelay = 10;
		this.creatureSpawns	= game.getArena().getCreatureSpawns();
		this.plugin			= plugin;
	}
	public void startRound(){
		if(roundNo == 1)
			spawnDelay = firstWaveDelay;
		else
			spawnDelay = defaultDelay;
		// spawns creatures after delay
		spawnTaskID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if(!activeCreatures.addAll(Spawner.roundSpawn(roundNo, game, creatureSpawns)))
					Messenger.warning("A round started with 0 mobs spawned. This is a problem :(");
				// change above to try/catch and print stacktrace
				Messenger.arenaCast(game.getArena(), "&aRound " + roundNo + "!");
			}
		}, spawnDelay*20); // seconds * 20 ticks
		
	}
	// removes a mob when they have been killed then checks if the round has ended from it
	public boolean creatureKilled(Creature creature){
		if(activeCreatures.remove(creature)){
			//Messenger.gameCast(gameInstance, "Mob killed!");
			return true;
		}
		return false;
	}
	public void checkRoundEnd(){
		if(activeCreatures.isEmpty()){
			roundNo++;
			startRound();
		}
	}
	public int getSpawnTaskID(){
		return spawnTaskID;
	}
	public ArrayList<Creature> getActiveCreatures(){
		return activeCreatures;
	}
	public void clearMobs() {
		for(Creature mob : activeCreatures){
			mob.remove();
		}
	}
	public boolean hasCreature(Creature creature) {
		for(Creature selectedCreature : activeCreatures){
			if(creature.equals(selectedCreature))
				return true;
		}
		return false;
	}
}
