package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;

import org.bukkit.entity.Creature;

import com.Patane.Battlegrounds.game.GameHandler;
import com.Patane.Battlegrounds.game.RoundHandler;

public class ActiveCreatures {

	// collects and returns all active mobs in all active games
	public static ArrayList<Creature> getActiveCreaturesAll(){
		ArrayList<Creature> mobs = new ArrayList<Creature>();
		for(RoundHandler selectedround : RoundInstances.get()){
			mobs.addAll(selectedround.getActiveCreatures());
		}
		return mobs;
	}
	public static GameHandler getGame(Creature creature){
		for(RoundHandler selectedround : RoundInstances.get()){
			if(selectedround.hasCreature(creature))
				return selectedround.getGame();
		}
		return null;
	}
}
