package com.Patane.Battlegrounds.arena.editor.region;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.ArenaYML;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorInfo;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.editor.EditorType;
import com.Patane.Battlegrounds.util.util;
import com.sk89q.worldedit.regions.AbstractRegion;

@EditorInfo(
		name = "ground", permission = ""
	)
public class GroundEditor implements EditorType{
	Plugin plugin;
	Arena arena;
	String arenaName;
	Player creator;
	EditorListeners listener;
	
	public GroundEditor(Plugin plugin, Arena arena, Player creator, Editor editor){
		this.plugin		= plugin;
		this.arena 		= arena;
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
	}
	@Override
	public void initilize() {
		Messenger.send(creator, "&2Create new &7Ground Region&2:");
		Messenger.sendRaw(creator, "&a Select a region with worldedit and");
		Messenger.sendRaw(creator, "&a type &7/bg save &ato save new ground region");
	}
	@Override
	public void save() {
		AbstractRegion region = util.getAbstractRegion(plugin, creator);
		arena.setGround(region);
		ArenaYML.saveRegion(arenaName, region, "Ground");
		Messenger.send(creator, "&aSaved &7Ground Region &afor Arena &7" + arenaName + "&a.");
	}

	@Override
	public EditorListeners getListener() {
		return listener;
	}
	
}
