package com.Patane.Battlegrounds.commands.all;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.modes.editor.Editor;
import com.Patane.Battlegrounds.arena.modes.editor.classes.ClassEditor;
import com.Patane.Battlegrounds.arena.modes.editor.types.BuildEditor;
import com.Patane.Battlegrounds.arena.modes.editor.types.SpawnEditor;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.commands.BGCommand;
import com.Patane.Battlegrounds.commands.CommandInfo;

@CommandInfo(
	name = "edit",
	description = "Edit an arena",
	usage = "/bg edit [arena] [build|spawns]",
	permission = ""
)
public class editCommand implements BGCommand{

	@Override
	public boolean execute(Plugin plugin, Player sender, String[] args) {
		String arenaName = (args.length > 1 ? args[1] : null);
		String editType = (args.length > 2 ? args[2] : null);
		if(arenaName == null){
			Messenger.send(sender, "&cPlease specify an arena!");
			return false;
		}
		if(editType == null){
			Messenger.send(sender, "&cPlease specify an editing type (build or spawns)");
			return false;
		}
		Arena arena = Arenas.grab(arenaName);
		if (arena == null){
			Messenger.send(sender, "&cThis arena does not exist.");
			return false;
		}
		if(arena.getMode() instanceof Editor){
			Editor editor = (Editor) arena.getMode();
			Messenger.send(sender, editor.getCreator().getDisplayName() + " &cis already editing this arena!");
			return false;
		}
		// NEED TO CHANGE THIS TO REGISTER EACH EDIT TYPE (like commands) AND THEN SEARCH FOR KEYWORDS
		Editor editor = (Editor) arena.setMode(new Editor(plugin, arena, sender));
		if(editType.toLowerCase().contains("spawn")){
			editor.newEditorType(new SpawnEditor(plugin, arena, sender, editor));
		} else if (editType.toLowerCase().contains("build")){
			editor.newEditorType(new BuildEditor(plugin, arena, sender, editor));
		} else if (editType.toLowerCase().contains("class")){
			editor.newEditorType(new ClassEditor(plugin, arena, sender, editor));
		} else{
			Messenger.send(sender, "&cPlease type a valid editor type (eg. /bg edit [arena] build)");
			return false;
		}
		return true;
	}

}
