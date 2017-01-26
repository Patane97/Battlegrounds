package com.Patane.Battlegrounds.GUI;

import java.util.Arrays;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.editor.classes.ClassesGUI;


public class ClassPage extends Page{
	BGClass linkedClass;
	public ClassPage(ClassesGUI gui, Page back, BGClass linkedClass) {
		super(gui, GUIenum.translate("&l" + linkedClass.getName()), linkedClass.getInventory().getSize(), back);
		this.linkedClass 	= linkedClass;
		loadFromClass();
	}
	public BGClass getLinkedClass(){
		return linkedClass;
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
