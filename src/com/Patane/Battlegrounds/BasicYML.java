package com.Patane.Battlegrounds;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.util.Config;
import com.Patane.Battlegrounds.util.util;

public class BasicYML {
	protected static Plugin plugin;

	protected static Config config;
	protected static String root;
	protected static ConfigurationSection header;
	
	public static void loadYML(Plugin plugin, String config, String root){
		BasicYML.plugin = plugin;
		BasicYML.config = new Config(plugin, config);
		BasicYML.root = root;
		if(!isRootSection())
			createRootSection();
		BasicYML.header = getRootSection();
	}
	
	// DEFINE EACH OF THESE. You know what it does but will others? :)
	
	protected static ConfigurationSection createRootSection() {
		return config.createSection(root);
	}
	protected static ConfigurationSection createSection(String...strings) {
		String path = (strings.length > 1 ? util.stringJoiner(strings, ".") : strings[0]);
		if(isSection(path))
			return getSection(path);
		return config.createSection(root + "." + path);
	}
	protected static ConfigurationSection clearCreateSection(String...strings) {
		String path = (strings.length > 1 ? util.stringJoiner(strings, ".") : strings[0]);
		clearSection(path);
		return config.createSection(root + "." + path);
	}
	protected static void setHeader(String...strings) {
		header = createSection(strings);
	}
	protected static void setHeader(ConfigurationSection section) {
		header = section;
	}
	public static boolean isRootSection() {
		return config.isConfigurationSection(root);
	}
	public static boolean isSection(String...strings){
		String path = (strings.length > 1 ? util.stringJoiner(strings, ".") : strings[0]);
		return config.isConfigurationSection(root + "." + path);
	}
	public static ConfigurationSection getRootSection() {
		return config.getConfigurationSection(root);
	}
	public static ConfigurationSection getSection(String...strings) {
		String path = (strings.length > 1 ? util.stringJoiner(strings, ".") : strings[0]);
		return config.getConfigurationSection(root + "." + path);
	}
	public static boolean isEmpty(String...strings) {
		String path = (strings.length > 1 ? util.stringJoiner(strings, ".") : strings[0]);
		if(isSection(path))
			return getSection(path).getKeys(false).isEmpty();
		return true;
	}
	public static void clearSection(String...strings) {
		String path = (strings.length > 1 ? util.stringJoiner(strings, ".") : strings[0]);
		getRootSection().set(path, null);
		config.save();
	}
	public static void checkEmptyClear(String...strings) {
		if(isEmpty(strings))
			clearSection(strings);
	}
}
