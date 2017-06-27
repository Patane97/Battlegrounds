package com.Patane.Battlegrounds.GUI;

import java.util.List;
import java.util.Map;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.inventory.ItemStack;

public class MainPage extends Page{
	public MainPage(GUI gui, String name, int invSize) {
		super(gui, name, invSize);
	}
	@Override
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv)
			return true;
		return false;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(isMenu(slot)){
				
				if(item.equals(backIcon)){
					gui.getPlayer().closeInventory();
					gui.exit();
				}
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean dragItem(boolean topInv, DragType drag, Map<Integer, ItemStack> newItems, ItemStack oldItem, List<Integer> slots) {
		if(topInv)
			return true;
		return false;
	}
	
	@Override
	public void buildMenuBar() {
		super.buildMenuBar();
	}
	@Override
	protected void createBackIcon() {
		backIcon = GUIutil.stainedPane((short) 14, GUIutil.SAVE_EXIT);
	}
	
}
