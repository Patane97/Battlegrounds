package com.Patane.Battlegrounds.arena.modes.editor.classes;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.GUI.ClassMainPage;
import com.Patane.Battlegrounds.GUI.ClassPage;
import com.Patane.Battlegrounds.GUI.GUI;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.classes.BGClass;

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
		for(BGClass selectedClass : arena.getClasses()){
			ClassPage classPage = new ClassPage(this, mainPage, selectedClass);
			mainPage.addLink(selectedClass.getIcon(), classPage);
		}
	}
	@Override
	public void exit(){
		super.exit();
		// save classes to YML
		arena.getMode().sessionOver();
	}

}
