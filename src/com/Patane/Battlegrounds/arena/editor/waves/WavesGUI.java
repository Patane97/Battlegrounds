package com.Patane.Battlegrounds.arena.editor.waves;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.GUI.GUI;
import com.Patane.Battlegrounds.GUI.waves.WavesMainPage;
import com.Patane.Battlegrounds.arena.Arena;

public class WavesGUI extends GUI{
	Arena arena;
	
	WavesEditor wavesEditor;
	
	public WavesGUI(Plugin plugin, Arena arena, String name, Player player, WavesEditor wavesEditor) {
		super(plugin, name, player);
		this.arena = arena;
		setMainPage(new WavesMainPage(this, name, 27));
		this.wavesEditor = wavesEditor;
	}
	public Arena getArena(){
		return arena;
	}
	@Override
	public void exit(){
		super.exit();
		arena.getMode().sessionOver();
	}

}
