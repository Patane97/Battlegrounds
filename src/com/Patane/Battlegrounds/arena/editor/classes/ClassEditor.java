package com.Patane.Battlegrounds.arena.editor.classes;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.ArenaYML;
import com.Patane.Battlegrounds.arena.classes.BGClassYML;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorInfo;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.editor.EditorType;

@EditorInfo(
		name = "class", permission = ""
	)
public class ClassEditor implements EditorType{
	Plugin plugin;
	Arena arena;
	String arenaName;
	Player creator;
	
	public ClassEditor(Plugin plugin, Arena arena, Player creator, Editor editor){
		this.plugin 	= plugin;
		this.arena 		= arena;
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
	}
	@Override
	public void initilize() {
		new ClassesGUI(plugin, arena, "&8&l&o" + arenaName + " &2&lClasses", creator, this);
	}
	@Override
	public void save() {
		ArenaYML.saveClasses(arena.getName());
		BGClassYML.saveAllClasses();
		Messenger.send(creator, "&aSaved &7all classes&a.");
	}

	@Override
	public EditorListeners getListener() {
		return null;
	}
	
}
