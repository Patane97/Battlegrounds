package com.Patane.Battlegrounds.commands.all;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.ArenaYML;
import com.Patane.Battlegrounds.arena.modes.editor.Editor;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.commands.BGCommand;
import com.Patane.Battlegrounds.commands.CommandInfo;
@CommandInfo(
		name = "remove",
		description = "Remove an arena",
		usage = "/bg remove [arena]",
		permission = ""
	)
public class removeCommand implements BGCommand{

	@Override
	public boolean execute(Plugin plugin, Player sender, String[] args) {
		String arenaName = (args.length > 1 ? args[1] : null);
		if(arenaName == null){
			Messenger.send(sender, "&cYou must specify an arena name. Eg. /bg remove [arena name]");
			return false;
		}
		Arena arena = Arenas.grab(args[1]);
		if(arena.getMode() instanceof Editor){
			Messenger.send(sender, "&cArena '" + arena.getName() + "' is in edit mode and cannot be removed");
			return false;
		}
		if(arena != null && Arenas.remove(arena)){
			Messenger.arenaCast(arena, "&cYou have been kicked because this arena is being removed.");
			arena.getMode().sessionOver();
			ArenaYML.remove(arena);
			Messenger.send(sender, "&aArena '" + arena.getName() + "' removed!");
		}
		else{
			Messenger.send(sender, "&cThere is no arena with that name");
			return false;
		}
		return true;
	}

}
