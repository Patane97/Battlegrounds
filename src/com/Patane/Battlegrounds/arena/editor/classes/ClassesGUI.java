package com.Patane.Battlegrounds.arena.editor.classes;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.GUI.ChestGUI;
import com.Patane.Battlegrounds.GUI.Page;
import com.Patane.Battlegrounds.GUI.classes.ClassMainPage;
import com.Patane.Battlegrounds.GUI.classes.ClassPage;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.collections.Classes;

public class ClassesGUI extends ChestGUI{
	Arena arena;
	
	ClassEditor classEditor;
	
	public ClassesGUI(Plugin plugin, Arena arena, String name, Player player, ClassEditor classEditor) {
		super(plugin, player, name);
		this.arena = arena;
		setMainPage(new ClassMainPage(this, name, 27));
		this.classEditor = classEditor;
	}
	public Arena getArena(){
		return arena;
	}
	@Override
	public void exit(){
		super.exit();
		for(Page page : mainPage.getLinks()){
			if(page instanceof ClassPage){
				ClassPage classPage = (ClassPage) page;
				classPage.saveToClass();
			}
		}
		arena.getMode().sessionOver();
	}
	public boolean checkClassExisting(String itemName) {
		if(arena.hasClass(itemName)){
			Messenger.send(player, "&cThere is already a class in this arena with this name.");
			return true;
		} else if(Classes.contains(itemName)){
			Messenger.send(player, "&cThere is already a class with that name. Add it from the &6All Classes &cmenu.");
			return true;
		}
		return false;
	}

}
