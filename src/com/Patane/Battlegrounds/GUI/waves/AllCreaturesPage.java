package com.Patane.Battlegrounds.GUI.waves;

import com.Patane.Battlegrounds.GUI.Page;
import com.Patane.Battlegrounds.arena.editor.waves.WavesGUI;
import com.Patane.Battlegrounds.arena.game.waves.Wave;
import com.Patane.Battlegrounds.custom.BGCreature;

public class AllCreaturesPage extends Page{
	WavesGUI gui;
	Wave wave;
	public AllCreaturesPage(WavesGUI gui, String name, int invSize, Page back, Wave wave) {
		super(gui, name, invSize, back);
		this.gui = gui;
		this.wave = wave;
	}
	@Override
	protected void initilize() {}
	@Override
	public void update(){
		inventory.clear();
		buildMenuBar();
		for(BGCreature selectedCreature : BGCreature.values()){
			if(!wave.getCreatures().keySet().contains(selectedCreature))
				addIcon(selectedCreature.getSpawnEgg("&7Click to add to wave"));
		}
	}
}
