package com.Patane.Battlegrounds.arena.game.waves;

import java.util.HashMap;

import com.Patane.Battlegrounds.collections.Waves;

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
	WaveType type;
	int increment;
	int priority;
	HashMap<Integer, String> creatures;
	
	public Wave(String name, WaveType type, int increment, int priority, HashMap<Integer, String> creatures){
		this.name		= name;
		this.type 		= type;
		this.increment 	= increment;
		this.priority 	= priority;
		this.creatures 	= creatures;
		Waves.add(this);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public HashMap<Integer, String> getCreatures(){
		return creatures;
	}
	public String putCreature(int probability, String creatureName){
		return creatures.put(probability, creatureName);
	}
	public boolean removeCreature(String creatureName){
		for(int probability : creatures.keySet()){
			if(creatures.get(probability).equals(creatureName)){
				creatures.remove(probability);
				return true;
			}
		}
		return false;
	}
}
