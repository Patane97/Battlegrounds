package com.Patane.Battlegrounds.arena.modes.editor.types;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.modes.editor.Editor;
import com.Patane.Battlegrounds.arena.modes.editor.EditorListeners;

public class ClassEditListeners extends EditorListeners{
	ClassEditor classEditor;
	public ClassEditListeners(Plugin plugin, Arena arena, ClassEditor classEditor, Editor editor) {
		super(plugin, arena, editor);
		this.classEditor = classEditor;
	}
	
	@EventHandler
	public void onNewClass(InventoryClickEvent event){
		if(event.getClickedInventory().equals(classEditor.getClassInventory())){
			if(event.getSlot() == 53){
				event.setCancelled(true);
				Messenger.send(event.getWhoClicked(), "CREATING NEW CLASS!");
			}
		}
	}

}
