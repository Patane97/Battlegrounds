package com.Patane.Battlegrounds.GUI;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.arena.game.waves.WaveType;
import com.Patane.Battlegrounds.util.util;

public class ItemNamer extends AnvilGUI{
	ItemStack item;
	List<String> returnLore;
	Function<ItemStack, Boolean> function;
	
	public ItemNamer(Plugin plugin, Player player, String name, ItemStack item, Function<ItemStack, Boolean> function) {
		super(plugin, player, name);
		returnLore = item.getItemMeta().getLore();
		this.item = item;
		this.function = function;
		listener.unregister();
		inventory.setItem(Slot.INPUT_LEFT, item);
		setListener(new ItemNamerListener(plugin));
	}

	@Override
	public ItemStack prepareAnvil(AnvilInventory inventory, ItemStack result) {
		if(result == null || result.getType() == Material.AIR)
			return null;
		return util.hideFlags(util.setItemNameLore(result, Chat.translate("&r&6"+result.getItemMeta().getDisplayName()), returnLore.toArray(new String[returnLore.size()])), ItemFlag.HIDE_ENCHANTS);
		
	}
	@Override
	public boolean regularClick(AnvilInventory inventory, ClickType click, ItemStack clickedItem, ItemStack cursorItem, int slot) {
		if(slot == Slot.INPUT_LEFT){
			doFunction(null);
			exit();
		}
		if(slot == Slot.OUTPUT && inventory.getItem(Slot.OUTPUT) != null){
			if(inventory.getRenameText().isEmpty())
				return true;
			ItemStack left = inventory.getItem(0);
			ItemStack right = inventory.getItem(1);
			inventory.setItem(0, new ItemStack(Material.AIR));
			inventory.setItem(1, new ItemStack(Material.AIR));
			if(!doFunction(clickedItem)){
				inventory.setItem(0, left);
				inventory.setItem(1, right);
				return true;
			}
			exit();
		}
		return false;
	}
	protected boolean doFunction(ItemStack item){
		return function.apply(item);
	}
	protected class ItemNamerListener extends AnvilListener {
		public ItemNamerListener(Plugin plugin) {
			super(plugin);
		}
		@Override
		@EventHandler
		public void onInventoryClose(InventoryCloseEvent event){
			if(event.getInventory().equals(inventory)){
				inventory.setItem(0, new ItemStack(Material.AIR));
				inventory.setItem(1, new ItemStack(Material.AIR));
				exit();
			}
		}
	}
	public static class WaveMod extends ItemNamer{
		int value;
		Integer max;
		Integer min;
		WaveType waveType;
		String bookName;
		BiFunction<ItemStack, Integer, Boolean> function;
		
		public WaveMod(Plugin plugin, Player player, String name, ItemStack item, BiFunction<ItemStack, Integer, Boolean> function, int value, Integer max, Integer min, WaveType waveType) {
			super(plugin, player, name, util.setItemNameLore(item, name, "&7Click Here to Cancel.", "&7Click Output to Confirm."), null);
			this.function = function;
			this.value = value;
			this.max = max;
			this.min = min;
			if(this.max != null && this.min != null && max < min) this.max = this.min;
			this.waveType = waveType;
			returnLore.clear();
			returnLore.add(waveType.getDesc(value));
			ItemStack incrementItem = util.createEnchantBook(Enchantment.DURABILITY, 1, true, bookName(), "&7Left/Right click to add/remove 1");
			incrementItem = util.hideFlags(incrementItem, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
			inventory.setItem(Slot.INPUT_RIGHT, incrementItem);
		}
		@Override
		public boolean regularClick(AnvilInventory inventory, ClickType click, ItemStack clickedItem, ItemStack cursorItem, int slot) {
			if(super.regularClick(inventory, click, clickedItem, cursorItem, slot))
				return true;
			if(slot == Slot.INPUT_RIGHT){
				if(click == ClickType.LEFT) value ++;
				else if(click == ClickType.RIGHT) value --;
				else if(click == ClickType.SHIFT_LEFT) value += 5;
				else if(click == ClickType.SHIFT_RIGHT) value -= 5;
				if(min != null && value < min) value = min;
				if(max != null && value > max) value = max;
				returnLore.clear();
				returnLore.add(waveType.getDesc(value));
				inventory.setItem(Slot.INPUT_RIGHT, util.setItemNameLore(clickedItem, bookName()));
				
				return true;
			}
			return false;
		}
		@Override
		protected boolean doFunction(ItemStack item){
			return function.apply(item, value);
		}
		public String bookName(){
			return bookName = "&3Increment ("+value+")";
		}
	}
	
	
	
}
