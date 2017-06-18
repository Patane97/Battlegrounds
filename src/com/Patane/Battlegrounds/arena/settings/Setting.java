package com.Patane.Battlegrounds.arena.settings;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.util.util;

public enum Setting {
	DESTRUCTABLE("Destructable", "destructable", SettingType.BOOLEAN, false, Material.TNT, (short) 0,
			"&fCan players break/place",
			"&farena blocks during a game?"),
	PVP_ENABLED("PvP Damage", "pvp", SettingType.BOOLEAN, false, Material.WOOD_SWORD, (short) 0, 
			"&fCan players damage other",
			"&fplayers?"),
	SPECTATE_DEATH("Spectate on Elimination", "spectate-death", SettingType.BOOLEAN, true, Material.EYE_OF_ENDER, (short) 0,
			"&fDo players become spectators",
			"&fonce eliminated?"),
	GLOBAL_NEW_ANNOUNCE("Global New Lobby Announcement", "global-new-announce", SettingType.BOOLEAN, true, Material.SNOW_BALL, (short) 0,
			"&fShould the start of a new",
			"&flobby be broadcast to the",
			"&fwhole server?"),
	GLOBAL_END_ANNOUNCE("Global End Game Announcement", "global-end-announce", SettingType.BOOLEAN, true, Material.SLIME_BALL, (short) 0,
			"&fShould the end of a game",
			"&fbe broadcast to the whole",
			"&fserver?"),
	FIRST_DELAY("First Wave Delay", "first-wave-delay", SettingType.FLOAT, 7.0f, 0.0f, Material.BLAZE_POWDER, (short) 0,
			"&fHow many seconds will",
			"&fit wait for the first",
			"&fround to start?"),
	WAVE_DELAY("Default Wave Delay", "wave-delay", SettingType.FLOAT, 5.0f, 0.0f, Material.MAGMA_CREAM, (short) 0,
			"&fHow many seconds will",
			"&fit wait between ending",
			"&fone round and starting"),
	FINAL_WAVE("Final Wave No.", "final-wave", SettingType.INTEGER, 0, 0, Material.SKULL_ITEM, (short) 0,
			"&fWhat is the final wave",
			"&fthat can be played?",
			"&f(0 = no final wave)"),
	FOOD_REGEN("Food Regeneration", "food-regen", SettingType.INTEGER, 0, 0, Material.APPLE, (short) 0,
			"&fHow many points of food",
			"&fwill players regenerate",
			"&fper 5 seconds?",
			"&f(0 = no regen)"),
	MIN_PLAYERS("Minimum Players", "min-players", SettingType.INTEGER, 1, 1, Material.BUCKET, (short) 0,
			"&fWhat are the minimum",
			"&famount of players needed",
			"&fto start a match?"),
	MAX_PLAYERS("Maximum Players", "max-players", SettingType.INTEGER, 0, 0, Material.WATER_BUCKET, (short) 0,
			"&fWhat are the maximum",
			"&famount of players allowed",
			"&fto join a match?");
	
	private String formalName;
	private String ymlName;
	private Object defaultValue;
	private ItemStack item;
	private SettingType type;
	private Object min;

	Setting(String formalName, String ymlName, SettingType type, Object defaultValue, Material material, short data, String ...lore){
		this.defaultValue = defaultValue;
		this.formalName = formalName;
		this.ymlName = ymlName;
		this.type = type;
		String[] firstLine = {"&6["+defaultValue+"]"};
		lore = ArrayUtils.addAll(firstLine, lore);
		this.item = util.createItem(material, 1, data, "&3"+formalName, lore);
	}
	Setting(String formalName, String ymlName, SettingType type, Object defaultValue, Object min, Material material, short data, String ...lore){
		this(formalName, ymlName, type, defaultValue, material, data, lore);
		this.min = min;
	}
	public String getFormalName(){
		return this.formalName;
	}
	public String getYmlName(){
		return this.ymlName;
	}
	public ItemStack getItem(){
		return this.item;
	}
	public Object getDefault(){
		return this.defaultValue;
	}
	public Object getMin(){
		return this.min;
	}
	public ItemStack getItem(Object value){
		ItemStack tempItem = item.clone();
		ItemMeta itemMeta = tempItem.getItemMeta();
		List<String> tempLore = itemMeta.getLore();
		tempLore.set(0, Chat.translate("&6["+value+"]"));
		itemMeta.setLore(tempLore);
		tempItem.setItemMeta(itemMeta);
		return tempItem;
	}
	public SettingType getType(){
		return this.type;
	}
	public static Setting getFromName(String ymlName){
		for(Setting setting : Setting.values()){
			if(setting.getYmlName().equals(ymlName))
				return setting;
		}
		return null;
	}
}
