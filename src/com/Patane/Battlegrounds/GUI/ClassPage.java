package com.Patane.Battlegrounds.GUI;

import java.util.Arrays;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.modes.editor.classes.ClassesGUI;


public class ClassPage extends Page{
	BGClass linkedClass;
	public ClassPage(ClassesGUI gui, Page back, BGClass linkedClass) {
		super(gui, linkedClass.getName(), linkedClass.getInventory().getSize(), back);
		this.linkedClass 	= linkedClass;
		this.title 			= Chat.translate("&9" + linkedClass.getName());
		loadFromClass();
	}
	public BGClass getBGClass(){
		return linkedClass;
	}
	@Override
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot) {
		if(topInv){
			gui.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(gui.getPlugin(), new Runnable() {
				public void run() {
					saveToClass();
				}
			}, 1);
		}
		return false;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot) {
		if(topInv){
			if(super.pickupItem(topInv, click, item, slot))
				return true;
			gui.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(gui.getPlugin(), new Runnable() {
				public void run() {
					saveToClass();
				}
			}, 1);
		}
		return false;
	}
	@Override
	public boolean moveItem(boolean topInv, ClickType click, ItemStack item, int slot) {
		return pickupItem(topInv, click, item, slot);
	}
	public void saveToClass(){
		ItemStack[] inv = inventory.getContents().clone();
		
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
