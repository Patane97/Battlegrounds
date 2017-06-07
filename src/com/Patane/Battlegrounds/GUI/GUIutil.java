package com.Patane.Battlegrounds.GUI;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Patane.Battlegrounds.Chat;

public class GUIutil {
	
	final static String BACK = "&a&lBack";
	final static String BAR = " ";
	static String SAVE_EXIT = "&a&lSave & Exit";

	public static ItemStack stainedPane(int colour, String name){
		ItemStack icon = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) colour);
		ItemMeta iconMeta = icon.getItemMeta();
		iconMeta.setDisplayName(Chat.translate(name));
		icon.setItemMeta(iconMeta);
		return icon;
	}
}
