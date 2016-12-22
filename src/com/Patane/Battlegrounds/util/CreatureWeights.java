package com.Patane.Battlegrounds.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.EntityType;

public class CreatureWeights {
	private static HashMap<EntityType, Integer> creatureWeights = new HashMap<EntityType, Integer>();
	
	public CreatureWeights(){
		creatureWeights.put(EntityType.ZOMBIE, 5);
		creatureWeights.put(EntityType.SKELETON, 7);
		creatureWeights.put(EntityType.SPIDER, 15);
		creatureWeights.put(EntityType.CREEPER, 30);
	}
	public int getWeight(EntityType entity){
		return creatureWeights.get(entity);
	}
	/**
	 * Finds which EntityTypes will fit inside the weight given
	 * 
	 * @param weight
	 * @return an ArrayList of creatures that can fit inside the given weight
	 */
	public ArrayList<EntityType> getAllowedTypes(int weight){
		ArrayList<EntityType> entityTypes = new ArrayList<EntityType>();
		for(EntityType selectedCreature : creatureWeights.keySet()){
			if(creatureWeights.get(selectedCreature)*2 <= weight){
				entityTypes.add(selectedCreature);
			}
		}
		// This is here so if for any reason there is a round that has 0 allowed entities, one Zombie will always spawn
		if(entityTypes.isEmpty()) entityTypes.add(EntityType.ZOMBIE);
		return entityTypes;
	}
	
}
