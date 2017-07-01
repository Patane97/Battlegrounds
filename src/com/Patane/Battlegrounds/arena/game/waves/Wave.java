package com.Patane.Battlegrounds.arena.game.waves;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.collections.Waves;
import com.Patane.Battlegrounds.custom.BGCreature;
import com.Patane.Battlegrounds.util.util;

public class Wave {
	/**
	 * ******************* STATIC YML SECTION *******************
	 */
	private static WavesYML yml;

	public static void setYML(WavesYML yml){
		Wave.yml = yml;
	}
	public static WavesYML YML(){
		return Wave.yml;
	}
	/**
	 * **********************************************************
	 */
	String name;
	ItemStack icon;
	WaveType type;
	int increment;
	int priority;
	HashMap<BGCreature, Integer> creatures;
	
	public Wave(String name, WaveType type, int increment, int priority, HashMap<BGCreature, Integer> creatures){
		this.name		= name;
		this.type 		= type;
		this.increment 	= increment;
		this.priority 	= priority;
		this.creatures 	= creatures;
		this.icon		= util.setItemNameLore(type.getIcon(), "&6" + name, type.getDesc(increment));
		
		Waves.add(this);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ItemStack getIcon() {
		return icon;
	}
	public WaveType getType() {
		return type;
	}
	public void setType(WaveType type) {
		this.type = type;
	}
	public int getIncrement() {
		return this.increment;
	}
	public void setIncrement(int increment) {
		this.increment = increment;
	}
	public int getPriority() {
		return this.priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public HashMap<BGCreature, Integer> getCreatures(){
		return creatures;
	}
	public int putCreature(int probability, String creatureName){
		return putCreature(probability, BGCreature.getFromName(creatureName));
	}
	public int putCreature(int probability, BGCreature creatureName){
		return creatures.put(creatureName, probability);
	}
	public int removeCreature(String creatureName){
		return creatures.remove(creatureName);
	}
}
