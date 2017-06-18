package com.Patane.Battlegrounds.arena.game;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.game.util.Spawner;

public class RoundHandler {
	int roundNo;
	int totalMobs;
	// delay of creatures spawning in seconds
	long spawnDelay;
	float firstDelay;
	float waveDelay;
	
	Plugin plugin;
	Game game;
	ArrayList<Location> creatureSpawns;
	ArrayList<Creature> activeCreatures = new ArrayList<Creature>();
	private int spawnTaskID;
	
	RoundHandler(Plugin plugin, Game game){
		this.game 			= game;
		this.roundNo 		= 1;
		this.waveDelay		= game.getArena().getSettings().WAVE_DELAY;
		this.firstDelay 	= game.getArena().getSettings().FIRST_DELAY;
		this.creatureSpawns	= game.getArena().getCreatureSpawns();
		this.plugin			= plugin;
	}
	public void startRound(){
		spawnDelay = (long) (roundNo == 1 ? firstDelay : waveDelay);
		
		// re-create this:
		/*	a method that runs whats in spawner and returns a hashmap of <creature(type of mob), Integer (int for location in creatureSpawns)>
		 *  a repeated task that starts at (spawndelay*20)-(20/4) and runs 3 times (shown below) which spawns particles at each location to show where mobs are spawning
		 *  a delayed task at spawnDelay*20 which runs through the previous hashmap and spawns respective mob at respective int-location in creatureSpawns (similar to below)  
		 * 
		 */
		spawnTaskID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Spawner(this), spawnDelay*20); // seconds * 20 ticks
		
	}
	// removes a mob when they have been killed
	public boolean creatureKilled(Creature creature){
		if(activeCreatures.remove(creature)){
			Messenger.debug(game.getArena(), "Creature " + creature.getName() + " killed.");
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
		Messenger.debug(game.getArena(), "Next Round");
		int finalWave = game.getArena().getSettings().FINAL_WAVE;
		if(finalWave != 0 && roundNo >= finalWave){
			game.finalRoundEnd();
			Messenger.arenaCast(game.getArena(), "&2Congradulations! You passed the final round.");
		}
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
