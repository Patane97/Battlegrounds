package com.Patane.Battlegrounds.GUI;

import java.util.List;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.Messenger;
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
		//maybe add below to end of items lore (at bottom of lore)
//		"&3Inrement Value ["+value+"]", "&7Left/Right click to add/remove 1"
		this.function = function;
//		player.setItemOnCursor(item);
		inventory.setItem(Slot.INPUT_LEFT, item);
		// FIX ERROR WITH PREPAREANVILEVENT AND MOVE BELOW LINE INTO ANVILGUI
		this.listener = new AnvilListener(plugin);
	}

	@Override
	public ItemStack prepareAnvil(AnvilInventory inventory, ItemStack result) {
		if(result == null || result.getType() == Material.AIR)
			return null;
		return util.hideFlags(util.setItemNameLore(result, Chat.translate("&r&6"+result.getItemMeta().getDisplayName()), returnLore.toArray(new String[returnLore.size()])), ItemFlag.HIDE_ENCHANTS);
		
	}
	@Override
	public boolean regularClick(AnvilInventory inventory, ClickType click, ItemStack clickedItem, ItemStack cursorItem, int slot) {
		String text = inventory.getRenameText();
		if(slot == Slot.OUTPUT && inventory.getItem(Slot.OUTPUT) != null && !text.isEmpty()){
			if(!function.apply(clickedItem))
				return true;
			exit();
		}
		return false;
	}

	public static class IntegerMod extends ItemNamer{
		int value;
		Integer max;
		Integer min;
		WaveType waveType;
		String bookName;
		
		public IntegerMod(Plugin plugin, Player player, String name, ItemStack item, Function<ItemStack, Boolean> function, int value, Integer max, Integer min, WaveType waveType) {
			super(plugin, player, name, util.setItemNameLore(item, name, "&7Click on the Output Item to Confirm!"), function);
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
			if(this.inventory.equals(inventory)){
				if(slot == Slot.INPUT_RIGHT){
					if(click == ClickType.LEFT) value ++;
					else if(click == ClickType.RIGHT) value --;
					else if(click == ClickType.SHIFT_LEFT) value += 5;
					else if(click == ClickType.SHIFT_RIGHT) value -= 5;
					if(min != null && value < min) value = min;
					if(max != null && value > max) value = max;
					Messenger.debug(player, ""+value);
					returnLore.clear();
					returnLore.add(waveType.getDesc(value));
					inventory.setItem(Slot.INPUT_RIGHT, util.setItemNameLore(clickedItem, bookName()));
					
					return true;
				}
				return true;
			}
			return false;
		}
		public String bookName(){
			return bookName = "&3Increment ("+value+")";
		}
	}
	
	
	
}
