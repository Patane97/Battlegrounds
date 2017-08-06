package com.Patane.Battlegrounds.GUI.waves;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.GUI.ChestGUI;
import com.Patane.Battlegrounds.GUI.ChestGUI.GUIAction;
import com.Patane.Battlegrounds.GUI.ItemNamer;
import com.Patane.Battlegrounds.GUI.MainPage;
import com.Patane.Battlegrounds.GUI.Page;
import com.Patane.Battlegrounds.arena.editor.types.WavesEditor.WavesGUI;
import com.Patane.Battlegrounds.arena.game.waves.Wave;
import com.Patane.Battlegrounds.arena.game.waves.WaveType;
import com.Patane.Battlegrounds.collections.Waves;
import com.Patane.Battlegrounds.custom.BGCreature;

public class WavesMainPage extends MainPage{
	WavesGUI gui;
	WaveType currentWaveType = null;
	int currentIncrement;
	
	public WavesMainPage(WavesGUI gui, String name, int invSize) {
		super(gui, name, invSize);
		this.gui = gui;
		initilize();
	}
	protected void initilize(){
		for(WaveType waveType : WaveType.values()){
			addMenuIcon(waveType.getSlot(), waveType.getAddingIcon());
			menuActions.put(waveType.getSlot(), getAddWaveAction(waveType));
		}
		for(Wave wave : gui.getArena().getWaves()){
			WavePage wavePage = new WavePage(gui, this, wave);
			addLink(wave.getIncIcon(), wavePage);
		}
		refreshWaves();
	}
	private void refreshWaves() {
		for(int i=menuSize ; i < inventory.getSize() ; i++){
			try{
				Wave tempWave = gui.getArena().getWaves().get(i-menuSize);
				inventory.setItem(i, tempWave.getIncIcon());
			} catch(IndexOutOfBoundsException e){
				inventory.setItem(i, null);
			}
		}
	}
	@Override
	public boolean addLink(int slot, ItemStack icon, Page linkPage){
		links.put(icon, linkPage);
		return true;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(isLink(item))
				gui.switchPage(links.get(item));
			return true;
		}
		return false;
	}
	@Override
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(currentWaveType != null){
				gui.getPlayer().setItemOnCursor(null);
				Wave newWave = new Wave(item.getItemMeta().getDisplayName(), currentWaveType, currentIncrement, 0, new HashMap<BGCreature, Integer>());
				currentIncrement = 1;
				currentWaveType = null;
				// save YML
				gui.getArena().addWave(newWave);
				update();
				WavePage wavePage = new WavePage(gui, gui.getMainPage(), newWave);
				addLink(newWave.getIncIcon(), wavePage);
				Messenger.send(gui.getPlayer(), "&aAdded &7" + ChatColor.stripColor(newWave.getName()) + "&a to &7" + gui.getArena().getName() + "&a.");
				return true;
			}
			return true;
		}
		return false;
	}
	@Override
	public void update(){
//		super.update();
		refreshWaves();
		
	}
	private GUIAction getAddWaveAction(WaveType waveType){
		return new GUIAction(){
			public boolean execute(ChestGUI gui, Page page){
				new ItemNamer.WaveMod(gui.getPlugin(), gui.getPlayer(), "New "+waveType.getName()+" Wave", page.inventory().getItem(waveType.getSlot()).clone(), (item, increment) -> {
					if(item == null){
						gui.getPlayer().openInventory(gui.inventory());
						gui.getPlayer().setItemOnCursor(item);
						return false;
					}
					String name = item.getItemMeta().getDisplayName();
					if(Waves.grab(name) != null){
						Messenger.send(gui.getPlayer(), "&cThere is already a wave with this name!");
						return false;
					}
					currentIncrement = increment;
					currentWaveType = waveType;
					gui.getPlayer().openInventory(gui.inventory());
					gui.getPlayer().setItemOnCursor(item);
					return true;
				}, 1, null, 1, waveType);
				return true;
			}
		};
	}
}
