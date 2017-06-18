package com.Patane.Battlegrounds.GUI.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.GUI.GUIutil;
import com.Patane.Battlegrounds.GUI.MainPage;
import com.Patane.Battlegrounds.GUI.Page;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.editor.classes.ClassesGUI;
import com.Patane.Battlegrounds.collections.Classes;
import com.Patane.Battlegrounds.util.util;

public class ClassMainPage extends MainPage{
	
	ClassesGUI classesGui;
	ItemStack allClassesIcon;
	int allClassesSlot;
	
	public ClassMainPage(ClassesGUI gui, String name, int invSize) {
		super(gui, name, invSize);
		this.gui = gui;
		this.classesGui = gui;
		links.put(menuBar[allClassesSlot], new AllClassesPage(classesGui, "&6&lOther classes", 45, this));
	}
	@Override
	public void buildMenuBar(){
		super.buildMenuBar();
		allClassesIcon = GUIutil.stainedPane(1, "&6&lOther Classes");
		this.allClassesSlot = 8;
		menuBar[allClassesSlot] = allClassesIcon;
	}
	// ADDING A CLASS TO THE ARENA + ALL CLASSES LIST
	@Override
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(!item.getItemMeta().hasDisplayName() || !Chat.hasAlpha(ChatColor.stripColor(item.getItemMeta().getDisplayName()))){
				Messenger.send(gui.getPlayer(), "Item must have a non-empty custom name!");
				return true;
			}
			String itemName = item.getItemMeta().getDisplayName();
			if(classesGui.getArena().hasClass(itemName)){
				Messenger.send(classesGui.getPlayer(), "&cThis class already exists within this arena.");
				return true;
			} else if(Classes.contains(itemName)){
				Messenger.send(classesGui.getPlayer(), "&cThis class already exists. Add it from the &6All Classes &cmenu.");
				return true;
			}
			BGClass newClass = Classes.add(new BGClass(classesGui.getPlugin(), itemName, item), true);
			classesGui.getArena().addClass(newClass);
			ClassPage classPage = new ClassPage(classesGui, classesGui.getMainPage(), newClass);
			if(addLink(newClass.getIcon(), classPage)){
				Messenger.send(classesGui.getPlayer(), "&aAdded &7" + ChatColor.stripColor(itemName) + "&a to &7" + classesGui.getArena().getName() + "&a.");
				return false;
			}
		}
		return false;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(super.pickupItem(topInv, click, item, slot))
				return true;
			if(slot == allClassesSlot){
				classesGui.switchPage(links.get(item));
				return true;
				}
			if(isLink(item))
				classesGui.switchPage(links.get(item));
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
			inventory.setItem(slot, null);
			if(links.containsKey(item) && links.get(item) instanceof ClassPage){
				BGClass removingClass = ((ClassPage) links.get(item)).getLinkedClass();
				classesGui.getArena().removeClass(removingClass.getName());
				links.remove(item);
				update();
				allClassesAddAnimation(item);
				Messenger.send(classesGui.getPlayer(), "&cMoved &7" + ChatColor.stripColor(removingClass.getName()) + "&c to &7Other classes&c.");
				return true;
			}
		}
		return placeItem(true, click, item, slot);
	}
	public void allClassesAddAnimation(ItemStack item){
		// changes allClassesSlot to moved item
		inventory.setItem(allClassesSlot, item);
		// changes back to original Icon after 0.5 seconds
		classesGui.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(classesGui.getPlugin(), new Runnable() {
			public void run() {
				inventory.setItem(allClassesSlot, allClassesIcon);
			}
		}, 10);
	}
	@Override
	public boolean addLink(ItemStack icon, Page linkPage){
		return super.addLink(util.hideAttributes(icon), linkPage);
	}
	@Override
	public void update() {
		for(int i = 9 ; i < inventory.getContents().length-1 ; i++){
			if(inventory.getItem(i) != null && inventory.getItem(i).getType() != Material.AIR)
				continue;
			else if(inventory.getItem(i+1) != null && inventory.getItem(i+1).getType() != Material.AIR){
				inventory.setItem(i, inventory.getItem(i+1));
				inventory.setItem(i+1, null);
				continue;
			}
			break;
		}
	}
	@Override
	public void clean() {
		for(int i = menuSize + links.size() ; i < inventory.getSize() ; i++)
			inventory.setItem(i, null);
	}
}
