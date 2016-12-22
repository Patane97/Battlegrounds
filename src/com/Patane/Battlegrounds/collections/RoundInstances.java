package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.entity.Creature;

import com.Patane.Battlegrounds.game.RoundHandler;

public class RoundInstances {
	private static Collection<RoundHandler> RoundInstances = new ArrayList<RoundHandler>();
	
	// adding or removing from the collection of round instances
	public static void add(RoundHandler round){
		RoundInstances.add(round);
	}
	public static void remove(RoundHandler round){
		RoundInstances.remove(round);
	}
	// OLD
	// used to bridge across a mob dying EventHandler to all rounds. Essentially checks if a mob dying causes any rounds in any games to end
	public static void checkRoundEndAll(Creature deadMob){
		for(RoundHandler selectedround : RoundInstances){
			selectedround.creatureKilled(deadMob);
		}
	}
	public static Collection<RoundHandler> get(){
		return RoundInstances;
	}
}
