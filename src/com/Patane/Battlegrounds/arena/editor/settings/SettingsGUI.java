package com.Patane.Battlegrounds.arena.editor.settings;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.GUI.GUI;
import com.Patane.Battlegrounds.GUI.settings.SettingsMainPage;
import com.Patane.Battlegrounds.arena.Arena;

public class SettingsGUI extends GUI{
	Arena arena;
	
	SettingsEditor settingsEditor;
	
	public SettingsGUI(Plugin plugin, Arena arena, String name, Player player, SettingsEditor settingsEditor) {
		super(plugin, name, player);
		setMainPage(new SettingsMainPage(this, name, 27));
		this.arena = arena;
		this.settingsEditor = settingsEditor;
		// display settings
	}
	public Arena getArena(){
		return arena;
	}
	@Override
	public void exit(){
		super.exit();
		// save settings
		arena.getMode().sessionOver();
	}

}
