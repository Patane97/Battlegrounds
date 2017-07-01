package com.Patane.Battlegrounds.arena.game.waves;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.util.util;

public enum WaveType {
	SINGLE(util.createItem(Material.IRON_NUGGET, 1, (short) 0, "&6Unnamed Wave"), "&7At round {INCREMENT}"),
	RECURRING(util.createItem(Material.MELON_SEEDS, 1, (short) 0, "&6Unnamed Wave"), "&7Every {INCREMENT} rounds"),
	BOSS(util.createItem(Material.NETHER_STAR, 1, (short) 0, "&6Unnamed Wave"), "&7At round {INCREMENT}");
	
	ItemStack icon;
	String description;
	
	WaveType (ItemStack icon, String description){
		this.icon 			= icon;
		this.description 	= description;
	}

	public ItemStack getIcon() {
		return icon;
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
}
