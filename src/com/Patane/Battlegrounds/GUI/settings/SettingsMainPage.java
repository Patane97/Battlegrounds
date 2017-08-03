package com.Patane.Battlegrounds.GUI.settings;

import java.util.HashMap;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.GUI.MainPage;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.editor.types.SettingsEditor.SettingsGUI;
import com.Patane.Battlegrounds.arena.settings.Setting;
import com.Patane.Battlegrounds.util.util;

public class SettingsMainPage extends MainPage{

	String[] BOOLEAN 	= {"BOOLEAN","&7 Left/Right click to add/remove 1"};
	String[] INTEGER 	= {"INTEGER","&7 Left/Right click to add/remove 1",
						   "&7 Shift click to add/remove 5"};
	String[] FLOAT 		= {"FLOAT","&7 Left/Right click to add/remove 0.5",
						   "&7 Shift click to add/remove 5"};
	SettingsGUI gui;
	
	HashMap<ItemStack, Setting> settingsLink = new HashMap<ItemStack, Setting>();
	
	public SettingsMainPage(SettingsGUI gui, String name, int invSize) {
		super(gui, name, invSize);
		this.gui = gui;
		initilizeIcons();
	}
	private void initilizeIcons(){
		// Loops through each of the enum values from settings class. 
		// Ensures that we only need to add a new setting in that class
		// for it to be automatically added #nohardcoding
		for(Setting setting : Setting.values())
			createSettingIcon(setting);
	}
	private void createSettingIcon(Setting setting){
		ItemStack icon = util.hideFlags(setting.getItem(Arena.YML().getSetting(gui.getArena().getName(), setting)), ItemFlag.HIDE_ATTRIBUTES);
		settingsLink.put(icon, setting);
		addIcon(icon);
	}
	private void updateSettingItem(Setting setting, Object value, int slot) {
		ItemStack newItem = setting.getItem(value);
		settingsLink.put(newItem, setting);
		addIcon(slot, newItem);
	}

	private boolean toggleBoolean(Setting setting){
		Arena arena = gui.getArena();
		String ymlName = setting.getYmlName();
		boolean current = (boolean) arena.getSetting(ymlName);
		arena.putSetting(ymlName, !current);
		return (boolean) Arena.YML().saveSetting(arena, ymlName);
	}
	private int changeInteger(Setting setting, ClickType click) {
		Arena arena = gui.getArena();
		String ymlName = setting.getYmlName();
		int current = (int) arena.getSetting(ymlName);
		if(click == ClickType.LEFT)
			arena.putSetting(ymlName, current+1);
		if(click == ClickType.RIGHT)
			arena.putSetting(ymlName, current-1);
		if(click == ClickType.SHIFT_LEFT)
			arena.putSetting(ymlName, current+5);
		if(click == ClickType.SHIFT_RIGHT)
			arena.putSetting(ymlName, current-5);
		return (int) Arena.YML().saveSetting(arena, ymlName);
	}
	private float changeFloat(Setting setting, ClickType click) {
		Arena arena = gui.getArena();
		String ymlName = setting.getYmlName();
		float current = (float) arena.getSetting(ymlName);
		if(click == ClickType.LEFT)
			arena.putSetting(ymlName, current+0.5f);
		if(click == ClickType.RIGHT)
			arena.putSetting(ymlName, current-0.5f);
		if(click == ClickType.SHIFT_LEFT)
			arena.putSetting(ymlName, current+1f);
		if(click == ClickType.SHIFT_RIGHT)
			arena.putSetting(ymlName, current-1f);
		return (float) Arena.YML().saveSetting(arena, ymlName);
	}

	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(super.pickupItem(topInv, click, item, slot))
			return true;
		if(topInv){
			if(settingsLink.containsKey(item)){
				Setting setting = settingsLink.get(item);
				settingsLink.remove(item);
				switch(setting.getType()){
					case BOOLEAN: updateSettingItem(setting, toggleBoolean(setting), slot); break;
					case INTEGER: updateSettingItem(setting, changeInteger(setting, click), slot); break;
					case FLOAT: updateSettingItem(setting, changeFloat(setting, click), slot); break;
				}
			}
		}
		return false;
	}
	@Override
	public boolean moveItem(boolean topInv, ClickType click, ItemStack item, int slot) {
		return pickupItem(topInv, click, item, slot);
	}
	@Override
	public boolean replaceItem(boolean topInv, ClickType click, ItemStack thisItem, ItemStack thatItem, int slot) {
		return pickupItem(topInv, click, thatItem, slot);
	}
	

}
