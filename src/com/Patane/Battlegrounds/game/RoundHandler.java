package com.Patane.Battlegrounds.game;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Creature;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.collections.*;
import com.Patane.Battlegrounds.util.Spawner;

public class RoundHandler {
	int roundNo;
	GameHandler gameInstance;
	Location creatureSpawn;
	ArrayList<Creature> activeCreatures = new ArrayList<Creature>();
	
	RoundHandler(GameHandler game){
		this.gameInstance 	= game;
		this.roundNo 		= 1;
		this.creatureSpawn	= game.getArena().getCreatureSpawn();
		RoundInstances.add(this);
	}
	public void startRound(){
		Messenger.gameCast(gameInstance, "Round " + roundNo);
		// runs spawning calculations and adds all spawned mobs to activeMobs ArrayList
		if(!activeCreatures.addAll(Spawner.roundSpawn(roundNo, gameInstance, creatureSpawn))){
			Messenger.severe("A round started with 0 mobs spawned. This is a problem :(");
		}
	}
	// removes a mob when they have been killed then checks if the round has ended from it
	public boolean creatureKilled(Creature creature){
		if(activeCreatures.remove(creature)){
			Messenger.gameCast(gameInstance, "Mob killed!");
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
