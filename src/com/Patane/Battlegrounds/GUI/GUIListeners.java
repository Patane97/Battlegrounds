package com.Patane.Battlegrounds.GUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

//import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.listeners.BGListener;

import net.md_5.bungee.api.ChatColor;


public class GUIListeners extends BGListener{
	
	GUI gui;
	Player playerGUI;
	
	public GUIListeners(Plugin plugin, GUI gui){
		super(plugin);
		this.gui = gui;
		this.playerGUI = gui.getPlayer();
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemClick(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getClickedInventory();
		Inventory top = event.getView().getTopInventory();
		if(!player.getDisplayName().equals(playerGUI.getDisplayName())
				|| inv == null
				|| !ChatColor.stripColor(top.getName()).equalsIgnoreCase(gui.inventory().getTitle()))
			return;
		if((event.getCurrentItem() == null && event.getCursor() == null)
				|| (event.getCurrentItem().getType() == Material.AIR && event.getCursor().getType() == Material.AIR))
			return;
		
		ItemStack cursorItem = event.getCursor();
		ItemStack clickedItem = event.getCurrentItem();
		boolean topInv = inv.equals(top);
		int slot = event.getSlot();
		ClickType click = event.getClick();

		if(clickedItem.getType() != Material.AIR && (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)){
//			Messenger.send(player, "Shift Click");
			event.setCancelled(gui.shiftClick(topInv, click, clickedItem, slot));
		} else if((clickedItem.getType() != Material.AIR 
				|| cursorItem.getType() != Material.AIR) 
				&& (click == ClickType.LEFT 
				|| click == ClickType.RIGHT 
				|| click == ClickType.MIDDLE)){
//			Messenger.send(player, "Regular Click");
			event.setCancelled(gui.regularClick(topInv, click, clickedItem, cursorItem, slot));
		}
	}
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event){
		Player player = (Player) event.getPlayer();
		Inventory inv = event.getInventory();
		if(!player.getDisplayName().equals(playerGUI.getDisplayName()) 
				|| !ChatColor.stripColor(inv.getName()).equalsIgnoreCase(gui.inventory().getName())
				|| gui.invResetting())
			return;
		gui.exit();
	}
}