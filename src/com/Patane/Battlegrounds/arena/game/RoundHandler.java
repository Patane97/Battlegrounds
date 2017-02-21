package com.Patane.Battlegrounds.arena.game;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.arena.game.util.Spawner;

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
	private int spawnTaskID;

	int count = 0;
	
	RoundHandler(Plugin plugin, Game game){
		this.game 	= game;
		this.roundNo 		= 1;
		this.defaultDelay	= 3;
		this.firstWaveDelay = 5;
		this.creatureSpawns	= game.getArena().getCreatureSpawns();
		this.plugin			= plugin;
	}
	public void startRound(){
		spawnDelay = (roundNo == 1 ? firstWaveDelay : defaultDelay);
		
		// re-create this:
		/*	a method that runs whats in spawner and returns a hashmap of <creature(type of mob), Integer (int for location in creatureSpawns)>
		 *  a repeated task taht starts at (spawndelay*20)-(20/4) and runs 3 times (shown below) which spawns particles at each location to show where mobs are spawning
		 *  a delayed task at spawnDelay*20 which runs through the previous hashmap and spawns respective mob at respective int-location in creatureSpawns (similar to below)  
		 * 
		 */
		spawnTaskID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Spawner(this), spawnDelay*20); // seconds * 20 ticks
		
	}
	// removes a mob when they have been killed then checks if the round has ended from it
	public boolean creatureKilled(Creature creature){
		if(activeCreatures.remove(creature)){
//			Messenger.arenaCast(game.getArena(), "&6Creature &7" + creature.getName() + "&6 killed.");
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
			nextRound();
		}
	}
	public void nextRound(){
		roundNo++;
		startRound();
	}
	public void stopAllTasks(){
		Bukkit.getServer().getScheduler().cancelTask(spawnTaskID);
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
	public Game getGame() {
		return game;
	}
	public ArrayList<Location> getCreatureSpawns() {
		return creatureSpawns;
	}
	public void updateTotalMobs() {
		totalMobs = activeCreatures.size();
	}
}
