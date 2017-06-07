package com.Patane.Battlegrounds.GUI.classes;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.GUI.Page;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.editor.classes.ClassesGUI;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.collections.Classes;
import com.Patane.Battlegrounds.util.util;

import net.md_5.bungee.api.ChatColor;

public class AllClassesPage extends Page{
	ClassesGUI classesGui;
	public AllClassesPage(ClassesGUI classesGui, String name, int invSize, Page back) {
		super(classesGui, name, invSize, back);
		this.classesGui = classesGui;
	}
	@Override
	public void update(){
		inventory.clear();
		buildMenuBar();
		printMenuBar();
		links.clear();
		for(BGClass selectedClass : Classes.get()){
			if(!classesGui.getArena().hasClass(selectedClass.getName()))
				addLink(selectedClass.getIcon(), classesGui.getMainPage());
		}
	}
	@Override
	public boolean replaceItem(boolean topInv, ClickType click, ItemStack thisItem, ItemStack thatItem, int slot) {
		return placeItem(topInv, click, thatItem, slot);
	}
	@Override
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(!item.getItemMeta().hasDisplayName() || !Chat.hasAlpha(ChatColor.stripColor(item.getItemMeta().getDisplayName()))){
				Messenger.send(gui.getPlayer(), "Item must have a non-empty custom name!");
				return true;
			}
			String name = (item.hasItemMeta() 
					? ChatColor.stripColor(item.getItemMeta().getDisplayName()) 
					: "");
			if(Classes.contains(name)){
				Messenger.send(classesGui.getPlayer(), "&cThis class already exists.");
				return true;
			}
			BGClass newClass = Classes.add(new BGClass(classesGui.getPlugin(), name, item), true);
			addLink(newClass.getIcon(), classesGui.getMainPage());
			Messenger.send(classesGui.getPlayer(), "&aAdded &7" + ChatColor.stripColor(name) + "&a to &7all arenas&a.");
			
		}
		return false;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(super.pickupItem(topInv, click, item, slot))
				return true;
			String name = (item.hasItemMeta() 
					? ChatColor.stripColor(item.getItemMeta().getDisplayName()) 
					: "");
			if(isLink(item)){
				BGClass newClass = Classes.grab(name);
				if(newClass != null){
					classesGui.getArena().addClass(newClass);
					classesGui.getMainPage().addLink(item, new ClassPage(classesGui, classesGui.getMainPage(), newClass));
					classesGui.switchPage(links.get(item));
					Messenger.send(classesGui.getPlayer(), "&aAdded &7" + ChatColor.stripColor(name) + "&a to &7" + classesGui.getArena().getName() + "&a.");
				}
			}
			return true;
		}
		return false;
	}
	@Override
	public boolean moveItem(boolean topInv, ClickType click, ItemStack item, int slot) {
		if(topInv){
			// need to have a confirmation message before deleting!!!
			if(links.containsKey(item)){
				String name = (item.hasItemMeta() 
						? ChatColor.stripColor(item.getItemMeta().getDisplayName()) 
						: "");
				Classes.remove(name);
				Arenas.removeClass(name);
				links.remove(item);
				update();
				classesGui.getPlayer().setItemOnCursor(item);
				Messenger.send(classesGui.getPlayer(), "&cRemoved &7" + ChatColor.stripColor(name) + "&c from &7all arenas&c.");
				return true;
			}
		}
		return placeItem(true, click, item, slot);
	}
	@Override
	public boolean addLink(ItemStack icon, Page linkPage){
		return super.addLink(util.hideAttributes(icon), linkPage);
	}
}
