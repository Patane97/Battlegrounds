package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.entity.Entity;

import com.Patane.Battlegrounds.game.RoundHandler;

public class RoundInstances {
	private static Collection<RoundHandler> RoundInstances = new ArrayList<RoundHandler>();
	
	// adding or removing from the collection of round instances
	public static void roundAdd(RoundHandler round){
		RoundInstances.add(round);
	}
	public static void roundRemove(RoundHandler round){
		RoundInstances.remove(round);
	}
	// checks if all rounds have ended
	public static void checkRoundEndAll(Entity deadMob){
		for(RoundHandler selectedround : RoundInstances){
			selectedround.checkRoundEnd(deadMob);
		}
	}
}
