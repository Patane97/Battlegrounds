package com.Patane.Battlegrounds.arena.editor.types;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.GUI.ChestGUI;
import com.Patane.Battlegrounds.GUI.Page;
import com.Patane.Battlegrounds.GUI.classes.ClassMainPage;
import com.Patane.Battlegrounds.GUI.classes.ClassPage;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorInfo;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.editor.EditorType;
import com.Patane.Battlegrounds.collections.Classes;

@EditorInfo(
		name = "class", permission = ""
	)
public class ClassEditor implements EditorType{
	Plugin plugin;
	Arena arena;
	String arenaName;
	Player creator;
	
	public ClassEditor(Plugin plugin, Arena arena, Player creator, Editor editor){
		this.plugin 	= plugin;
		this.arena 		= arena;
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
	}
	@Override
	public void initilize() {
		creator.openInventory((new ClassesGUI(plugin, arena, "&8&l&o" + arenaName + " &2&lClasses", creator, this)).inventory());
	}
	@Override
	public void save() {
		Arena.YML().saveClasses(arena.getName());
		BGClass.YML().save();
		Messenger.send(creator, "&aSaved &7all classes&a.");
	}

	@Override
	public EditorListeners getListener() {
		return null;
	}
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
}
