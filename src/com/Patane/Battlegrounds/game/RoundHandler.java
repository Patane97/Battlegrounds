package com.Patane.Battlegrounds.game;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.collections.*;
import com.Patane.Battlegrounds.util.Spawner;

public class RoundHandler {
	int roundNo;
	// delay of creatures spawning in seconds
	long spawnDelay;
	
	Plugin plugin;
	GameHandler gameInstance;
	ArrayList<Location> creatureSpawns;
	ArrayList<Creature> activeCreatures = new ArrayList<Creature>();
	int spawnTaskID;
	
	RoundHandler(Plugin plugin, GameHandler game){
		this.gameInstance 	= game;
		this.roundNo 		= 1;
		this.spawnDelay		= 5;
		this.creatureSpawns	= game.getArena().getCreatureSpawns();
		this.plugin			= plugin;
		RoundInstances.add(this);
	}
	public void startRound(){
		// spawns creatures after delay
		spawnTaskID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if(!activeCreatures.addAll(Spawner.roundSpawn(roundNo, gameInstance, creatureSpawns)))
					Messenger.severe("A round started with 0 mobs spawned. This is a problem :(");
				Messenger.gameCast(gameInstance, "&aRound " + roundNo + "!");
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
	public int getRoundNo(){
		return roundNo;
	}
	public GameHandler getGame(){
		return gameInstance;
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
