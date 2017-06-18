package com.Patane.Battlegrounds;

import org.bukkit.plugin.java.JavaPlugin;

import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.ArenaYML;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.classes.BGClassYML;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.collections.EditorTypes;
import com.Patane.Battlegrounds.commands.CommandHandler;
import com.Patane.Battlegrounds.listeners.GlobalListeners;
import com.Patane.Battlegrounds.playerData.PlayerData;
import com.Patane.Battlegrounds.playerData.PlayerDataYML;

public class Battlegrounds extends JavaPlugin {
	
	private static boolean debugMode = true;
	
	public void onEnable() {
		runEnableTasks();
		// plugin enable info message with version
		Messenger.info("Version " + this.getDescription().getVersion() + " Loaded Successfully!");
	}
	public void onDisable() {
		Arenas.allSessionsOver();
		Arena.YML().saveAllArenas();
		BGClass.YML().saveAllClasses();
	}
	private void runEnableTasks() {
		// registering the listeners class
		getServer().getPluginManager().registerEvents(new GlobalListeners(), this);
		// registering the main command(s) "/bg" and "/battlegrounds" or "/battleground"
        CommandHandler commandHandler = new CommandHandler(this);
		this.getCommand("bg").setExecutor(commandHandler);
		EditorTypes.registerAll();
		loadFiles();
		cleanArenas();
	}
	private void loadFiles(){
//		BGClass.YML().load(this);
		BGClass.setYML(new BGClassYML());
		BGClass.YML().load(this);
//		PlayerData.YML().load(this);
		PlayerData.setYML(new PlayerDataYML());
		PlayerData.YML().load(this);
//		Arena.YML().load(this);
		Arena.setYML(new ArenaYML());
		Arena.YML().load(this);
	}
	private void cleanArenas(){
		Arenas.cleanAll();
	}
	public static boolean debugMode() {
		return debugMode;
	}
}