package com.Patane.Battlegrounds.arena.editor.build;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorInfo;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.editor.EditorType;

@EditorInfo(
		name = "build", permission = ""
	)
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
		
	}
	@Override
	public void initilize() {
		Messenger.send(creator, "&2Build/Break blocks within &7Ground &2and &7Lobby Regions&2:");
		Messenger.sendRaw(creator, "&a You can now break and place blocks in this arena's ground and lobby regions.");
		Messenger.sendRaw(creator, "&a Type &7/bg save &ato save arena!");
	}
	@Override
	public void save() {
		Messenger.send(creator, "&aSaved &7All Blocks &awithin Arena &7" + arenaName + "&a.");
	}

	@Override
	public EditorListeners getListener() {
		return listener;
	}
	
}
