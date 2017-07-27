package com.Patane.Battlegrounds.arena.editor.waves;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorInfo;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.editor.EditorType;

@EditorInfo(
		name = "waves", permission = ""
	)
public class WavesEditor implements EditorType{
	Plugin plugin;
	Arena arena;
	String arenaName;
	Player creator;
	
	public WavesEditor(Plugin plugin, Arena arena, Player creator, Editor editor){
		this.plugin 	= plugin;
		this.arena 		= arena;
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
	}
	@Override
	public void initilize() {
		creator.openInventory((new WavesGUI(plugin, arena, "&8&l&o" + arenaName + "&2&l Waves", creator, this)).inventory());
	}
	@Override
	public void save() {
//		arena.setSettings(new ArenaSettings(arena));
//		Arena.YML().saveSettings(arenaName);
		Messenger.send(creator, "&aSaved &7" + arena.getName() + "&a waves.");
	}

	@Override
	public EditorListeners getListener() {
		return null;
	}
	
}
