package com.Patane.Battlegrounds.arena.game.waves;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.util.util;

public enum WaveType {
	SINGLE("Single", IncrementType.SINGULAR, util.createItem(Material.IRON_NUGGET, 1, (short) 0, "&6Unnamed Wave"), "&7Wave at {INCREMENT}", 3),
	RECURRING("Recurring", IncrementType.RECURRING, util.createItem(Material.MELON_SEEDS, 1, (short) 0, "&6Unnamed Wave"), "&7Wave every {INCREMENT}", 4),
	BOSS("Boss", IncrementType.SINGULAR, util.createItem(Material.NETHER_STAR, 1, (short) 0, "&6Unnamed Wave"), "&7Boss at {INCREMENT}", 5);
	
	String name;
	IncrementType incType;
	ItemStack icon;
	String description;
	int slot;
	
	WaveType (String name, IncrementType incType, ItemStack icon, String description, int slot){
		this.name			= name;
		this.incType		= incType;
		this.icon 			= icon;
		this.description 	= description;
		this.slot			= slot;
	}
	public String getName() {
		return name;
	}
	public ItemStack getIcon() {
		return icon;
	}
	public int getSlot() {
		return slot;
	}
	public ItemStack getAddingIcon(){
		return util.createItem(icon.getType(), 1, (short) 0, "&6Add " + name, incType.getDesc());
	}
	
	public String getDesc(int increment){
		return description.replaceAll("\\{INCREMENT\\}", String.valueOf(increment));
	}
	
	public static WaveType getFromName(String waveTypeName){
		for(WaveType waveType : WaveType.values()){
			if(waveType.name().equals(waveTypeName))
				return waveType;
		}
		return null;
	}
	private enum IncrementType{
		SINGULAR("&7At round &5x"), RECURRING("&7Every &5x &7rounds");
		
		final String desc;
		
		IncrementType(String desc){
			this.desc = desc;
		}
		
		public String getDesc(){
			return desc;
		}
	}
}
