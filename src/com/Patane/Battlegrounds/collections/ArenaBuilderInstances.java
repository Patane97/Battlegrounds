package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.arena.builder.ArenaBuilder;

public class ArenaBuilderInstances {
	private static Collection<ArenaBuilder> ArenaBuilderInstances = new ArrayList<ArenaBuilder>();
	public static void add(ArenaBuilder builder){
		ArenaBuilderInstances.add(builder);
	}
	public static void remove(ArenaBuilder builder){
		ArenaBuilderInstances.remove(builder);
	}
	public static Collection<ArenaBuilder> get(){
		return ArenaBuilderInstances;
	}
	public static ArenaBuilder getBuilder(Player player){
		for(ArenaBuilder selectedBuilder : ArenaBuilderInstances){
			if(selectedBuilder.getCreator().equals(player))
				return selectedBuilder;
		}
		return null;
	}
	public static void saveAll() {
		// to fix ConcurrentModificationException
		Collection<ArenaBuilder> UnmodifiedBuilderInstances = new ArrayList<ArenaBuilder>();
		UnmodifiedBuilderInstances.addAll(ArenaBuilderInstances);
		for(ArenaBuilder builder : UnmodifiedBuilderInstances){
			builder.save();
		}
		
	}
	public static ArenaBuilder getBuilder(ArenaHandler arena) {
		for(ArenaBuilder selectedBuilder : ArenaBuilderInstances){
			if(selectedBuilder.getArena().equals(arena))
				return selectedBuilder;
		}
		return null;
	}
}
