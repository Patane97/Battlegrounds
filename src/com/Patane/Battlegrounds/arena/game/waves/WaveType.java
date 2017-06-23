package com.Patane.Battlegrounds.arena.game.waves;

public enum WaveType {
	SINGLE(),
	RECURRING(),
	BOSS();
	
	public static WaveType getFromName(String waveTypeName){
		for(WaveType waveType : WaveType.values()){
			if(waveType.name().equals(waveTypeName))
				return waveType;
		}
		return null;
	}
}
