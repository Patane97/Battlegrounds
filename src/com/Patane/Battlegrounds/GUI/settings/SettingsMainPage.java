package com.Patane.Battlegrounds.GUI.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.GUI.GUI;
import com.Patane.Battlegrounds.GUI.MainPage;
import com.Patane.Battlegrounds.util.util;

public class SettingsMainPage extends MainPage{

	String[] BOOLEAN 	= {"BOOLEAN","&7 Left/Right click to add/remove 1"};
	String[] INTEGER 	= {"INTEGER","&7 Left/Right click to add/remove 1",
						   "&7 Shift click to add/remove 5"};
	String[] FLOAT 		= {"FLOAT","&7 Left/Right click to add/remove 0.5",
						   "&7 Shift click to add/remove 5"};
	
	HashMap<ItemStack, String> settingsLink = new HashMap<ItemStack, String>();
	HashMap<ItemStack, String> settingsType = new HashMap<ItemStack, String>();
	
	public SettingsMainPage(GUI gui, String name, int invSize) {
		super(gui, name, invSize);
		initilizeIcons();
	}
	private void initilizeIcons(){
		addIcon(createSettingItem("Destructable", Material.TNT, "destructable", BOOLEAN , 
				"&fCan players break/place",
				"&farena blocks during a game?"));
		addIcon(createSettingItem("PvP Damage", Material.WOOD_SWORD, "pvp", BOOLEAN , 
				"&fCan players damage other",
				"&fplayers?"));
		addIcon(createSettingItem("Spectate on Elimination", Material.EYE_OF_ENDER, "spectate-death", BOOLEAN , 
				"&fDo players become spectators",
				"&fonce eliminated?"));
		addIcon(createSettingItem("Global New Lobby Announcement", Material.SNOW_BALL, "global-new-announce", BOOLEAN , 
				"&fShould the start of a new",
				"&flobby be broadcast to the",
				"&fwhole server?"));
		addIcon(createSettingItem("Global End Game Announcement", Material.SLIME_BALL, "global-end-announce", BOOLEAN , 
				"&fShould the end of a game",
				"&fbe broadcast to the whole",
				"&fserver?"));
		addIcon(createSettingItem("First Wave Delay", Material.BLAZE_POWDER, "first-wave-delay", FLOAT , 
				"&fHow many seconds will",
				"&fit wait for the first",
				"&fround to start?"));
		addIcon(createSettingItem("Default Wave Delay", Material.MAGMA_CREAM, "wave-delay", FLOAT , 
				"&fHow many seconds will",
				"&fit wait between ending",
				"&fone round and starting",
				"&fthe next?"));
		addIcon(createSettingItem("Final Wave No.", Material.SKULL_ITEM, "final-wave", INTEGER , 
				"&fWhat is the final wave",
				"&fthat can be played?",
				"&f(-1 for no final wave)"));
		addIcon(createSettingItem("Food Regeneration", Material.APPLE, "food-regen", INTEGER , 
				"&fHow many points of food",
				"&fwill players regenerate",
				"&fper 5 seconds?",
				"&f(-1 for no regeneration)"));
		addIcon(createSettingItem("Minimum Players", Material.BUCKET, "min-players", INTEGER , 
				"&fWhat are the minimum",
				"&famount of players needed",
				"&fto start a match?"));
		addIcon(createSettingItem("Maximum Players", Material.WATER_BUCKET, "max-players", INTEGER , 
				"&fWhat are the maximum",
				"&famount of players allowed",
				"&fin a match?"));
		
	}
	private ItemStack createSettingItem(String settingName, Material settingItem, String YML, String[] footer, String ... lore){
		settingItem = (settingItem == null ? Material.PAPER : settingItem);
		ItemStack item = new ItemStack(settingItem);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(Chat.translate("&c&l" + settingName));
		
		List<String> finalLore = new ArrayList<String>(Arrays.asList(lore));
		String type = footer[0];
		finalLore.addAll(Arrays.asList(Arrays.copyOfRange(footer, 1, footer.length)));
		finalLore = Chat.translate(finalLore);
		itemMeta.setLore(finalLore);
		
		item.setItemMeta(itemMeta);
		
		settingsLink.put(item, YML);
		settingsType.put(item, type);
		return util.hideAttributes(item);
	}
	private void updateSettingItem(ItemStack item, String link, int slot) {
		ItemMeta itemMeta = item.getItemMeta();
		String value = "PLACEHOLDER"; //use link to find value
		List<String> tempLore = itemMeta.getLore();
		tempLore.set(0, value);
		itemMeta.setLore(tempLore);
		
		item.setItemMeta(itemMeta);
		addIcon(slot, item);
	}
	private Boolean toggleBoolean(String YML){
		return true;
	}
	private int changeInteger(String string, ClickType click) {
		return 0;
	}
	private float changeFloat(String string, ClickType click) {
		return 0;
	}
	@Override
	public boolean pickupItem(boolean topInv, ClickType click, ItemStack item, int slot){
		if(super.pickupItem(topInv, click, item, slot))
			return true;
		if(topInv){
			if(settingsType.containsKey(item)){
				String type = settingsType.get(item);
				String link = settingsType.get(item);
				switch(type){
				case "BOOLEAN": toggleBoolean(link);
				case "INTEGER": changeInteger(link, click);
				case "FLOAT": changeFloat(link, click);
				}
				updateSettingItem(item, link, slot);
			}
		}
		return false;
	}

}
