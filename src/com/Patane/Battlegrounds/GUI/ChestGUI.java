package com.Patane.Battlegrounds.GUI;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Chat;

public class ChestGUI extends DragableGUI{
	protected MainPage mainPage;
	protected Page currentPage;
	
	final static String BACK = "&a&lBack";
	final static String BAR = " ";
	final static String SAVE_EXIT = "&a&lSave & Exit";
	
	public ChestGUI(Plugin plugin, Player player, String name){
		super(plugin, player, plugin.getServer().createInventory(null, 54, Chat.translate(name)));
		this.mainPage 		= new MainPage(this, name, 36);
		this.currentPage 	= mainPage;
	}
	public MainPage getMainPage(){
		return mainPage;
	}
	public Page getCurrentPage(){
		return currentPage;
	}
	protected void setMainPage(MainPage mainPage) {
		if(this.mainPage.equals(currentPage))
			switchPage(mainPage);
		this.mainPage = mainPage;
	}
	public void switchPage(Page page) {
//		currentPage.clean();
		currentPage = page;
//		currentPage.update();
		inventory = currentPage.inventory();
		update();
	}
	public void update() {
		resettingInv = true;
		player.setItemOnCursor(null);
//		player.openInventory(inventory);
		resettingInv = false;
	}
	public void refresh() {
		currentPage.clean();
		currentPage.update();
		inventory = currentPage.inventory();
		update();
	}
	@Override
	public boolean regularClick(Inventory inventory, ClickType click, ItemStack clickedItem, ItemStack cursorItem, int slot) {
		if(click == ClickType.NUMBER_KEY || click == ClickType.UNKNOWN)
			return true;
		if(currentPage.isMenu(slot))
			return currentPage.menuActions((cursorItem.getType() != Material.AIR ? cursorItem : clickedItem), slot, click);
		boolean topInv = inventory.equals(this.inventory);
		if(clickedItem.getType() != Material.AIR && (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT))
			return currentPage.moveItem(topInv, click, clickedItem, slot);
		if(clickedItem.getType() != Material.AIR && cursorItem.getType() != Material.AIR)
			return currentPage.replaceItem(topInv, click, clickedItem, cursorItem, slot);
		if(clickedItem.getType() != Material.AIR)
			return currentPage.pickupItem(topInv, click, clickedItem, slot);
		if(cursorItem.getType() != Material.AIR)
			return currentPage.placeItem(topInv, click, cursorItem, slot);
		return false;
	}
	@Override
	public boolean dragClick(boolean topInv, DragType drag, Map<Integer, ItemStack> newItems, ItemStack cursor, ArrayList<Integer> slots) {
		for(int slot : slots)
			if(currentPage.isMenu(slot))
				return true;
		return currentPage.dragItem(topInv, drag, newItems, cursor, slots);
	}	
	public interface GUIAction {
		public boolean execute(ChestGUI gui, Page page);
	}
	
}
