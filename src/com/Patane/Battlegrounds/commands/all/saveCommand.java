package com.Patane.Battlegrounds.commands.all;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.modes.editor.Editor;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.commands.BGCommand;
import com.Patane.Battlegrounds.commands.CommandInfo;
@CommandInfo(
		name = "save",
		description = "Save an arena whilst in edit mode",
		usage = "/bg save",
		permission = ""
	)
public class saveCommand implements BGCommand{

	@Override
	public boolean execute(Plugin plugin, Player sender, String[] args) {
		for(Arena arena : Arenas.get()){
			if(arena.getMode() instanceof Editor){
				Editor editor = (Editor) arena.getMode();
				if(sender.getDisplayName().equals(editor.getCreator().getDisplayName())){
					editor.sessionOver();
					return true;
				}
			}
		}
		Messenger.send(sender, "&cYou must be editing an arena to save it.");
		return false;
	}

}
