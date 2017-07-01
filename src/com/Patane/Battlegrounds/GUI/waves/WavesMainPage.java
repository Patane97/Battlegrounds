package com.Patane.Battlegrounds.GUI.waves;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.GUI.MainPage;
import com.Patane.Battlegrounds.GUI.Page;
import com.Patane.Battlegrounds.arena.editor.waves.WavesGUI;
import com.Patane.Battlegrounds.arena.game.waves.Wave;
import com.Patane.Battlegrounds.arena.game.waves.WaveType;
import com.Patane.Battlegrounds.collections.Waves;
import com.Patane.Battlegrounds.custom.BGCreature;
import com.Patane.Battlegrounds.util.util;

public class WavesMainPage extends MainPage{
	WavesGUI gui;
	ItemStack single;
	ItemStack recurring;
	ItemStack boss;
	
	
	public WavesMainPage(WavesGUI gui, String name, int invSize) {
		super(gui, name, invSize);
		this.gui = gui;
		initilize();
	}
	protected void initilize(){
		single = addMenuIcon(3, util.setItemNameLore(WaveType.SINGLE.getIcon(), "&6Single Wave", "&7At round &5x"));
		recurring = addMenuIcon(4, util.setItemNameLore(WaveType.RECURRING.getIcon(), "&6Recurring Wave", "&7Every &5x &7rounds"));
		boss = addMenuIcon(5, util.setItemNameLore(WaveType.BOSS.getIcon(), "&6Boss Wave", "&7At round &5x"));
		for(Wave wave : gui.getArena().getWaves()){
			WavePage wavePage = new WavePage(gui, this, wave);
			addLink(wave.getIcon(), wavePage);
		}
	}
	@Override
	public boolean addLink(int slot, ItemStack icon, Page linkPage){
		icon = nextItem(1, icon);
		links.put(icon, linkPage);
		inventory.setItem(slot, icon);
		gui.update();
		return true;
	}
	private ItemStack nextItem(int no, ItemStack item){
		if(alreadyIcon(item)){
			if(!item.getItemMeta().getDisplayName().contains(" (" + no + ")"))
				return nextItem(no, util.setItemNameLore(item, item.getItemMeta().getDisplayName() + " (" + no + ")"));
			return nextItem(no, util.setItemNameLore(item, item.getItemMeta().getDisplayName().replaceAll(" (" + no + ")", " (" + no++ + ")")));
		}
		return item;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(isMenu(slot)){
				Messenger.debug(gui.getPlayer(), "0");
				if(slot == 3 || slot == 4 || slot == 5){
					// BUG: ADD A SINGLE, TRYING TO ADD A SECOND SINGLE DOESNT GET PAST ABOVE IF STATEMENT!
					gui.getPlayer().setItemOnCursor(item);
					Messenger.debug(gui.getPlayer(), "1");
					return true;
				}
			}
			if(isLink(item))
				gui.switchPage(links.get(item));
			Messenger.debug(gui.getPlayer(), "2");
			return true;
		}
		if(super.pickupItem(topInv, click, item, slot)){
			Messenger.debug(gui.getPlayer(), "3");
			return true;
		}
		return false;
	}
	@Override
	public boolean placeItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(topInv){
			if(item.equals(single)){
				Wave newWave = Waves.add(new Wave("&6Single Wave", WaveType.SINGLE, 1, 0, new HashMap<BGCreature, Integer>()));
				// save YML
				gui.getArena().addWave(newWave);
				WavePage wavePage = new WavePage(gui, gui.getMainPage(), newWave);
				if(addLink(newWave.getIcon(), wavePage)){
					Messenger.send(gui.getPlayer(), "&aAdded &7" + ChatColor.stripColor(newWave.getName()) + "&a to &7" + gui.getArena().getName() + "&a.");
				}
				return true;
			}
			if(super.placeItem(topInv, click, item, slot))
				return true;
			
		}
		return false;
	}
}
