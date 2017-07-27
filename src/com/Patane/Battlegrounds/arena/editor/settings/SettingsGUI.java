package com.Patane.Battlegrounds.arena.editor.settings;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.GUI.ChestGUI;
import com.Patane.Battlegrounds.GUI.settings.SettingsMainPage;
import com.Patane.Battlegrounds.arena.Arena;

public class SettingsGUI extends ChestGUI{
	Arena arena;
	
	SettingsEditor settingsEditor;
	
	public SettingsGUI(Plugin plugin, Arena arena, String name, Player player, SettingsEditor settingsEditor) {
		super(plugin, player, name);
		this.arena = arena;
		setMainPage(new SettingsMainPage(this, name, 27));
		this.settingsEditor = settingsEditor;
	}
	public Arena getArena(){
		return arena;
	}
	@Override
	public void exit(){
		super.exit();
		arena.getMode().sessionOver();
	}

}
