package com.Patane.Battlegrounds.commands.all;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.commands.BGCommand;
import com.Patane.Battlegrounds.commands.CommandInfo;
@CommandInfo(
	name = "create",
	description = "Create an arena",
	usage = "/bg create [arena]",
	permission = ""
)
public class createCommand implements BGCommand{

	@Override
	public boolean execute(Plugin plugin, Player sender, String[] args) {
		String arenaName = (args.length > 1 ? args[1] : null);
		if(arenaName == null){
			Messenger.send(sender, "&cYou must give the arena a name. Eg. /bg create [arena]");
			return false;
		}
		Arena arena = Arenas.grab(arenaName);
		if(arena != null){
			Messenger.send(sender, "&7" + arena.getName() + "&c is already the name of an arena. Please choose another");
			return false;
		}
		new Editor(plugin, arenaName, sender);
		return true;
	}

}
