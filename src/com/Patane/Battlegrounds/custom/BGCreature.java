package com.Patane.Battlegrounds.custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.util.util;

public enum BGCreature {
	ZOMBIE(BGEntityType.ZOMBIE, 5, 8),
	SKELETON(BGEntityType.SKELETON, 7, 4),
	SPIDER(BGEntityType.SPIDER, 6, 3),
	CREEPER(BGEntityType.CREEPER, 6, 2),
	CHASER_SKELETON(BGEntityType.CHASER_SKELETON, 6, 5),
	ZOMBIE_KNIGHT(BGEntityType.ZOMBIE_KNIGHT, 5, 2);
	
	String name;
	BGEntityType entityType;
	int weight;
	// REMOVE FREQUENCY
	int frequency;
	
	private BGCreature(BGEntityType entityType, int weight, int frequency){
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
	
	public static List<BGCreature> genCreatureDraft(int weight){
		List<BGCreature> creatureDraft = new ArrayList<BGCreature>();
		for(BGCreature creatureInfo : BGCreature.values()){
			if(creatureInfo.getWeight() <= weight){
				for(int i = 1; i <= creatureInfo.getFrequency(); i++)
					creatureDraft.add(creatureInfo);
			}
		}
		Collections.shuffle(creatureDraft);
		return creatureDraft;
	}
	public static BGCreature getFromName(String creatureName){
		for(BGCreature creature : BGCreature.values()){
			if(creature.getEntityType().name().equalsIgnoreCase(creatureName))
				return creature;
		}
		return null;
	}
	public ItemStack getSpawnEgg(String...lore){
		ItemStack icon = new ItemStack(Material.MONSTER_EGG, 1, (short) entityType.getID());
		icon = util.setItemNameLore(icon, "&6"+name, lore);
		return icon;
	}
}
