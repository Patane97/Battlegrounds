package com.Patane.Battlegrounds.arena.editor.region;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorInfo;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.editor.EditorType;
import com.Patane.Battlegrounds.util.util;
import com.sk89q.worldedit.regions.AbstractRegion;

@EditorInfo(
		name = "lobby", permission = ""
	)
public class LobbyEditor implements EditorType{
	Plugin plugin;
	Arena arena;
	String arenaName;
	Player creator;
	EditorListeners listener;
	
	public LobbyEditor(Plugin plugin, Arena arena, Player creator, Editor editor){
		this.plugin		= plugin;
		this.arena 		= arena;
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
	}
	@Override
	public void initilize() {
		Messenger.send(creator, "&2Create new &7Lobby Region&2:"
							+ "\n&a Select a region with worldedit and"
							+ "\n&a type &7/bg save &ato save new lobby region");
	}
	@Override
	public void save() {
		AbstractRegion region = util.getAbstractRegion(plugin, creator);
		arena.setLobby(region);
		Arena.YML().saveRegion(arenaName, region, "Lobby");
		Messenger.send(creator, "&aSaved &7Lobby Region &afor Arena &7" + arenaName + "&a.");
	}

	@Override
	public EditorListeners getListener() {
		return listener;
	}
	
}
