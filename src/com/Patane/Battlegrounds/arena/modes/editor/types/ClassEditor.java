package com.Patane.Battlegrounds.arena.modes.editor.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.modes.editor.Editor;
import com.Patane.Battlegrounds.arena.modes.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.modes.editor.EditorType;

public class ClassEditor implements EditorType{
	Plugin plugin;
	Arena arena;
	String arenaName;
	Player creator;
	EditorListeners listener;
	Inventory classGUI;
	
	public ClassEditor(Plugin plugin, Arena arena, Player creator, Editor editor){
		this.plugin 	= plugin;
		this.arena 		= arena;
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
		this.listener	= new ClassEditListeners(plugin, arena, this, editor);
		openClassGUI();
		
	}
	public void openClassGUI(){
		classGUI = plugin.getServer().createInventory(null, 54, "Classes");
		ItemStack newClassItem = new ItemStack(Material.DIRT);
		ItemMeta newClassItemMeta = newClassItem.getItemMeta();
		newClassItemMeta.setDisplayName("Create new class");
		newClassItem.setItemMeta(newClassItemMeta);
		classGUI.setItem(53, newClassItem);
		creator.openInventory(classGUI);
	}
	@Override
	public void save() {
		Messenger.send(creator, "&aArena '&7" + arenaName + "&a' succesfully saved!");
	}

	@Override
	public EditorListeners getListener() {
		return listener;
	}
	public Inventory getClassInventory() {
		return classGUI;
	}
	
}
