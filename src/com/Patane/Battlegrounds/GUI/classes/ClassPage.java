package com.Patane.Battlegrounds.GUI.classes;

import java.util.Arrays;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.GUI.Page;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.editor.classes.ClassesGUI;
import com.Patane.Battlegrounds.collections.Classes;
import com.Patane.Battlegrounds.util.util;

import net.md_5.bungee.api.ChatColor;


public class ClassPage extends Page{
	ClassesGUI gui;
	BGClass linkedClass;
	int iconSlot = 4;
	public ClassPage(ClassesGUI gui, Page back, BGClass linkedClass) {
		super(gui, Chat.translate("&l" + linkedClass.getName()), linkedClass.getInventory().getSize(), back);
		this.gui = gui;
		this.linkedClass = linkedClass;
		addMenuIcon(iconSlot, linkedClass.getIcon());
		loadFromClass();
	}
	public BGClass getLinkedClass(){
		return linkedClass;
	}
	@Override
	public boolean replaceItem(boolean topInv, ClickType click, ItemStack thisItem, ItemStack thatItem, int slot) {
		// When user wants to change a classes Icon.
		if(topInv && slot == iconSlot){
			String thatItemName = ChatColor.stripColor(thatItem.getItemMeta().getDisplayName());
			if(!linkedClass.getName().equalsIgnoreCase(thatItemName) && gui.checkClassExisting(thatItemName))
				return true;
			thatItem = util.hideAttributes(thatItem);
			BGClass temp = linkedClass;
			linkedClass.setIcon(thatItem);
			linkedClass.setName(thatItemName);
			gui.getArena().replaceClass(temp, linkedClass);
			Classes.replace(temp, linkedClass);
			// MAKE A WAY TO REPLACE ITEM IN ALL ITEMFRAMES IN ALL LOBBIES
			addMenuIcon(iconSlot, linkedClass.getIcon());
			setTitle("&l" + thatItemName);
			gui.getMainPage().replaceIcon(thisItem, thatItem);
			gui.getMainPage().replaceLink(thisItem, thatItem);
			gui.refresh();
			gui.getPlayer().setItemOnCursor(thisItem);
			return false;
		}
		return true;
	}
	@Override
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot) {
		if(topInv){
			saveToClass();
		}
		return false;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot) {
		if(topInv){
			if(slot == iconSlot && click == ClickType.MIDDLE)
				return false;
			if(super.pickupItem(topInv, click, item, slot)){
				saveToClass();
				return true;
			}
			saveToClass();
		}
		return false;
	}
	@Override
	public boolean moveItem(boolean topInv, ClickType click, ItemStack item, int slot) {
		return pickupItem(topInv, click, item, slot);
	}
	public void saveToClass(){
		ItemStack icon = inventory.getItem(iconSlot);
		String iconName = ChatColor.stripColor(icon.getItemMeta().getDisplayName());
		linkedClass.setIcon(icon);
		linkedClass.setName(iconName);
		ItemStack[] inv = inventory.getContents();
		Messenger.info("Saving " + linkedClass.getName() + " to class.");
		ItemStack[] convertedInv = new ItemStack[45];
		convertedInv = Arrays.copyOfRange(inv, menuSize, inv.length);
		linkedClass.setContents(convertedInv);
	}
	public void loadFromClass(){
		int count = menuSize;
		for(ItemStack selectedItem : linkedClass.getInventory().getContents()){
			inventory.setItem(count, selectedItem);
			count++;
		}
	}
}
