package com.Patane.Battlegrounds.GUI.classes;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.GUI.ChestGUI;
import com.Patane.Battlegrounds.GUI.ChestGUI.GUIAction;
import com.Patane.Battlegrounds.GUI.MainPage;
import com.Patane.Battlegrounds.GUI.Page;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.editor.types.ClassEditor.ClassesGUI;
import com.Patane.Battlegrounds.collections.Classes;
import com.Patane.Battlegrounds.util.util;

public class ClassMainPage extends MainPage{
	
	ClassesGUI gui;
	ItemStack allClassesIcon;
	int allClassesSlot = 8;
	
	public ClassMainPage(ClassesGUI gui, String name, int invSize) {
		super(gui, name, invSize);
		this.gui = gui;
		initilize();
		menuActions.put(allClassesSlot, new GUIAction(){
			public boolean execute(ChestGUI gui, Page page){
				gui.switchPage(links.get(allClassesIcon));
				return true;
			}
		});
	}
	@Override
	public void initilize(){
		allClassesIcon = addMenuLink(allClassesSlot, util.createItem(Material.STAINED_GLASS_PANE, 1, (short) 1, "&6&lOther Classes"), 
				new AllClassesPage(gui, "&6&lOther classes", 45, this));
		for(String selectedClass : gui.getArena().getClasses()){
			BGClass bgClass = Classes.grab(selectedClass);
			if(bgClass == null)
				break;
			ClassPage classPage = new ClassPage(gui, this, bgClass);
			addLink(bgClass.getIcon(), classPage);
		}
	}
	@Override
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(!item.getItemMeta().hasDisplayName() || !Chat.hasAlpha(ChatColor.stripColor(item.getItemMeta().getDisplayName()))){
				Messenger.send(gui.getPlayer(), "&cItem must have a non-empty custom name!");
				return true;
			}
			String itemName = item.getItemMeta().getDisplayName();
			if(gui.checkClassExisting(itemName))
				return true;
			BGClass newClass = Classes.add(new BGClass(gui.getPlugin(), itemName, item));
			BGClass.YML().save(newClass);
			gui.getArena().addClass(newClass);
			ClassPage classPage = new ClassPage(gui, gui.getMainPage(), newClass);
			if(addLink(newClass.getIcon(), classPage)){
				Messenger.send(gui.getPlayer(), "&aAdded &7" + ChatColor.stripColor(itemName) + "&a to &7" + gui.getArena().getName() + "&a.");
				return false;
			}
		}
		return false;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(isLink(item))
				gui.switchPage(links.get(item));
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
				gui.getArena().removeClass(removingClass.getName());
				links.remove(item);
				update();
				allClassesAddAnimation(item);
				Messenger.send(gui.getPlayer(), "&cMoved &7" + ChatColor.stripColor(removingClass.getName()) + "&c to &7Other classes&c.");
				return true;
			}
		}
		return placeItem(true, click, item, slot);
	}
	@Override
	public boolean dragItem(boolean topInv, DragType drag, Map<Integer, ItemStack> newItems, ItemStack oldItem, List<Integer> slots) {
		if(topInv){
			if(drag == DragType.EVEN)
				return placeItem(topInv, ClickType.LEFT, oldItem, slots.get(0));
			return placeItem(topInv, ClickType.RIGHT, oldItem, slots.get(0));
		}
		return false;
	}
	public void allClassesAddAnimation(ItemStack item){
		// changes allClassesSlot to moved item
		inventory.setItem(allClassesSlot, item);
		// changes back to original Icon after 0.5 seconds
		gui.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(gui.getPlugin(), new Runnable() {
			public void run() {
				inventory.setItem(allClassesSlot, allClassesIcon);
			}
		}, 10);
	}
	@Override
	public boolean addLink(ItemStack icon, Page linkPage){
		return super.addLink(util.hideFlags(icon, ItemFlag.HIDE_ATTRIBUTES), linkPage);
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
//		for(int i = menuSize + links.size() ; i < inventory.getSize() ; i++)
//			inventory.setItem(i, null);
	}
}
