package com.Patane.Battlegrounds.arena.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.collections.Classes;
import com.Patane.Battlegrounds.util.Config;
import com.Patane.Battlegrounds.util.YML;
import com.Patane.Battlegrounds.util.util;

public class BGClassYML {
	static Config classConfig;
	static Plugin plugin;
	static ConfigurationSection classSection;
	
	public static void load(Plugin battlegrounds) {
		plugin			= battlegrounds;
		classConfig 	= new Config(plugin, "classes.yml");
		classSection = (classConfig.isConfigurationSection("classes") 
						? classConfig.getConfigurationSection("classes") 
						: classConfig.createSection("classes"));
		Classes.addAll(loadAllClasses());
	}
	
	public static ArrayList<BGClass> loadAllClasses(){
		ArrayList<BGClass> bgClasses = new ArrayList<BGClass>();
		classSection = classConfig.getConfigurationSection("classes");
		for(String className : classSection.getKeys(false)){
			BGClass bgClass = loadClass(className);
			if(bgClass == null){
				Messenger.warning("Class '" + className + "' not recognised in classes.yml!");
				continue;
			}
			// for some reason this works WITHOUT this 'add' code. find out why.
			bgClasses.add(bgClass);
		}
		return bgClasses;
	}
	public static boolean saveClass(BGClass bgClass){
		try{
			if(bgClass == null)
				return false; 
			classSection = classConfig.getConfigurationSection("classes");
			classSection.set(bgClass.getName().toUpperCase(), null);
			classSection = classConfig.createSection("classes." + bgClass.getName().toUpperCase());
			ItemStack[] inv 		= bgClass.getInventory().getContents();
			ItemStack[] armor 		= new ItemStack[4];
			armor 					= Arrays.copyOfRange(inv, 0, 4);
			ItemStack offHand 		= inv[4];
			ItemStack[] contents 	= new ItemStack[36];
			contents 				= Arrays.copyOfRange(inv, 9, inv.length);
			
			classSection.set("Icon", bgClass.getIcon());
			classSection.set("Armor", armor);
			classSection.set("OffHand", offHand);
			for(Entry<Integer, ItemStack> entry : YML.playerInventoryContentsFormat(contents).entrySet())
				classSection.set("Contents." + entry.getKey(), entry.getValue());
			classConfig.save();
			return true;
		} catch (Exception e){
			Messenger.warning("Class '" + bgClass.getName() + "' failed to save.");
			e.printStackTrace();
			return false;
		}
	}
	public static void saveAllClasses(){
		classConfig.get("classes", null);
		for(BGClass selectedClass : Classes.get())
			saveClass(selectedClass);
	}
	public static BGClass loadClass(String className){
		try{
			classSection = classConfig.getConfigurationSection("classes." + className);
			
			ItemStack icon = classSection.getItemStack("Icon");
				
			@SuppressWarnings("unchecked")
			List<ItemStack> itemList = (List<ItemStack>) classSection.get("Armor");
			ItemStack[] armor = (ItemStack[]) itemList.toArray(new ItemStack[0]);
			
			ItemStack offHand = classSection.getItemStack("OffHand");
			// TO BE IMPLEMENTED!
	//		ItemStack[] effects = new ItemStack[4];
			
			classSection = (classConfig.isConfigurationSection("classes." + className + ".Contents") 
							? classConfig.getConfigurationSection("classes." + className + ".Contents")
							: classConfig.createSection("classes." + className + ".Contents"));
			// recalling item slots + items to be used later
			HashMap<Integer, ItemStack> printedItems = new HashMap<Integer, ItemStack>();
			for(String slotNo : classSection.getKeys(false))
				printedItems.put(Integer.parseInt(slotNo), classSection.getItemStack(slotNo));
			// creating a contents of inventory from previous slots + items hashmap
			ItemStack[] contents = YML.getPlayerInventoryContents(printedItems);
			
			ItemStack[] classInventory = new ItemStack[45];
			int i, b;
			// adding armor
			for(i = 0, b = i ; i < armor.length ; i++)
				classInventory[i] = armor[i-b];
			//adding offhand
			classInventory[4] = offHand;
			// adding contents
			for(i = 9, b = i ; i < contents.length + 9; i++)
				classInventory[i] = contents[i-b];
			className = util.formaliseString(className);
			
			if(icon.hasItemMeta() && !icon.getItemMeta().getDisplayName().equalsIgnoreCase(className))
				className = icon.getItemMeta().getDisplayName();

			BGClass newClass = new BGClass(plugin, className, icon, classInventory);
			return newClass;
		} catch (Exception e){
			Messenger.warning("Class '" + className + "' failed to load.");
			e.printStackTrace();
			return null;
		}
	}
	public static void remove(String className){
		classSection = classConfig.getConfigurationSection("classes");
		classSection.set(className, null);
		classConfig.save();
	}
}
