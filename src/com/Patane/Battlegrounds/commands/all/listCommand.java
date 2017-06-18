package com.Patane.Battlegrounds.commands.all;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.commands.BGCommand;
import com.Patane.Battlegrounds.commands.CommandInfo;
import com.Patane.Battlegrounds.util.util;

@CommandInfo(
		name = "list",
		description = "Lists all arenas",
		usage = "/bg list",
		permission = ""
	)
public class listCommand implements BGCommand{

	@Override
	public boolean execute(Plugin plugin, Player sender, String[] args) {
		ArrayList<String> arenas = Arenas.getNames(true);
		Messenger.send(sender, "&fListed Arenas: ["+ arenas.size() +"] " + util.stringJoiner(arenas, "&7, "));
		return true;
	}

}
