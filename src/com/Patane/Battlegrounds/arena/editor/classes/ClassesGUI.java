package com.Patane.Battlegrounds.arena.editor.classes;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.GUI.ClassMainPage;
import com.Patane.Battlegrounds.GUI.ClassPage;
import com.Patane.Battlegrounds.GUI.GUI;
import com.Patane.Battlegrounds.GUI.Page;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.collections.Classes;

public class ClassesGUI extends GUI{
	Arena arena;
	
	ClassEditor classEditor;
	
	public ClassesGUI(Plugin plugin, Arena arena, String name, Player player, ClassEditor classEditor) {
		super(plugin, name, player);
		setMainPage(new ClassMainPage(this, name, 27));
		this.arena = arena;
		this.classEditor = classEditor;
		initializeClasses();
	}
	public Arena getArena(){
		return arena;
	}
	public void initializeClasses(){
		for(String selectedClass : arena.getClasses()){
			BGClass bgClass = Classes.grab(selectedClass);
			if(bgClass == null)
				break;
			ClassPage classPage = new ClassPage(this, mainPage, bgClass);
			mainPage.addLink(bgClass.getIcon(), classPage);
		}
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

}
