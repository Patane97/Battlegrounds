package com.Patane.Battlegrounds.GUI;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class MainPage extends Page{

	public MainPage(GUI gui, String name, int invSize) {
		super(gui, name, invSize);
	}
//	@Override
//	public boolean topItemPlaced(ItemStack item, int slot){
//		return true;
//	}
//	@Override
//	public boolean topItemClicked(ItemStack item, int slot){
//		return super.topItemClicked(item, slot);
//	}
	@Override
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv)
			return true;
		return false;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv)
			return super.pickupItem(topInv, click, item, slot);
		return false;
	}
}
