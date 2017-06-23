package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.HashMap;

import com.Patane.Battlegrounds.arena.game.waves.Wave;

public class Waves {
	private static HashMap<String, Wave> waves = new HashMap<String, Wave>();

	public static Wave add(Wave wave){
		return waves.put(wave.getName(), wave);
	}
	public static boolean remove(Wave wave){
		if(waves.remove(wave.getName()) != null){
			Wave.YML().clearSection(wave.getName());
			return true;
		}
		return false;
	}
	public static ArrayList<String> getNames(){
		return new ArrayList<String>(waves.keySet());
	}
	public static ArrayList<Wave> get(){
		return new ArrayList<Wave>(waves.values());
	}
}
