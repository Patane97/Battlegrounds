package com.Patane.Battlegrounds.commands.all;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.commands.BGCommand;
import com.Patane.Battlegrounds.commands.CommandInfo;
@CommandInfo(
	name = "spectate",
	description = "Spectate an arena",
	usage = "/bg spectate [arena]",
	permission = ""
)
public class spectateCommand implements BGCommand{

	@Override
	public boolean execute(Plugin plugin, Player sender, String[] args) {
		String arenaName = (args.length > 1 ? args[1] : null);
		Arena currentArena = Arenas.grab(sender);
		if (currentArena != null){
			Messenger.send(sender, "&cPlease leave your current game before spectating");
			return false;
		}
		if(arenaName == null){
			Messenger.send(sender, "&cPlease specify an arena!"
							   + "\n&7 Usage: &7/bg join [arena]");
			return false;
		}
		Arena arena = Arenas.grab(arenaName);
		if(arena == null){
			Messenger.send(sender, "&cArena &7" + arenaName + "&c does not exist");
			return false;
		}
		arena.joinSpectator(sender);
		return true;
	}

}
