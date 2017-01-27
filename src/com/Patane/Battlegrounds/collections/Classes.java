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
		classes.put(convert(bgClass.getName()), bgClass);
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
		className = convert(className);
		if(classes.remove(className) != null){
			BGClassYML.remove(className);
			return true;
		}
		return false;
	}
	public static BGClass grab(String className){
		className = convert(className);
		return classes.get(className);
	}
	public static ArrayList<BGClass> get(){
		ArrayList<BGClass> tempClasses = new ArrayList<BGClass>();
		for(BGClass selectedClass : classes.values())
			tempClasses.add(selectedClass);
		return tempClasses;
	}
	public static boolean contains(String className) {
		return classes.containsKey(convert(className));
	}
	public static String partlyContains(String className) {
		className = convert(className);
		for(String selectedClassName : classes.keySet()){
			if(className.contains(selectedClassName))
				return selectedClassName;
		}
		return null;
	}
	private static String convert(String name){
		return ChatColor.stripColor(name.toUpperCase());
	}
}
