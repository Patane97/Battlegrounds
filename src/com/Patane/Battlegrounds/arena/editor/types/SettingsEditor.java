package com.Patane.Battlegrounds.arena.editor.types;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.GUI.ChestGUI;
import com.Patane.Battlegrounds.GUI.settings.SettingsMainPage;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorInfo;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.editor.EditorType;
import com.Patane.Battlegrounds.arena.settings.ArenaSettings;

@EditorInfo(
		name = "settings", permission = ""
	)
public class SettingsEditor implements EditorType{
	Plugin plugin;
	Arena arena;
	String arenaName;
	Player creator;
	
	public SettingsEditor(Plugin plugin, Arena arena, Player creator, Editor editor){
		this.plugin 	= plugin;
		this.arena 		= arena;
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
	}
	@Override
	public void initilize() {
		creator.openInventory((new SettingsGUI(plugin, arena, "&8&l&o" + arenaName + "&2&l Settings", creator, this)).inventory());
	}
	@Override
	public void save() {
		arena.setSettings(new ArenaSettings(arena));
		Arena.YML().saveSettings(arenaName);
		Messenger.send(creator, "&aSaved &7" + arena.getName() + "&a settings.");
	}

	@Override
	public EditorListeners getListener() {
		return null;
	}
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
}
