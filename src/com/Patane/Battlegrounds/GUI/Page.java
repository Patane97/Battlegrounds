package com.Patane.Battlegrounds.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.util.util;

public abstract class Page {
	protected String title;
	protected Inventory inventory;
	protected Page back;
	
	protected ItemStack backIcon;
	protected ItemStack barIcon;
	
	protected HashMap<ItemStack, Page> links;
	protected ItemStack[] menuBar;
	protected int menuSize;
	protected int invSize;
	
	protected GUI gui;

	public Page (GUI gui, String name,  int invSize){
		this(gui, name, invSize, null);
	}
	public Page (GUI gui, String name, int invSize, Page back){
		this.title 		= Chat.translate(name);
		this.gui 		= gui;
		this.menuSize	= 9;
		this.invSize	= invSize + menuSize;
		this.inventory 	= gui.getPlugin().getServer().createInventory(null, this.invSize, this.title);
		this.back 		= (back == null? this : back);
		this.links = new HashMap<ItemStack, Page>();
		backIcon = (back == null ? util.createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, GUI.SAVE_EXIT) 
								 : util.createItem(Material.STAINED_GLASS_PANE, 1, (short) 0, GUI.BACK));
		barIcon = util.createItem(Material.STAINED_GLASS_PANE, 1, (short) 15, GUI.BAR);
		menuBar = new ItemStack[9];
		buildMenuBar();
	}
	public Inventory inventory(){
		return inventory;
	}
	public void setTitle(String title){
		this.title = Chat.translate(title);
		ItemStack[] temp = this.inventory.getContents();
		this.inventory = gui.getPlugin().getServer().createInventory(null, invSize, this.title);
		this.inventory.setContents(temp);
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
	public ArrayList<Page> getLinks() {
		ArrayList<Page> tempLinks = new ArrayList<Page>();
		tempLinks.addAll(links.values());
		return tempLinks;
	}
	protected ItemStack addMenuIcon(int slot, ItemStack icon){
		menuBar[slot] = icon;
		inventory.setItem(slot, menuBar[slot]);
		return icon;
	}
	protected ItemStack addMenuLink(int slot, ItemStack icon, Page page){
		addMenuIcon(slot, icon);
		links.put(icon, page);
		return icon;
	}
	protected void buildMenuBar() {
		menuBar[0] = backIcon;
		inventory.setItem(0, menuBar[0]);
		for(int i = 1 ; i < menuBar.length ; i++){
			menuBar[i] = barIcon;
			inventory.setItem(i, menuBar[i]);
		}
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
	public boolean dragItem(boolean topInv, DragType drag, Map<Integer, ItemStack> newItems, ItemStack cursor, List<Integer> slots) {
		return false;
	}
	public boolean isLink(ItemStack item){
		if(links.keySet().contains(item))
			return true;
		return false;
	}
	public void replaceLink(ItemStack thisItem, ItemStack thatItem){
		if(isLink(thisItem)){
			Page page = links.get(thisItem);
			links.remove(thisItem);
			links.put(thatItem, page);
		}
	}
	public boolean replaceIcon(ItemStack thisItem, ItemStack thatItem) {
		int slot = util.getSlot(inventory, thisItem);
		if(!ChatColor.stripColor(thisItem.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(thatItem.getItemMeta().getDisplayName()))
				&& alreadyIcon(thatItem)){
			Messenger.send(gui.getPlayer(), "&cThis item is already an Icon");
			return false;
		}
		inventory.setItem(slot, thatItem);
		return true;
	}
	protected abstract void initilize();
	public void clean(){};
	public void update(){};
}
