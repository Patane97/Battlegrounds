package com.Patane.Battlegrounds;

import org.bukkit.plugin.java.JavaPlugin;

import com.Patane.Battlegrounds.collections.GameInstances;
import com.Patane.Battlegrounds.commands.CommandHandler;

public class Battlegrounds extends JavaPlugin {
	public void onEnable() {
		// registering the listeners class
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		// registering the main command(s) "/bg" and "/battlegrounds" or "/battleground"
        CommandHandler commandHandler = new CommandHandler();
		this.getCommand("bg").setExecutor(commandHandler);
//		this.getCommand("battlegrounds").setExecutor(commandHandler);
//		this.getCommand("battleground").setExecutor(commandHandler);
		// plugin enable info message with version
		Messenger.info("Version " + this.getDescription().getVersion() + " Loaded Successfully!");
	}
	public void onDisable() {
		GameInstances.endGameAll();
	}
}
