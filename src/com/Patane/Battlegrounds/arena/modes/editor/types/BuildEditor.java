package com.Patane.Battlegrounds.arena.modes.editor.types;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.modes.editor.Editor;
import com.Patane.Battlegrounds.arena.modes.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.modes.editor.EditorType;

public class BuildEditor implements EditorType{
	Arena arena;
	String arenaName;
	Player creator;
	EditorListeners listener;
	
	public BuildEditor(Plugin plugin, Arena arena, Player creator, Editor editor){
		this.arena 		= arena;
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
		this.listener	= new BuildEditListeners(plugin, arena, this, editor);
		Messenger.send(creator, "&2===== &aBuild Editor! &2=====");
		Messenger.sendRaw(creator, "&a You can now break and place blocks in this arena.");
		Messenger.sendRaw(creator, "&2 Type &a/bg save &2when you're done!");
		
	}
	@Override
	public void save() {
		Messenger.send(creator, "&aArena '&7" + arenaName + "&a' succesfully saved!");
	}

	@Override
	public EditorListeners getListener() {
		return listener;
	}
	
}
