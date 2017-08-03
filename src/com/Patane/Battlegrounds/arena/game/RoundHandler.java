package com.Patane.Battlegrounds.arena.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Creature;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.game.waves.Wave;
import com.Patane.Battlegrounds.arena.game.waves.WaveType;
import com.Patane.Battlegrounds.custom.BGCreature;
import com.Patane.Battlegrounds.custom.BGEntityType;
import com.Patane.Battlegrounds.custom.Spawning;
import com.Patane.Battlegrounds.util.Locating;

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
	
	public class Spawner implements Runnable{
		RoundHandler roundHandler;
		public Spawner(RoundHandler roundHandler){
			this.roundHandler = roundHandler;
		}
		private Wave getWave(int roundNo){
			Arena arena = roundHandler.getGame().getArena();
			List<Wave> waves = new ArrayList<Wave>();
			int currentPriority = 0;
			for(Wave currentWave : arena.getWaves()){
				// See if you can have all this logic within WaveType (as seperate methods for each waveType)
				if((currentWave.getType() != WaveType.RECURRING && currentWave.getIncrement() == roundNo)
				|| (currentWave.getType() == WaveType.RECURRING && currentWave.getIncrement()%roundNo == 0)){
					if(currentWave.getPriority() > currentPriority){
						waves.clear();
						waves.add(currentWave);
						currentPriority = currentWave.getPriority();
					} else if(currentWave.getPriority() == currentPriority)
						waves.add(currentWave);
				}
			}
			if(waves.size() > 1)
				Collections.shuffle(waves);
			return waves.get(0);
		}
		/**
		 * Generates a random weighted list of creatures that will be spawned in the roundHandler's current round
		 * @return
		 */
		public List<BGEntityType> genEntityList(){
			// adjusting this changes the growth of mobs per round (lower=slower, higher=faster)
			int maxWeight = roundHandler.getRoundNo()*5;
			List<BGEntityType> creatures = new ArrayList<BGEntityType>();
//			CreatureWeights mobWeights = new CreatureWeights();
			List<BGCreature> creatureDraft = BGCreature.genCreatureDraft(maxWeight);
			if (creatureDraft.size() <= 0) return null;
//			Messenger.arenaCast(roundHandler.getGame().getArena(), "&6Draft Size: &7" + creatureDraft.size());
			ListIterator<BGCreature> draftIterator = creatureDraft.listIterator();
			while(maxWeight > 0){
				if(!draftIterator.hasNext()){
					while (draftIterator.hasPrevious())
						draftIterator.previous();
				}
				BGCreature creature = draftIterator.next();
				maxWeight = maxWeight - creature.getWeight();
				creatures.add(creature.getEntityType());
			}
			return creatures;
		}
		@Override
		public void run() {
			int roundNo = roundHandler.getRoundNo();
			Game game = roundHandler.getGame();
			List<BGEntityType> creatures = genEntityList();
			if(creatures == null){
				roundHandler.startRound();
				return;
			}
			roundHandler.getActiveCreatures().clear();
			Messenger.arenaCast(game.getArena(), "&2Round &a" + roundNo + "&2!");
			List<Integer> spawnNoList = new ArrayList<Integer>();
			int spawnLimit = roundHandler.getCreatureSpawns().size();
			for(int i = 0; i < spawnLimit; i++)
				spawnNoList.add(i);
			Collections.shuffle(spawnNoList);
			int spawnIteration = 0;
			for(BGEntityType entityType : creatures){
				if(spawnIteration >= spawnLimit){
					Collections.shuffle(spawnNoList);
					spawnIteration = 0;
				}
				int spawnNo = spawnNoList.get(spawnIteration);
				spawnIteration ++;
				Location spawnLocation = roundHandler.getCreatureSpawns().get(spawnNo);
				spawnLocation = new Location(spawnLocation.getWorld(), spawnLocation.getX()+0.5, spawnLocation.getY(), spawnLocation.getZ()+0.5, spawnLocation.getYaw(), spawnLocation.getPitch());
				Creature newCreature = (Creature) Spawning.spawnEntity(roundHandler.getGame().getArena(), entityType, spawnLocation);
//				// EFFECT //
				spawnLocation.getWorld().spawnParticle(Particle.CLOUD, spawnLocation, 50, 0.5, 1, 0.5, 0.02);
				newCreature.setCustomNameVisible(true);
				newCreature.setTarget(Locating.findClosestPlayer(newCreature, game.getArena().getPlayers()));
			}
			roundHandler.checkRoundEnd();
			roundHandler.updateTotalMobs();
			game.updateExp();
		}
	}
}
