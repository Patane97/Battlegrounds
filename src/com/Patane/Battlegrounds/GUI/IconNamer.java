package com.Patane.Battlegrounds.GUI;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.GUI.AnvilGUIFix.AnvilClickEvent;

public class IconNamer extends AnvilGUI{
//	private final Function<String, Boolean> function;
	AnvilGUIFix gui;
	
	public IconNamer(Plugin plugin, Player player, String name, ItemStack item, Function<String, Boolean> function) {
		super(plugin, player, (name == null ? "&7Renaming "+item.getItemMeta().getDisplayName() : name));
//		this.function = function;
		this.gui = new AnvilGUIFix(player, new AnvilGUIFix.AnvilClickEventHandler() {
			
			@Override
			public void onAnvilClick(AnvilClickEvent event) {
				if(event.getSlot() == AnvilGUIFix.AnvilSlot.OUTPUT){
					event.setWillClose(true);
					event.setWillDestroy(true);
					function.apply(event.getName());
				} else {
					event.setWillClose(false);
					event.setWillDestroy(false);
				}
			}
		});
		gui.setSlot(AnvilGUIFix.AnvilSlot.INPUT_LEFT, item);
		try {
			gui.open();
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
//		inventory.setItem(Slot.INPUT_LEFT, item);
//		
//		player.openInventory(inventory);
	}

}
