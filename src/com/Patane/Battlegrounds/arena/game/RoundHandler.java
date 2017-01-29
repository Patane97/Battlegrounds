package com.Patane.Battlegrounds.arena.game;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.util.Spawner;

public class RoundHandler {
	int roundNo;
	int totalMobs;
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
		this.defaultDelay	= 3;
		this.firstWaveDelay = 5;
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
				activeCreatures.addAll(Spawner.roundSpawn(roundNo, game, creatureSpawns));
				// change above to try/catch and print stacktrace
				Messenger.arenaCast(game.getArena(), "&2Round &a" + roundNo + "&2!");
				checkRoundEnd();
				totalMobs = activeCreatures.size();
				game.updateExp();
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
	public int getRoundNo(){
		return roundNo;
	}
	public int getTotalMobs(){
		return totalMobs;
	}
	public int getAmountMobs(){
		return activeCreatures.size();
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
