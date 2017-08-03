package com.Patane.Battlegrounds.arena.classes;

import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.collections.Classes;
import com.Patane.Battlegrounds.util.util;

public class BGClass {
	/**
	 * ******************* STATIC YML SECTION *******************
	 */
	private static BGClassYML yml;

	public static void setYML(BGClassYML yml){
		BGClass.yml = yml;
	}
	public static BGClassYML YML(){
		return BGClass.yml;
	}
	/**
	 * **********************************************************
	 */
	private String name;
	private ItemStack icon;
	private Inventory inventory;
	
	public BGClass(Plugin plugin, String name, ItemStack icon){
		this.name = ChatColor.stripColor(name);
		setIcon(icon);
		inventory = plugin.getServer().createInventory(null, 45, name);
		Classes.add(this);
	}
	public BGClass(Plugin plugin, String name, ItemStack icon, ItemStack[] items){
		this(plugin, name, icon);
		inventory.setContents(items);
	}
	public String getName(){
		return ChatColor.stripColor(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public Inventory getInventory(){
		return inventory;
	}
	public ItemStack getIcon(){
		return icon;
	}
	public void setIcon(ItemStack icon) {
		this.icon = util.hideFlags(icon, ItemFlag.HIDE_ATTRIBUTES);
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
