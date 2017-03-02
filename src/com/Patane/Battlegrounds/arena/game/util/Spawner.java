package com.Patane.Battlegrounds.arena.game.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Creature;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.game.Game;
import com.Patane.Battlegrounds.arena.game.RoundHandler;
import com.Patane.Battlegrounds.custom.BGCreatureInfo;
import com.Patane.Battlegrounds.custom.BGEntityType;
import com.Patane.Battlegrounds.custom.Spawning;
import com.Patane.Battlegrounds.util.Locating;


public class Spawner implements Runnable{
	RoundHandler roundHandler;
	public Spawner(RoundHandler roundHandler){
		this.roundHandler = roundHandler;
	}
	/**
	 * Generates a random weighted list of creatures that will be spawned in the roundHandler's current round
	 * @return
	 */
	public List<BGEntityType> genEntityList(){
		// adjusting this changes the growth of mobs per round (lower=slower, higher=faster)
		int maxWeight = roundHandler.getRoundNo()*5;
		List<BGEntityType> creatures = new ArrayList<BGEntityType>();
//		CreatureWeights mobWeights = new CreatureWeights();
		List<BGCreatureInfo> creatureDraft = BGCreatureInfo.genCreatureDraft(maxWeight);
		if (creatureDraft.size() <= 0) return null;
//		Messenger.arenaCast(roundHandler.getGame().getArena(), "&6Draft Size: &7" + creatureDraft.size());
		ListIterator<BGCreatureInfo> draftIterator = creatureDraft.listIterator();
		while(maxWeight > 0){
			if(!draftIterator.hasNext()){
				while (draftIterator.hasPrevious())
					draftIterator.previous();
			}
			BGCreatureInfo creature = draftIterator.next();
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
//			// EFFECT //
			spawnLocation.getWorld().spawnParticle(Particle.CLOUD, spawnLocation, 50, 0.5, 1, 0.5, 0.02);
			newCreature.setCustomNameVisible(true);
			newCreature.setTarget(Locating.findClosestPlayer(newCreature, game.getArena().getPlayers()));
		}
		roundHandler.checkRoundEnd();
		roundHandler.updateTotalMobs();
		game.updateExp();
	}
}
