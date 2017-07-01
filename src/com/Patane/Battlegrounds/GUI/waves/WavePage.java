package com.Patane.Battlegrounds.GUI.waves;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.GUI.Page;
import com.Patane.Battlegrounds.arena.editor.waves.WavesGUI;
import com.Patane.Battlegrounds.arena.game.waves.Wave;
import com.Patane.Battlegrounds.custom.BGCreature;
import com.Patane.Battlegrounds.util.util;


public class WavePage extends Page{
	WavesGUI gui;
	Wave linkedWave;
	ItemStack allCreaturesIcon;
	int allCreaturesSlot = 8;
	
	int iconSlot = 4;
	public WavePage(WavesGUI gui, Page back, Wave linkedWave) {
		super(gui, Chat.translate("&l" + linkedWave.getName() + " wave"), 45, back);
		this.gui = gui;
		this.linkedWave = linkedWave;
		initilize();
	}
	@Override
	protected void initilize() {
		allCreaturesIcon = addMenuLink(allCreaturesSlot, util.createItem(Material.STAINED_GLASS_PANE, 1, (short) 1, "&6&lUsable Creatures"), 
				new AllCreaturesPage(gui, "&6&lUsable Creatures", 45, this, linkedWave));
		for(BGCreature creature : linkedWave.getCreatures().keySet()){
			addIcon(creature.getSpawnEgg("&6Probability: " + linkedWave.getCreatures().get(creature)));
		}
	}
}
