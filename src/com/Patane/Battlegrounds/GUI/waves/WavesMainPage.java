package com.Patane.Battlegrounds.GUI.waves;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.GUI.ChestGUI;
import com.Patane.Battlegrounds.GUI.GUIAction;
import com.Patane.Battlegrounds.GUI.MainPage;
import com.Patane.Battlegrounds.GUI.Page;
import com.Patane.Battlegrounds.arena.editor.waves.WavesChestGUI;
import com.Patane.Battlegrounds.arena.game.waves.Wave;
import com.Patane.Battlegrounds.arena.game.waves.WaveType;
import com.Patane.Battlegrounds.collections.Waves;
import com.Patane.Battlegrounds.custom.BGCreature;
import com.Patane.Battlegrounds.util.util;

public class WavesMainPage extends MainPage{
	WavesChestGUI gui;
	ItemStack single;
	int singleSlot = 3;
	ItemStack recurring;
	int recurringSlot = 4;
	ItemStack boss;
	int bossSlot = 5;
	
	
	public WavesMainPage(WavesChestGUI gui, String name, int invSize) {
		super(gui, name, invSize);
		this.gui = gui;
		initilize();
		menuActions.put(singleSlot, new GUIAction(){
			public boolean execute(ChestGUI gui, Page page){
				gui.getPlayer().setItemOnCursor(page.inventory().getItem(singleSlot));
				single = gui.getPlayer().getItemOnCursor();
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
		single = addMenuIcon(singleSlot, util.setItemNameLore(WaveType.SINGLE.getIcon(), "&6Single Wave", "&7At round &5x"));
		recurring = addMenuIcon(recurringSlot, util.setItemNameLore(WaveType.RECURRING.getIcon(), "&6Recurring Wave", "&7Every &5x &7rounds"));
		boss = addMenuIcon(bossSlot, util.setItemNameLore(WaveType.BOSS.getIcon(), "&6Boss Wave", "&7At round &5x"));
		for(Wave wave : gui.getArena().getWaves()){
			WavePage wavePage = new WavePage(gui, this, wave);
			addLink(wave.getIcon(), wavePage);
		}
	}
	@Override
	public boolean addLink(int slot, ItemStack icon, Page linkPage){
		Messenger.debug(gui.getPlayer(), "LINK: "+icon.getItemMeta().getDisplayName());
		links.put(icon, linkPage);
		inventory.setItem(slot, icon);
		gui.update();
		return true;
	}
	private ItemStack nextItem(int no, ItemStack item){
		if(alreadyIcon(item)){
			if(item.getItemMeta().getDisplayName().contains(" (" + no + ")")){
				Messenger.debug(gui.getPlayer(), "match");
				return nextItem(no++, util.setItemNameLore(item, item.getItemMeta().getDisplayName().replaceAll(" (" + no + ")", " (" + no++ + ")")));
			}
			return nextItem(no++, util.setItemNameLore(item, item.getItemMeta().getDisplayName() + " (" + no++ + ")"));
		}
		return item;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(isLink(item))
//				gui.switchPage(links.get(item));
				links.remove(item);
			update();
			return true;
		}
		return false;
	}
	@Override
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(item.equals(single)){
				Messenger.debug(gui.getPlayer(), "2");
				Wave newWave = Waves.add(new Wave(, WaveType.SINGLE, 1, 0, new HashMap<BGCreature, Integer>()));
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
}
