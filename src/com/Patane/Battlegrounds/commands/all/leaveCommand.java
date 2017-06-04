package com.Patane.Battlegrounds.commands.all;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.commands.BGCommand;
import com.Patane.Battlegrounds.commands.CommandInfo;
@CommandInfo(
		name = "leave",
		description = "Leave an arena",
		usage = "/bg leave",
		permission = ""
	)
public class leaveCommand implements BGCommand{

	@Override
	public boolean execute(Plugin plugin, Player sender, String[] args) {
		Arena arena = Arenas.grab(sender);
		if(arena != null){
			arena.getMode().removePlayer(sender.getDisplayName(), true);
			return true;
		}
		arena = Arenas.grabSpect(sender);
		if(arena != null){
			arena.leaveSpectator(sender);
			return true;
		}
		Messenger.send(sender, "&cYou must be in an arena to leave it!");
		return false;
	}

}
