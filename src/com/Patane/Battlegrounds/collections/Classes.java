package com.Patane.Battlegrounds.collections;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;

import com.Patane.Battlegrounds.arena.classes.BGClass;

public class Classes {
	private static HashMap<String, BGClass> classes = new HashMap<String, BGClass>();

	public static BGClass add(BGClass bgClass){
		return classes.put(bgClass.getName(), bgClass);
	}
	public static void addAll(ArrayList<BGClass> classList){
		for(BGClass selectedClass : classList){
			if(selectedClass.getIcon().getType() == Material.AIR){
				BGClass.YML().clearSection(selectedClass.getName());
				continue;
			}
			add(selectedClass);
		}
	}
	public static boolean remove(String className){
		if(classes.remove(className) != null){
			BGClass.YML().clearSection(className);
			return true;
		}
		return false;
	}
	public static BGClass grab(String className){
		for(String selectedClassName : classes.keySet()){
			if(className.equalsIgnoreCase(selectedClassName))
				return classes.get(selectedClassName);
		}
		return null;
	}
	public static ArrayList<BGClass> get(){
		return new ArrayList<BGClass>(classes.values());
	}
	public static ArrayList<String> getNames(){
		return new ArrayList<String>(classes.keySet());
	}
	public static boolean contains(String className) {
		for(String selectedClassName : classes.keySet()){
			if(className.equalsIgnoreCase(selectedClassName))
				return true;
		}
		return false;
	}
	/**
	 * Used to determine if className contains the name of a class
	 * 
	 * @param className string to check
	 * @return a raw string of the class name contained within className
	 */
	public static String partlyContains(String className) {
		for(String selectedClassName : classes.keySet()){
			if(className.contains(selectedClassName))
				return selectedClassName;
		}
		return null;
	}
	public static void replace(BGClass thisClass, BGClass thatClass) {
		HashMap<String, BGClass> temp = new HashMap<String, BGClass>();
		for(String className : classes.keySet()){
			if(className.equalsIgnoreCase(thisClass.getName()))
				temp.put(thatClass.getName(), thatClass);
			else
				temp.put(className, classes.get(className));
		}
		classes = temp;
	}
}
