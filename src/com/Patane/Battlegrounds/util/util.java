package com.Patane.Battlegrounds.util;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Wool;

public class util {
	public static void setColouredWool(Block block, DyeColor color){
		block.setType(Material.WOOL);
		BlockState state = block.getState();
		Wool woolData = (Wool)state.getData();
		woolData.setColor(color);
		state.setData(woolData);
		state.update();
	}
}
