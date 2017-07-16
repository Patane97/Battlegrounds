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

public abstract class ChestGUI {
	protected Plugin plugin;
	
	protected MainPage mainPage;
	protected Page currentPage;
	
	protected Inventory inventory;
	
	protected Player player;
	
	GUIListeners listener;
	
	boolean resettingInv;
	
	final static String BACK = "&a&lBack";
	final static String BAR = " ";
	final static String SAVE_EXIT = "&a&lSave & Exit";
	
	public ChestGUI(Plugin plugin, String name, Player player){	
		name = Chat.translate(name);
		this.plugin 		= plugin;
		this.player 		= player;
		this.inventory 			= plugin.getServer().createInventory(null, 54, name);
		this.mainPage 		= new MainPage(this, name, 36);
		this.currentPage 	= mainPage;
		
		player.openInventory(inventory);
		listener = new GUIListeners(plugin, this);
	}
	public boolean invResetting(){
		return resettingInv;
	}
	public Player getPlayer(){
		return player;
	}
	public Plugin getPlugin(){
		return plugin;
	}
	public MainPage getMainPage(){
		return mainPage;
	}
	public Inventory inventory() {
		return inventory;
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
		currentPage.clean();
		currentPage = page;
		currentPage.update();
		inventory = currentPage.inventory();
		update();
	}
	public void update() {
		resettingInv = true;
		player.setItemOnCursor(null);
		player.openInventory(inventory);
		resettingInv = false;
	}
	public void refresh() {
		currentPage.clean();
		currentPage.update();
		inventory = currentPage.inventory();
		update();
	}
	public void exit() {
		listener.unregister();
	}
	public boolean regularClick(boolean topInv, ClickType click, ItemStack clickedItem, ItemStack cursorItem, int slot) {
		if(currentPage.isMenu(slot))
			return currentPage.menuActions((cursorItem.getType() != Material.AIR ? cursorItem : clickedItem), slot, click);
		if(clickedItem.getType() != Material.AIR && cursorItem.getType() != Material.AIR)
			return currentPage.replaceItem(topInv, click, clickedItem, cursorItem, slot);
		if(clickedItem.getType() != Material.AIR)
			return currentPage.pickupItem(topInv, click, clickedItem, slot);
		if(cursorItem.getType() != Material.AIR)
			return currentPage.placeItem(topInv, click, cursorItem, slot);
		return false;
	}
	public boolean shiftClick(boolean topInv, ClickType click, ItemStack clickedItem, int slot) {
		if(currentPage.isMenu(slot))
			return currentPage.menuActions(clickedItem, slot, click);
		return currentPage.moveItem(topInv, click, clickedItem, slot);
	}
	public boolean dragClick(boolean topInv, DragType drag, Map<Integer, ItemStack> newItems, ItemStack cursor, ArrayList<Integer> slots) {
		for(int slot : slots)
			if(currentPage.isMenu(slot))
				return true;
		return currentPage.dragItem(topInv, drag, newItems, cursor, slots);
	}
}
