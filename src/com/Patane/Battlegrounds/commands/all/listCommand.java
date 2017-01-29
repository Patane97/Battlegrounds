package com.Patane.Battlegrounds.commands.all;

import java.util.StringJoiner;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.standby.ArenaMode;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.commands.BGCommand;
import com.Patane.Battlegrounds.commands.CommandInfo;

@CommandInfo(
		name = "list",
		description = "Lists all arenas",
		usage = "/bg list",
		permission = ""
	)
public class listCommand implements BGCommand{

	@Override
	public boolean execute(Plugin plugin, Player sender, String[] args) {

		StringJoiner list = new StringJoiner("&7, ");
		int amount = 0;
		for(String arenaName : Arenas.getNames()){
			Arena arena = Arenas.grab(arenaName);
			ArenaMode mode = arena.getMode();
			String name = mode.getColor() + arenaName;
			if(!arena.isActive())
				name = "&c" + arenaName;			
			list.add(name);
			amount++;
		}
		Messenger.send(sender, "&fListed Arenas: ("+ amount +") " + list.toString());
		return true;
	}

}
