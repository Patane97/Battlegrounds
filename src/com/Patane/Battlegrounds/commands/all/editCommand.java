package com.Patane.Battlegrounds.commands.all;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorHandler;
import com.Patane.Battlegrounds.arena.editor.EditorType;
import com.Patane.Battlegrounds.arena.game.Game;
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
		Arena arena = Arenas.grab(arenaName);
		if (arena == null){
			Messenger.send(sender, "&cArena &7" + arenaName + "&c does not exist");
			return false;
		}
		if(editType == null){
			Messenger.send(sender, "&cPlease specify an editing type");
			return false;
		}
		if(arena.getMode() instanceof Game){
			Messenger.send(sender, "&cCannot edit &7" + arena.getName() + " &cwhilst there is a game running!"
							   + "\n&7 You can choose to end the game with the /bg end " + arena.getName() +" command [NOT IMPLEMENTED YET]");
			return false;
		}
		if(arena.getMode() instanceof Editor){
			Editor editor = (Editor) arena.getMode();
			Messenger.send(sender, editor.getCreator().getDisplayName() + " &cis already editing this arena!");
			return false;
		}
		Class<? extends EditorType> editorClass = EditorHandler.getEditorType(editType);
		if(editorClass == null){
			Messenger.send(sender, "&cPlease type a valid editor type (eg. /bg edit " + arena.getName() + " build)");
			return false;
		}
		Editor editor = (Editor) arena.setMode(new Editor(plugin, arena, sender));
		try {
			EditorType editorType = editorClass.getConstructor(Plugin.class, Arena.class, Player.class, Editor.class).newInstance(plugin, arena, sender, editor);
			editor.newEditorType(editorType);
		} catch (Exception e) {	e.printStackTrace();}
		return true;
	}

}
