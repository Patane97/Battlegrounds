package com.Patane.Battlegrounds.util;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;

public class util {
	public static void setColouredWool(Block block, DyeColor color){
		block.setType(Material.WOOL);
		BlockState state = block.getState();
		Wool woolData = (Wool)state.getData();
		woolData.setColor(color);
		state.setData(woolData);
		state.update();
	}
	public static ItemStack hideAttributes(ItemStack item){
		ItemMeta iM = item.getItemMeta();
        iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(iM);
        return item;
	}
	public static String formaliseString(String s){
		s = s.toLowerCase();
		s = s.substring(0, 1).toUpperCase() + s.substring(1);
		return s;
	}
//	public static boolean checkRegionIntersecting(AbstractRegion region){
//		for(Arena arena : Arenas.get()){
//			AbstractRegion ground = arena.getGround();
//			AbstractRegion lobby = arena.getLobby();
//			for(region.)
//		}
//	}
	@SuppressWarnings("deprecation")
	public static AbstractRegion getAbstractRegion(Plugin plugin, Player creator){
		WorldEditPlugin worldEditPlugin = (WorldEditPlugin) plugin.getServer().getPluginManager().getPlugin("WorldEdit");
		String error = null;
		if(worldEditPlugin == null){
			error = "Worldedit plugin required!";
			Messenger.send(creator, "&cError: " + error);
			return null;
		}
		Selection selection = worldEditPlugin.getSelection(creator);
		if(selection == null){
			error = "Please make a worldedit selection.";
			Messenger.send(creator, "&cError: " + error);
			return null;
		}
		World world = selection.getWorld();
		AbstractRegion region = null;
		if (selection instanceof Polygonal2DSelection){
			Polygonal2DSelection polySel = (Polygonal2DSelection) selection;
			int minY = polySel.getNativeMinimumPoint().getBlockY();
			int maxY = polySel.getNativeMaximumPoint().getBlockY();
			region = new Polygonal2DRegion (BukkitUtil.getLocalWorld(world), polySel.getNativePoints(), minY, maxY);
		} else if (selection instanceof CuboidSelection) {
			BlockVector min = selection.getNativeMinimumPoint().toBlockVector();
			BlockVector max = selection.getNativeMaximumPoint().toBlockVector();
			region = new CuboidRegion (BukkitUtil.getLocalWorld(world), min, max);
		} else {
			error = "Cuboid or Polygonal region required!";
			Messenger.send(creator, "&cError: " + error);
			return null;
		}
		return region;
	}
}
