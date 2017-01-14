package com.Patane.Battlegrounds.arena.classes;

import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.util.Config;

public class BGClassYML {
	static Config classConfig;
	static Plugin plugin;
	
	public static void load(Plugin battlegrounds) {
		plugin			= battlegrounds;
		classConfig 	= new Config(plugin, "classes.yml");
	}
}
