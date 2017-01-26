package com.Patane.Battlegrounds.commands.all;

import java.util.StringJoiner;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.game.Game;
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

		StringJoiner list = new StringJoiner(", ");
		int amount = 0;
		for(String arenaName : Arenas.getNames()){
			Arena arena = Arenas.grab(arenaName);
			String name = "&a" + arenaName;
			if(arena.getMode() instanceof Editor || arena.hasEmptySpawns())
				name = "&c" + arenaName;
			else if (arena.getMode() instanceof Game)
				name = "&6" + arenaName;
			list.add(name);
			amount++;
		}
		Messenger.send(sender, "&fListed Arenas: ("+ amount +") " + list.toString());
		return true;
	}

}
