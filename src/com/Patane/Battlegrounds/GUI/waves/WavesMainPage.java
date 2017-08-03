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
import com.Patane.Battlegrounds.util.util;

public class WavesMainPage extends MainPage{
	WavesGUI gui;
	ItemStack single;
	int singleSlot = 3;
	ItemStack recurring;
	int recurringSlot = 4;
	ItemStack boss;
	int bossSlot = 5;
	
	boolean placingSingle;
	boolean placingRecurring;
	boolean placingBoss;
	
	int currentIncrement;
	
	public WavesMainPage(WavesGUI gui, String name, int invSize) {
		super(gui, name, invSize);
		this.gui = gui;
		initilize();
		menuActions.put(singleSlot, new GUIAction(){
			public boolean execute(ChestGUI gui, Page page){
				new ItemNamer.IntegerMod(gui.getPlugin(), gui.getPlayer(), "New Single Wave", page.inventory().getItem(singleSlot).clone(), (input) -> {
					String name = input.getItemMeta().getDisplayName();
					if(Waves.grab(name) != null){
						Messenger.send(gui.getPlayer(), "&cThere is already a wave with this name!");
						return false;
					}
					// NEED A WAY TO RETRIEVE THE INCREMENT (TO ADD TO WAVE WHEN BEING CREATED ON PLACEMENT)
//					currentIncrement = input.getItemMeta().getLore().get(0).match
					gui.getPlayer().openInventory(gui.inventory());
					gui.getPlayer().setItemOnCursor(input);
					placingSingle = true;
					return true;
				}, 1, null, 1, WaveType.SINGLE);
				return true;
			}
		});
		menuActions.put(recurringSlot, new GUIAction(){
			public boolean execute(ChestGUI gui, Page page){
				gui.getPlayer().setItemOnCursor(page.inventory().getItem(recurringSlot));
				return true;
			}
		});
		menuActions.put(bossSlot, new GUIAction(){
			public boolean execute(ChestGUI gui, Page page){
				gui.getPlayer().setItemOnCursor(page.inventory().getItem(bossSlot));
				return true;
			}
		});
	}
	protected void initilize(){
		single = addMenuIcon(singleSlot, util.setItemNameLore(WaveType.SINGLE.getIcon(), "&6Add Single", "&7At round &5x"));
		recurring = addMenuIcon(recurringSlot, util.setItemNameLore(WaveType.RECURRING.getIcon(), "&6Add Recurring", "&7Every &5x &7rounds"));
		boss = addMenuIcon(bossSlot, util.setItemNameLore(WaveType.BOSS.getIcon(), "&6Add Boss", "&7At round &5x"));
		for(Wave wave : gui.getArena().getWaves()){
			WavePage wavePage = new WavePage(gui, this, wave);
			addLink(wave.getIcon(), wavePage);
		}
	}
	@Override
	public boolean addLink(int slot, ItemStack icon, Page linkPage){
		links.put(icon, linkPage);
		inventory.setItem(slot, icon);
		update();
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
			if(placingSingle){
				placingSingle = false;
				Wave newWave = Waves.add(new Wave(item.getItemMeta().getDisplayName(), WaveType.SINGLE, 1, 0, new HashMap<BGCreature, Integer>()));
				// save YML
				gui.getArena().addWave(newWave);
				WavePage wavePage = new WavePage(gui, gui.getMainPage(), newWave);
				if(addLink(newWave.getIcon(), wavePage)){
					Messenger.send(gui.getPlayer(), "&aAdded &7" + ChatColor.stripColor(newWave.getName()) + "&a to &7" + gui.getArena().getName() + "&a.");
				}
				return true;
			}
			return true;
		}
		return false;
	}
	@Override
	public void update(){
		super.update();
		// Rearrange Icons to be in INCREMENT order
	}
}
