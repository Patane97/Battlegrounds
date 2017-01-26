package com.Patane.Battlegrounds.commands.all;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.game.Game;
import com.Patane.Battlegrounds.arena.lobby.Lobby;
import com.Patane.Battlegrounds.arena.standby.ArenaMode;
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
			Messenger.send(sender, "&cPlease specify an arena! Usage: &7/bg join [arena]");
			return false;
		}
		Arena arena = Arenas.grab(arenaName);
		if(arena == null){
			Messenger.send(sender, "&cArena &7" + arenaName + "&c does not exist");
			return false;
		}
		ArenaMode arenaMode = arena.getMode();
		if(arenaMode instanceof Editor){
			Messenger.send(sender, "&cArena &7" + arenaName + "&c is currently in edit mode");
			return false;
		}
		if(!arena.isActive()){
			Messenger.send(sender, "&cThis arena is currently inactive. Type &7/bg status [arena] &cfor more information");
			return false;
		}
		if(arenaMode instanceof Game){
			Messenger.send(sender, "&aArena is already in a game. Joining spectators...");
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
