package com.Patane.Battlegrounds.arena.game.waves;

import java.util.HashMap;

import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.BasicYML;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.collections.Waves;
import com.Patane.Battlegrounds.util.util;

public class WavesYML extends BasicYML{

	public WavesYML(Plugin plugin) {
		super(plugin, "waves.yml", "waves");
	}

	@Override
	public void save(){
		for(Wave selectedWave : Waves.get())
			save(selectedWave);
		Messenger.info("Successfully saved Waves: " + util.stringJoiner(Waves.getNames(), ", "));
	}

	@Override
	public void load(){
		HashMap<Integer, String> creature = new HashMap<Integer, String>();
		creature.put(10, "Zombie");
		new Wave("Default", WaveType.SINGLE, 1, 0, creature);
		for(String waveName : header.getKeys(false))
			load(waveName);
		Messenger.info("Successfully loaded Waves: " + util.stringJoiner(Waves.getNames(), ", "));
	}
	
	public void save(Wave wave){
		String waveName = wave.getName();
		try{
			setHeader(clearCreateSection(waveName));
			header.set("Type", wave.getType().name());
			header.set("Increment", wave.getIncrement());
			header.set("Priority", wave.getPriority());
			setHeader(waveName + ".Creatures");
			HashMap<Integer, String> creatures = wave.getCreatures();
			for(int probability : creatures.keySet()){
				header.set(Integer.toString(probability), creatures.get(probability));
			}
			config.save();
		} catch (Exception e){
			Messenger.severe("Error whilst saving Wave: " + waveName + ".");
			e.printStackTrace();
		}
	}
	public Wave load(String waveName){
		try{
			setHeader(waveName);
			WaveType type = WaveType.getFromName(header.getString("Type"));
			int increment = header.getInt("Increment");
			int priority = header.getInt("Priority");
			HashMap<Integer, String> creatures = new HashMap<Integer, String>();
			for(int probability : header.getIntegerList("Creatures")){
				creatures.put(probability, header.getString(Integer.toString(probability)));
			}
			Wave loadedWave = new Wave(waveName, type, increment, priority, creatures);
			return loadedWave;
		} catch (Exception e){
			Messenger.warning("Error loading Wave: " + waveName + ".");
			e.printStackTrace();
			return null;
		}
	}
}
