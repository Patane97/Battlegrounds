package com.Patane.Battlegrounds.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Chat;

public class AnvilGUI extends GUI{	
	public AnvilGUI(Plugin plugin, Player player, String name) {
		super(plugin, player, plugin.getServer().createInventory(null, InventoryType.ANVIL, Chat.translate(name)));
	}

	@Override
	public boolean regularClick(Inventory inventory, ClickType click, ItemStack clickedItem, ItemStack cursorItem, int slot) {
		return false;
	}
	public static class Slot {
        public static final int INPUT_LEFT = 0;
        public static final int INPUT_RIGHT = 1;
        public static final int OUTPUT = 2;
    }
}
