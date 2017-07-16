package com.Patane.Battlegrounds.GUI;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class IconNamer {
	protected Plugin plugin;
	
	protected Inventory inventory;
	protected ItemStack icon;
	
	GUIListeners listener;
	
	// change ChestGUI.java to be an abstract which both this and the current ChestGUI (maybe rename to chestChestGUI) extend.
	
	public IconNamer(Plugin plugin, ItemStack icon){
		this(plugin, icon, "&6Icon Namer (" + icon.getItemMeta().getDisplayName() + ")");
	}
	public IconNamer(Plugin plugin, ItemStack icon, String name){
		this.plugin 	= plugin;
		this.icon 		= icon;
		this.inventory 	= plugin.getServer().createInventory(null, InventoryType.ANVIL, name);
	}
}
