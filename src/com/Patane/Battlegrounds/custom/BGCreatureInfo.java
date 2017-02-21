package com.Patane.Battlegrounds.custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum BGCreatureInfo {
	ZOMBIE(BGEntityType.ZOMBIE, 5, 8),
	SKELETON(BGEntityType.SKELETON, 7, 4),
	SPIDER(BGEntityType.SPIDER, 6, 3),
	CREEPER(BGEntityType.CREEPER, 6, 2),
	CHASER_SKELETON(BGEntityType.CHASER_SKELETON, 6, 5),
	ZOMBIE_KNIGHT(BGEntityType.ZOMBIE_KNIGHT, 5, 2);
	
	String name;
	BGEntityType entityType;
	int weight;
	int frequency;
	
	private BGCreatureInfo(BGEntityType entityType, int weight, int frequency){
		this.name 		= entityType.getName();
		this.entityType = entityType;
		this.weight 	= weight;
		this.frequency	= frequency;
	}
	public String getName(){
		return name;
	}
	public BGEntityType getEntityType(){
		return entityType;
	}
	public int getWeight(){
		return weight;
	}
	public int getFrequency(){
		return frequency;
	}
	
	public static List<BGCreatureInfo> genCreatureDraft(int weight){
		List<BGCreatureInfo> creatureDraft = new ArrayList<BGCreatureInfo>();
		for(BGCreatureInfo creatureInfo : BGCreatureInfo.values()){
			if(creatureInfo.getWeight() <= weight){
				for(int i = 1; i <= creatureInfo.getFrequency(); i++)
					creatureDraft.add(creatureInfo);
			}
		}
		Collections.shuffle(creatureDraft);
		return creatureDraft;
	}
}
