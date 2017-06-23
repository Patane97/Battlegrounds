package com.Patane.Battlegrounds.arena.classes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.BasicYML;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.collections.Classes;
import com.Patane.Battlegrounds.util.YML;
import com.Patane.Battlegrounds.util.util;

public class BGClassYML extends BasicYML{
	
	public BGClassYML(Plugin plugin){
		super(plugin, "classes.yml", "classes");
	}
	
	@Override
	public  void save(){
		for(BGClass selectedClass : Classes.get())
			save(selectedClass);
		Messenger.info("Successfully saved Classes: " + util.stringJoiner(Classes.getNames(), ", "));
	}
	
	@Override
	public  void load(){
		for(String className : header.getKeys(false)){
			BGClass bgClass = load(className);
			if(bgClass == null){
				// CHANGE THIS TO BE SIMILAR TO ARENAS. CLASSES SHOULD STILL RENDER IF CERTAIN PARTS ARE MISSING (eg missing Icon turns into default icon)
				Messenger.warning("Class '" + className + "' not recognised in classes.yml!");
				continue;
			}
		}
		Messenger.info("Successfully loaded Classes: " + util.stringJoiner(Classes.getNames(), ", "));
	}
	
	public  boolean save(BGClass bgClass){
		try{
			String className = bgClass.getName();
			setHeader(clearCreateSection(className));
			
			ItemStack[] inv 		= bgClass.getInventory().getContents();
			ItemStack[] armor 		= new ItemStack[4];
			armor 					= Arrays.copyOfRange(inv, 0, 4);
			ItemStack offHand 		= inv[4];
			ItemStack[] contents 	= new ItemStack[36];
			contents 				= Arrays.copyOfRange(inv, 9, inv.length);
			
			header.set("Icon", bgClass.getIcon());
			header.set("Armor", armor);
			header.set("OffHand", offHand);
			for(Entry<Integer, ItemStack> entry : YML.playerInventoryContentsFormat(contents).entrySet())
				header.set("Contents." + entry.getKey(), entry.getValue());
			config.save();
			Messenger.debug("info", "Successfully saved Class: " + bgClass.getName() + ".");
			return true;
		} catch (NullPointerException e){
			Messenger.warning("Error saving BGClass: " + bgClass.getName() + ".");
			e.printStackTrace();
			return false;
		}
	}
	public  BGClass load(String className){
		try{
			setHeader(className);
			
			ItemStack icon = header.getItemStack("Icon");
			
			@SuppressWarnings("unchecked")
			List<ItemStack> itemList = (List<ItemStack>) header.get("Armor");
			ItemStack[] armor = (ItemStack[]) itemList.toArray(new ItemStack[0]);
			
			ItemStack offHand = header.getItemStack("OffHand");
			// TO BE IMPLEMENTED!
	//		ItemStack[] effects = new ItemStack[4];
			
			setHeader(createSection(className + ".Contents"));
			
			// recalling item slots + items to be used later
			HashMap<Integer, ItemStack> printedItems = new HashMap<Integer, ItemStack>();
			for(String slotNo : header.getKeys(false))
				printedItems.put(Integer.parseInt(slotNo), header.getItemStack(slotNo));
			// creating a contents of inventory from previous slots + items hashmap
			ItemStack[] contents = YML.getPlayerInventoryContents(printedItems);
			
			ItemStack[] classInventory = new ItemStack[45];
			int i, b;
			// adding armor
			for(i = 0, b = i ; i < armor.length ; i++)
				classInventory[i] = armor[i-b];
			// adding offhand
			classInventory[4] = offHand;
			// adding contents
			for(i = 9, b = i ; i < contents.length + 9; i++)
				classInventory[i] = contents[i-b];
			className = util.formaliseString(className);
			
			if(icon.hasItemMeta() && !icon.getItemMeta().getDisplayName().equalsIgnoreCase(className))
				className = icon.getItemMeta().getDisplayName();

			BGClass newClass = new BGClass(plugin, className, icon, classInventory);
			Messenger.debug("info", "Successfully loaded Class: " + className + ".");
			return newClass;
		} catch (Exception e){
			Messenger.warning("Error loading BGClass: " + className + ".");
			e.printStackTrace();
			return null;
		}
	}
}
