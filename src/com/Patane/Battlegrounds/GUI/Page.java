package com.Patane.Battlegrounds.GUI;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.Messenger;

public class Page {
	protected String title;
	protected Inventory inventory;
	protected Page back;
	
	protected ItemStack backIcon;
	protected ItemStack barIcon;
	
	protected HashMap<ItemStack, Page> links;
	protected ItemStack[] menuBar;
	protected int menuSize;
	
	protected GUI gui;

	public Page (GUI gui, String name,  int invSize){
		this(gui, name, invSize, null);
	}
	public Page (GUI gui, String name, int invSize, Page back){
		name = Chat.translate(name);
		this.title 		= name;
		this.gui 		= gui;
		this.menuSize	= 9;
		this.inventory 	= gui.getPlugin().getServer().createInventory(null, invSize+menuSize, name);
		this.back 		= (back == null? this : back);
		this.links = new HashMap<ItemStack, Page>();
		createBackIcon();
		createBarIcon();
		menuBar = new ItemStack[9];
		buildMenuBar();
		printMenuBar();
	}
	public Inventory inventory(){
		return inventory;
	}
	public String title(){
		return title;
	}
	public Page back(){
		return back;
	}
	public int getMenuSize(){
		return menuSize;
	}
	public void setGUI(GUI gui){
		this.gui = gui;
	}
	public ArrayList<Page> getLinks() {
		ArrayList<Page> tempLinks = new ArrayList<Page>();
		tempLinks.addAll(links.values());
		return tempLinks;
	}
	public void buildMenuBar() {
		menuBar[0] = backIcon;
		for(int i = 1 ; i < menuBar.length ; i++)
			menuBar[i] = barIcon;
	}
	public void printMenuBar() {
		int i = 0;
		for(ItemStack menuItem : menuBar){
			inventory.setItem(i, menuItem);
			i++;
		}
	}
	protected void createBackIcon() {
		backIcon = GUIutil.stainedPane(0, GUIutil.BACK);
	}
	protected void createBarIcon() {
		barIcon = GUIutil.stainedPane(15, GUIutil.BAR);
	}
	public boolean addLink(int slot, ItemStack icon, Page linkPage){
		if(alreadyIcon(icon)){
			Messenger.send(gui.getPlayer(), "&cThis item is already an Icon");
			return false;
		}
		links.put(icon, linkPage);
		inventory.setItem(slot, icon);
		gui.update();
		return true;
	}
	public boolean addLink(ItemStack icon, Page linkPage){
		for(int i=0 ; i<=inventory.getSize() ; i++){
			if(inventory.getItem(i) == null){
				return addLink(i, icon, linkPage);
			}
		}
		return false;
	}
	public boolean addIcon(int slot, ItemStack icon){
		if(alreadyIcon(icon)){
			Messenger.send(gui.getPlayer(), "&cThis item is already an Icon");
			return false;
		}
		inventory.setItem(slot, icon);
		gui.update();
		return true;
	}
	public boolean addIcon(ItemStack icon){
		for(int i=0 ; i<=inventory.getSize() ; i++){
			if(inventory.getItem(i) == null){
				return addIcon(i, icon);
			}
		}
		return false;
	}
	// change this to loop through and check if each item is same to 'item'
	protected boolean alreadyIcon(ItemStack item){
		for(ItemStack selectedItem : links.keySet()){
			if(selectedItem.getType() == item.getType() 
					&& item.hasItemMeta() 
					&& selectedItem.getItemMeta().getDisplayName().contains(ChatColor.stripColor(item.getItemMeta().getDisplayName())))
				return true;
		}
		return false;
	}
	protected boolean isMenu(int slot){
		if(slot >= 0 && slot < menuSize)
			return true;
		return false;
	}
	public void clean() {}
	public boolean replaceItem(boolean topInv, ClickType click, ItemStack thisItem, ItemStack thatItem, int slot) {
		return false;
	}
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot) {
		if(isMenu(slot)){
			if(item.equals(backIcon))
				gui.switchPage(back);
			return true;
		}
		return false;
	}
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot) {
		return false;
	}
	public boolean moveItem(boolean topInv, ClickType click, ItemStack item, int slot) {
		return false;
	}
	public void update() {
	}
	public boolean isLink(ItemStack item){
		if(links.keySet().contains(item))
			return true;
		return false;
	}
}
