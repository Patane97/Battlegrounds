package com.Patane.Battlegrounds.GUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.Messenger.ChatType;
import com.Patane.Battlegrounds.listeners.BGListener;

public abstract class GUI {
	protected Plugin plugin;

	protected Inventory inventory;
	protected Player player;
	protected Listener listener;
	boolean resettingInv;
	
	public GUI(Plugin plugin, Player player, Inventory inventory){
		this.plugin = plugin;
		this.player = player;
		this.inventory = inventory;
		setListener(new Listener(plugin));
	}
	public boolean invResetting(){
		return resettingInv;
	}
	public Player getPlayer(){
		return player;
	}
	public Plugin getPlugin(){
		return plugin;
	}
	public Inventory inventory() {
		return inventory;
	}
	public void setListener(Listener listener) {
		try{
			this.listener.unregister();
			Messenger.debug(ChatType.WARNING, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>UNREGISTERING");
		} catch (NullPointerException e) {}
		this.listener = listener;
	}
	public void exit() {
		listener.unregister();
		Messenger.debug(ChatType.WARNING, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>UNREGISTERING");
	}	
	
	public abstract boolean regularClick(Inventory inventory, ClickType click, ItemStack clickedItem, ItemStack cursorItem, int slot);
	
	protected class Listener extends BGListener {
		public Listener(Plugin plugin) {
			super(plugin);
			Messenger.debug(ChatType.WARNING, "REGISTERING<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		}
		@EventHandler
		public void onItemClick(InventoryClickEvent event){
			if(!(event.getInventory().equals(inventory) || event.getView().getTopInventory().equals(inventory)))
				return;
//			Player thisPlayer = (Player) event.getWhoClicked();
			Inventory thisInventory = event.getClickedInventory();
			if(thisInventory == null)
				return;
			ItemStack clickedItem 	= (event.getCurrentItem() == null ? new ItemStack(Material.AIR) : event.getCurrentItem());
			ItemStack cursorItem 	= (event.getCursor() == null ? new ItemStack(Material.AIR) : event.getCursor());
			int slot = event.getRawSlot();
			ClickType click = event.getClick();
			
			event.setCancelled(regularClick(thisInventory, click, clickedItem, cursorItem, slot));
//			Messenger.debug(thisPlayer, "Regular Click [&cInv: &7"+thisInventory.getTitle()+"&r&5 | &cClickType: &7"+click.name()+"&r&5 | &cClicked: &7"+clickedItem.getType().name()+"&r&5 | &cCursor: &7"+clickedItem.getType().name()+"&r&5 | &cSlot: &7"+slot+"&c]");
		}
		@EventHandler
		public void onInventoryClose(InventoryCloseEvent event){
			if(event.getInventory().equals(inventory) && !invResetting())
				exit();
		}
	}
	public interface GUIAction {
		public boolean execute(GUI gui, Page page);
	}
}
