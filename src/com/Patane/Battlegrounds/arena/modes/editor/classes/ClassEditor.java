package com.Patane.Battlegrounds.arena.modes.editor.classes;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.ArenaYML;
import com.Patane.Battlegrounds.arena.modes.editor.Editor;
import com.Patane.Battlegrounds.arena.modes.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.modes.editor.EditorType;

public class ClassEditor implements EditorType{
	Arena arena;
	String arenaName;
	Player creator;
	
	public ClassEditor(Plugin plugin, Arena arena, Player creator, Editor editor){
		this.arena 		= arena;
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
		@SuppressWarnings("unused")
		ClassesGUI classesDisplay = new ClassesGUI(plugin, arena, "Arena '" + arenaName + "' Classes", creator, this);
	}
	@Override
	public void save() {
		ArenaYML.saveClasses(arena.getName());
		Messenger.send(creator, "&aArena '&7" + arenaName + "&a' classes succesfully saved!");
	}

	@Override
	public EditorListeners getListener() {
		return null;
	}
	
}
