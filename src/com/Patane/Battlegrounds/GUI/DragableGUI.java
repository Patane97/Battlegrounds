package com.Patane.Battlegrounds.GUI;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;

public abstract class DragableGUI extends GUI{	
	
	public DragableGUI(Plugin plugin, Player player, Inventory inventory) {
		super(plugin, player, inventory);
	}
	@Override
	protected void createListener() {
		this.listener = new DragableListener(plugin);
	}
	public abstract boolean dragClick(boolean topInv, DragType drag, Map<Integer, ItemStack> newItems, ItemStack cursor, ArrayList<Integer> slots);
	
	protected class DragableListener extends Listener {
		public DragableListener(Plugin plugin) {
			super(plugin);
		}
		@EventHandler
		public void onItemDrag(InventoryDragEvent event){
			Player thisPlayer = (Player) event.getWhoClicked();
			Inventory thisTopInventory = event.getView().getTopInventory();
			if(!event.getInventory().equals(inventory))
				return;
			boolean topInv = false;
			// Determining if any slots are in top inventory since we cant get "clickedInventory".
			if(thisTopInventory != null){
				for(int slot : event.getRawSlots()){
					if(slot < thisTopInventory.getSize()){
						topInv = true;
						break;
					}
				}
			}
			DragType drag = event.getType();
			/*
			 * Run DRAG-CLICK function for GUI.
			 */
			event.setCancelled(dragClick(topInv, drag, event.getNewItems(), event.getOldCursor(), new ArrayList<Integer>(event.getInventorySlots())));
			Messenger.debug(thisPlayer, "Drag Click ["+topInv+" | "+drag.name()+"]");
		}
	}
}
