package com.Patane.Battlegrounds.GUI.waves;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.GUI.MainPage;
import com.Patane.Battlegrounds.arena.editor.waves.WavesGUI;
import com.Patane.Battlegrounds.arena.settings.Setting;

public class WavesMainPage extends MainPage{
	WavesGUI gui;
	
	HashMap<ItemStack, Setting> settingsLink = new HashMap<ItemStack, Setting>();
	
	public WavesMainPage(WavesGUI gui, String name, int invSize) {
		super(gui, name, invSize);
		this.gui = gui;
		initilizeIcons();
	}
	private void initilizeIcons(){
		
	}
}
