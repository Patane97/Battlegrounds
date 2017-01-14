package com.Patane.Battlegrounds.commands.all;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.modes.lobby.Lobby;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.commands.BGCommand;
import com.Patane.Battlegrounds.commands.CommandInfo;
@CommandInfo(
	name = "class",
	description = "Choose a class if your in a lobby",
	usage = "/bg class [class]",
	permission = ""
)
public class classCommand implements BGCommand{

	@Override
	public boolean execute(Plugin plugin, Player sender, String[] args) {
		String className = (args.length > 1 ? args[1] : null);
		Arena arena = Arenas.grab(sender);
		if (arena == null || !(arena.getMode() instanceof Lobby)){
			Messenger.send(sender, "&cYou must be in a lobby to choose a class!");
			return false;
		}
		if(className == null){
			Messenger.send(sender, "&cPlease specify which class you would like to change to");
			return false;
		}
		BGClass bgClass = arena.getClass(className);
		if(bgClass == null){
			Messenger.send(sender, "&cThere is no class of that name");
			return false;
		}
		arena.equipClass(sender, bgClass);
		return true;
	}

}
