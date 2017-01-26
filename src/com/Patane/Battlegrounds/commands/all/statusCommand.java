package com.Patane.Battlegrounds.commands.all;

import java.util.ArrayList;
import java.util.StringJoiner;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.game.Game;
import com.Patane.Battlegrounds.arena.lobby.Lobby;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.commands.BGCommand;
import com.Patane.Battlegrounds.commands.CommandInfo;
@CommandInfo(
	name = "status",
	description = "Check status of arena",
	usage = "/bg status [arena]",
	permission = ""
)
public class statusCommand implements BGCommand{

	@Override
	public boolean execute(Plugin plugin, Player sender, String[] args) {
		String arenaName = (args.length > 1 ? args[1] : null);
		Arena currentArena = Arenas.grab(sender);
		if (currentArena != null){
			Messenger.send(sender, "&cPlease leave your current game before starting a new one");
			return false;
		}
		if(arenaName == null){
			Messenger.send(sender, "&cPlease specify an arena! Usage: &7bg status [arena]");
			return false;
		}
		Arena arena = Arenas.grab(arenaName);
		if(arena == null){
			Messenger.send(sender, "&cArena &7" + arenaName + "&c does not exist");
			return false;
		}
		String active = (arena.isActive() ? "&aACTIVE" : "&cINACTIVE");
		if(arena.getMode() instanceof Lobby)
			active = "&aIN LOBBY";
		else if(arena.getMode() instanceof Game)
			active = "&eIN GAME";
		String groundRegion = (arena.hasGround() ? "&aSaved" : "&cMissing");
		String lobbyRegion = (arena.hasLobby() ? "&aSaved" : "&cMissing");

		StringJoiner classesString = new StringJoiner("&7, ");
		
		String classes = "&cNone";
		ArrayList<String> classesList = arena.getClasses();
		if(!classesList.isEmpty()){
			for(String selectedClass : classesList)
				classesString.add("&a" + selectedClass);
			classes = classesString.toString();
		}
		StringJoiner emptySpawnString = new StringJoiner("&7, ");
		String spawnsStatus = "&aNone";
		ArrayList<String> emptySpawns = arena.getEmptySpawnLists();
		if(!emptySpawns.isEmpty()){
			for(String spawn : emptySpawns)
				emptySpawnString.add("&c" + spawn.substring(2));
			spawnsStatus = emptySpawnString.toString();
		}
		Messenger.send(sender, "&2Arena &7" + arena.getName() + "&2 Status Report:");
		Messenger.sendRaw(sender, "  &2Current Status: " 	+ active);
		Messenger.sendRaw(sender, "  &2Ground Region: " 	+ groundRegion);
		Messenger.sendRaw(sender, "  &2Lobby Region: " 		+ lobbyRegion);
		Messenger.sendRaw(sender, "  &2Empty Spawns: " 		+ spawnsStatus);
		Messenger.sendRaw(sender, "  &2Available Classes: " + classes);
		return true;
	}

}
