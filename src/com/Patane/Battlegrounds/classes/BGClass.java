package com.Patane.Battlegrounds.classes;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BGClass {
	ItemStack icon;
	Inventory inventory;
	public BGClass(Inventory inventory){
		this.inventory = inventory;
	}
	public Inventory getInventory() {
		return inventory;
	}
}
