package com.Patane.Battlegrounds.GUI;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.modes.editor.classes.ClassesGUI;

import net.md_5.bungee.api.ChatColor;

public class ClassMainPage extends MainPage{
	
	ClassesGUI classesGui;
	
	public ClassMainPage(ClassesGUI gui, String name, int invSize) {
		super(gui, name, invSize);
		classesGui = gui;
	}
	@Override
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(!item.getItemMeta().hasDisplayName() || !Chat.hasAlpha(ChatColor.stripColor(item.getItemMeta().getDisplayName()))){
				Messenger.send(gui.getPlayer(), "Item must have a non-empty custom name!");
				return true;
			}
			String itemName = item.getItemMeta().getDisplayName();
			BGClass newClass = classesGui.getArena().newClass(item,  itemName);
			ClassPage classPage = new ClassPage(classesGui, classesGui.getMainPage(), newClass);
			return !addLink(newClass.getIcon(), classPage);
		}
		return false;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(super.pickupItem(topInv, click, item, slot))
				return true;
			String name = (item.hasItemMeta() ? item.getItemMeta().getDisplayName() : "");
			for(ItemStack selectedIcon : links.keySet()){
				if(selectedIcon.hasItemMeta()
						&& selectedIcon.getType() == item.getType()
						&& selectedIcon.getItemMeta().getDisplayName().equals(name)){
					classesGui.switchPage(links.get(selectedIcon));
					break;
				}
			}
			return true;
		}
		return false;
	}
	@Override
	public boolean replaceItem(boolean topInv, ClickType click, ItemStack thisItem, ItemStack thatItem, int slot) {
		return placeItem(topInv, click, thatItem, slot);
	}
	@Override
	public boolean moveItem(boolean topInv, ClickType click, ItemStack item, int slot) {
		if(topInv){
			//inventory.setItem(slot, null);
			if(links.containsKey(item) && links.get(item) instanceof ClassPage){
				BGClass removingClass = ((ClassPage) links.get(item)).getBGClass();
				classesGui.getArena().removeClass(removingClass);
				Messenger.send(classesGui.getPlayer(), "&aClass '&7" + removingClass.getName() + "'&a successfully removed!");
				return false;
			}
		}
		return placeItem(true, click, item, slot);
	}
	@Override
	public void clean() {
		for(int i = menuSize + links.size() ; i < inventory.getSize() ; i++)
			inventory.setItem(i, null);
	}
}
