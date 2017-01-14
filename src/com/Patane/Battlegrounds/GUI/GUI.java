package com.Patane.Battlegrounds.GUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class GUI {
	protected Plugin plugin;
	
	protected MainPage mainPage;
	protected Page currentPage;
	
	protected Inventory gui;
	
	protected Player player;
	
	GUIListeners listener;
	
	boolean resettingInv;
	
	public GUI(Plugin plugin, String name, Player player){		
		this.plugin 		= plugin;
		this.player 		= player;
		this.gui 			= plugin.getServer().createInventory(null, 54, name);
		this.mainPage 		= new MainPage(this, name, 36);
		this.currentPage 	= mainPage;
		this.currentPage.printMenuBar();
		
		player.openInventory(gui);
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
		return gui;
	}
	public Page getCurrentPage(){
		return currentPage;
	}
	protected void setMainPage(MainPage mainPage) {
		if(this.mainPage.equals(currentPage))
			switchPage(mainPage);
		this.mainPage = mainPage;
	}
	protected void switchPage(Page page) {
		currentPage.clean();
		currentPage = page;
		gui = currentPage.inventory();
		update();
	}
	public void update() {
		resettingInv = true;
		player.setItemOnCursor(null);
		player.openInventory(gui);
		resettingInv = false;
	}
	public void exit() {
		listener.unregister();
	}
	public boolean regularClick(boolean topInv, ClickType click, ItemStack clickedItem, ItemStack cursorItem, int slot) {
		if(clickedItem.getType() != Material.AIR && cursorItem.getType() != Material.AIR)
			return currentPage.replaceItem(topInv, click, clickedItem, cursorItem, slot);
		if(clickedItem.getType() != Material.AIR)
			return currentPage.pickupItem(topInv, click, clickedItem, slot);
		if(cursorItem.getType() != Material.AIR)
			return currentPage.placeItem(topInv, click, cursorItem, slot);
		return false;
	}
	public boolean shiftClick(boolean topInv, ClickType click, ItemStack clickedItem, int slot) {
		return currentPage.moveItem(topInv, click, clickedItem, slot);
	}
}
