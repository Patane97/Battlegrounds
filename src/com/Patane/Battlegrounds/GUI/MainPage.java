package com.Patane.Battlegrounds.GUI;

import java.util.List;
import java.util.Map;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.GUI.ChestGUI.GUIAction;

public class MainPage extends Page{
	public MainPage(ChestGUI gui, String name, int invSize) {
		super(gui, name, invSize);
		menuActions.put(0, new GUIAction(){
			public boolean execute(ChestGUI gui, Page page){
				gui.getPlayer().closeInventory();
				return true;
			}
		});
	}
	@Override
	protected void initilize() {}
	@Override
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv)
			return true;
		return false;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot){
		return pickupItem(topInv, click, item, slot);
	}
	@Override
	public boolean replaceItem(boolean topInv, ClickType click, ItemStack thisItem, ItemStack thatItem, int slot){
		return pickupItem(topInv, click, thisItem, slot);
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
}
