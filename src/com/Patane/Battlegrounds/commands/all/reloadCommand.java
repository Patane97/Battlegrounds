package com.Patane.Battlegrounds.commands.all;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.commands.BGCommand;
import com.Patane.Battlegrounds.commands.CommandInfo;

@CommandInfo(
		name = "reload",
		description = "Reloads the plugins configs",
		usage = "/bg reload",
		permission = ""
	)
public class reloadCommand implements BGCommand{

	@Override
	public boolean execute(Plugin plugin, Player sender, String[] args) {
		Messenger.send(sender, "&cFeature currently not implemented yet! :(");
		// need to make one of these to reload all configs.
//		plugin.reloadConfig();
		return true;
	}

}
