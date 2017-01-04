package com.Patane.Battlegrounds.commands.all;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.modes.ArenaMode;
import com.Patane.Battlegrounds.arena.modes.editor.Editor;
import com.Patane.Battlegrounds.arena.modes.game.Game;
import com.Patane.Battlegrounds.arena.modes.lobby.Lobby;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.commands.BGCommand;
import com.Patane.Battlegrounds.commands.CommandInfo;
@CommandInfo(
	name = "join",
	description = "Join an arena",
	usage = "/bg join [arena]",
	permission = ""
)
public class joinCommand implements BGCommand{

	@Override
	public boolean execute(Plugin plugin, Player sender, String[] args) {
		String arenaName = (args.length > 1 ? args[1] : null);
		Arena currentArena = Arenas.grab(sender);
		if (currentArena != null){
			Messenger.send(sender, "&cPlease leave your current game before starting a new one");
			return false;
		}
		if(arenaName == null){
			Messenger.send(sender, "&cPlease specify an arena! Usage: /bg join [arena]");
			return false;
		}
		Arena arena = Arenas.grab(arenaName);
		if(arena == null){
			Messenger.send(sender, "&cThis arena does not exist.");
			return false;
		}
		ArenaMode arenaMode = arena.getMode();
		if(arenaMode instanceof Editor){
			Messenger.send(sender, "&cThis arena is in edit mode");
			return false;
		}
		if(arena.hasEmptySpawns()){
			Messenger.send(sender, "&cThis arena is missing one or more spawns. Please ask a moderator to add missing spawns.");
			return false;
		}
		if(arenaMode instanceof Game){
			Messenger.send(sender, "&aThis arena is already in a game. Joining spectators...");
			Game game = (Game) arenaMode;
			game.addPlayer(sender);
			return true;
		}
		if(arenaMode instanceof Lobby){
			Lobby lobby = (Lobby) arenaMode;
			lobby.addPlayer(sender);
			return true;
		}
		arenaMode = arena.setMode(new Lobby(plugin, arena));
		arenaMode.addPlayer(sender);
		
		return true;
	}

}
