package com.Patane.Battlegrounds;

import org.bukkit.plugin.java.JavaPlugin;

import com.Patane.Battlegrounds.arena.builder.ArenaYML;
import com.Patane.Battlegrounds.collections.ArenaBuilderInstances;
import com.Patane.Battlegrounds.collections.GameInstances;
import com.Patane.Battlegrounds.commands.CommandHandler;
import com.Patane.Battlegrounds.playerData.PlayerDataYML;

public class Battlegrounds extends JavaPlugin {
	
	public void onEnable() {
		// registering the listeners class
		getServer().getPluginManager().registerEvents(new GlobalListeners(), this);
		// registering the main command(s) "/bg" and "/battlegrounds" or "/battleground"
        CommandHandler commandHandler = new CommandHandler(this);
		this.getCommand("bg").setExecutor(commandHandler);
//		this.getCommand("battlegrounds").setExecutor(commandHandler);
//		this.getCommand("battleground").setExecutor(commandHandler);

		loadFiles();
		// plugin enable info message with version
		Messenger.info("Version " + this.getDescription().getVersion() + " Loaded Successfully!");
	}
	public void onDisable() {
		ArenaBuilderInstances.saveAll();
		GameInstances.endGameAll();
	}
	public void loadFiles(){
		ArenaYML.load(this);
		PlayerDataYML.load(this);
	}
}