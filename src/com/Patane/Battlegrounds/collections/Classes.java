package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.classes.BGClassYML;

public class Classes {
	private static HashMap<String, BGClass> classes = new HashMap<String, BGClass>();

	public static BGClass add(BGClass bgClass, boolean save){
		classes.put(ChatColor.stripColor(bgClass.getName()).toUpperCase(), bgClass);
		if(save)
			BGClassYML.saveClass(bgClass);
		return bgClass;
	}
	public static void addAll(ArrayList<BGClass> classList){
		for(BGClass selectedClass : classList){
			if(selectedClass.getIcon().getType() == Material.AIR){
				BGClassYML.remove(selectedClass.getName());
				continue;
			}
			add(selectedClass, false);
		}
	}
	public static boolean remove(String className){
		if(classes.remove(className.toUpperCase()) != null){
			BGClassYML.remove(className.toUpperCase());
			return true;
		}
		return false;
	}
	public static BGClass grab(String className){
		return classes.get(className.toUpperCase());
	}
	public static ArrayList<BGClass> get(){
		ArrayList<BGClass> tempClasses = new ArrayList<BGClass>();
		for(BGClass selectedClass : classes.values())
			tempClasses.add(selectedClass);
		return tempClasses;
	}
	public static boolean contains(String className) {
		return classes.containsKey(className.toUpperCase());
	}
	public static String convertName(String className) {
		for(String selectedClassName : classes.keySet()){
			if(className.toUpperCase().contains(selectedClassName))
				return selectedClassName;
		}
		return null;
	}
}
