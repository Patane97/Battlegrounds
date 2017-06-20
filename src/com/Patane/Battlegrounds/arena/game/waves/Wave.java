package com.Patane.Battlegrounds.arena.game.waves;

import java.util.List;

public class Wave {
	
	WaveType type;
	int increment;
	int priority;
	List<String> creatures;
	
	public Wave(WaveType type, int increment, int priority, List<String> creatures){
		this.type 		= type;
		this.increment 	= increment;
		this.priority 	= priority;
		this.creatures 	= creatures;
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
	public boolean addCreature(String creatureName){
		return creatures.add(creatureName);
	}
	public boolean removeCreature(String creatureName){
		return creatures.remove(creatureName);
	}
}
