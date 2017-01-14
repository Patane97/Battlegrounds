package com.Patane.Battlegrounds.arena.classes;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BGClass {
	String name;
	ItemStack icon;
	Inventory inventory;
	
	public BGClass(Plugin plugin, String name, ItemStack icon){
		this.name = name;
		this.icon = icon;
		inventory = plugin.getServer().createInventory(null, 45, name);
	}
	public BGClass(Plugin plugin, String name, ItemStack icon, ItemStack[] items){
		this.name = name;
		this.icon = icon;
		inventory = plugin.getServer().createInventory(null, 45, name);
		inventory.setContents(items);
	}
	public String getName(){
		return name;
	}
	public Inventory getInventory(){
		return inventory;
	}
	public ItemStack getIcon(){
		return icon;
	}
	public void setContents(ItemStack[] items){
		try{
			inventory.setContents(items);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public ItemStack[] getContents() {
		return inventory.getContents();
	}
	public void setItem(int slot, ItemStack item) {
		inventory.setItem(slot, item);
		
	}
}