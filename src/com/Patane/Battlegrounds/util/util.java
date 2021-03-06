package com.Patane.Battlegrounds.util;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.collections.Arenas;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;

public class util {
	public static void setColouredWool(Block block, DyeColor color) {
		block.setType(Material.WOOL);
		BlockState state = block.getState();
		Wool woolData = (Wool) state.getData();
		woolData.setColor(color);
		state.setData(woolData);
		state.update();
	}

	public static ItemStack hideFlags(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack hideFlags(ItemStack item, ItemFlag...flags) {
		ItemMeta meta = item.getItemMeta();
		for(ItemFlag flag : flags)
			meta.addItemFlags(flag);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack createItem(Material material, int amount, short data, String name, String...lore){
		ItemStack item = new ItemStack(material, amount, data);
		ItemMeta itemMeta = item.getItemMeta();
		if(name != null)
			itemMeta.setDisplayName(Chat.translate(name));
		if(lore != null){
			List<String> finalLore = new ArrayList<String>(Arrays.asList(lore));
			finalLore = Chat.translate(finalLore);
			itemMeta.setLore(finalLore);
		}
		
		item.setItemMeta(itemMeta);
		
		return item;
	}
	public static ItemStack createEnchantBook(Enchantment enchantment, int level, boolean ignoreLevelRestriction, String name, String...lore) {
		ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
		meta.addStoredEnchant(enchantment, level, ignoreLevelRestriction);
		book.setItemMeta(meta);
		return setItemNameLore(book, name, lore);
	}
	public static ItemStack setItemNameLore(ItemStack item, String name, String... lore) {
		ItemMeta itemMeta = item.getItemMeta();
		if(name != null)
			itemMeta.setDisplayName(Chat.translate(name));
		if(lore.length > 0)
			itemMeta.setLore(Chat.translate(Arrays.asList(lore)));
		item.setItemMeta(itemMeta);
		return item;
	}
	public static String formaliseString(String s) {
		s = s.toLowerCase();
		s = s.substring(0, 1).toUpperCase() + s.substring(1);
		return s;
	}

	@SuppressWarnings("deprecation")
	public static AbstractRegion getAbstractRegion(Plugin plugin, Player creator) {
		WorldEditPlugin worldEditPlugin = (WorldEditPlugin) plugin.getServer().getPluginManager()
				.getPlugin("WorldEdit");
		if (worldEditPlugin == null) {
			Messenger.send(creator, "&cWorldedit plugin required!");
			return null;
		}
		Selection selection = worldEditPlugin.getSelection(creator);
		if (selection == null) {
			Messenger.send(creator, "&cPlease make a worldedit selection.");
			return null; 
		}
		World world = selection.getWorld();
		AbstractRegion region = null;
		if (selection instanceof Polygonal2DSelection) {
			Polygonal2DSelection polySel = (Polygonal2DSelection) selection;
			int minY = polySel.getNativeMinimumPoint().getBlockY();
			int maxY = polySel.getNativeMaximumPoint().getBlockY();
			region = new Polygonal2DRegion(BukkitUtil.getLocalWorld(world), polySel.getNativePoints(), minY, maxY);
		} else if (selection instanceof CuboidSelection) {
			BlockVector min = selection.getNativeMinimumPoint().toBlockVector();
			BlockVector max = selection.getNativeMaximumPoint().toBlockVector();
			region = new CuboidRegion(BukkitUtil.getLocalWorld(world), min, max);
		} else {
			Messenger.send(creator, "&cCuboid or Polygonal region selection is required!");
			return null;
		}
		for (Arena selectedArena : Arenas.get()) {
			if (intersecting(region, selectedArena.getLobby())) {
				Messenger.send(creator, "&cSelected Region is intersecting with arena &7" + selectedArena.getName()
						+ "&c's &7Lobby &cregion.");
				region = null;
			}
			if (intersecting(region, selectedArena.getGround())) {
				Messenger.send(creator, "&cSelected Region is intersecting with arena &7" + selectedArena.getName()
						+ "'s Ground &cregion.");
				region = null;
			}
		}
		return region;
	}

	public static boolean intersecting(AbstractRegion region1, AbstractRegion region2) {
		if (region1 == null || region2 == null)
			return false;
		int r1max, r2max, r1min, r2min;
		r1max = (region1 instanceof Polygonal2DRegion ? ((Polygonal2DRegion) region1).getMaximumY()
				: region1.getMaximumPoint().getBlockY());
		r2max = (region2 instanceof Polygonal2DRegion ? ((Polygonal2DRegion) region2).getMaximumY()
				: region2.getMaximumPoint().getBlockY());
		r1min = (region1 instanceof Polygonal2DRegion ? ((Polygonal2DRegion) region1).getMinimumY()
				: region1.getMinimumPoint().getBlockY());
		r2min = (region2 instanceof Polygonal2DRegion ? ((Polygonal2DRegion) region2).getMinimumY()
				: region2.getMinimumPoint().getBlockY());

		if (Math.max(r1min, r2min) > Math.min(r1max, r2max)) {
			return false;
		}
		List<BlockVector2D> r1points = getPoints(region1);
		List<BlockVector2D> r2points = getPoints(region2);

		BlockVector2D lastR1Point = r1points.get(r1points.size() - 1);
		BlockVector2D lastR2Point = r2points.get(r2points.size() - 1);
		for (BlockVector2D selectedR1Point : r1points) {
			for (BlockVector2D selectedR2Point : r2points) {

				Line2D line1 = new Line2D.Double(lastR1Point.getBlockX(), lastR1Point.getBlockZ(),
						selectedR1Point.getBlockX(), selectedR1Point.getBlockZ());

				if (line1.intersectsLine(lastR2Point.getBlockX(), lastR2Point.getBlockZ(), selectedR2Point.getBlockX(),
						selectedR2Point.getBlockZ())) {
					return true;
				}
				lastR2Point = selectedR2Point;
			}
			lastR1Point = selectedR1Point;
		}
		return false;
	}

	public static List<BlockVector2D> getPoints(AbstractRegion region) {
		if (region instanceof CuboidRegion) {
			List<BlockVector2D> points = new ArrayList<BlockVector2D>();
			int x1 = region.getMinimumPoint().getBlockX();
			int x2 = region.getMaximumPoint().getBlockX();
			int z1 = region.getMinimumPoint().getBlockZ();
			int z2 = region.getMaximumPoint().getBlockZ();

			points.add(new BlockVector2D(x1, z1));
			points.add(new BlockVector2D(x2, z1));
			points.add(new BlockVector2D(x2, z2));
			points.add(new BlockVector2D(x1, z2));
			return points;
		}
		if (region instanceof Polygonal2DRegion)
			return ((Polygonal2DRegion) region).getPoints();
		else
			return null;
	}

	public static Vector[] changeEachDir(int amount) {
		List<Vector> changes = new ArrayList<Vector>(6);
		changes.add(new Vector(0, 1, 0).multiply(amount));
		changes.add(new Vector(0, -1, 0).multiply(amount));
		changes.add(new Vector(1, 0, 0).multiply(amount));
		changes.add(new Vector(-1, 0, 0).multiply(amount));
		changes.add(new Vector(0, 0, 1).multiply(amount));
		changes.add(new Vector(0, 0, -1).multiply(amount));
		return (Vector[]) changes.toArray(new Vector[0]);
	}

	public static String stringJoiner(ArrayList<String> strings, String delimiter) {
		return stringJoiner(strings.toArray(new String[0]), delimiter);
	}
	public static String stringJoiner(String[] strings, String delimiter) {
		return stringJoiner(strings, new StringJoiner(delimiter));
	}
	public static String stringJoiner(String[] strings, String delimiter, String prefix, String suffix) {
		return stringJoiner(strings, new StringJoiner(delimiter, prefix, suffix));
	}
	private static String stringJoiner(String[] strings, StringJoiner stringJoiner){
		for(String string : strings){
			stringJoiner.add(string);
		}
		return stringJoiner.toString();
	}

	@SuppressWarnings("null")
	public static int getSlot(Inventory inventory, ItemStack thisItem) {
		int count = 0;
		for(ItemStack item : inventory.getContents()){
			if(thisItem.equals(item)){
				return count;
			}
			count++;
		}
		return (Integer) null;
	}

	public static ItemStack transItemColorCodes(ItemStack result) {
		ItemMeta itemMeta = result.getItemMeta();
		itemMeta.setDisplayName(Chat.translate(itemMeta.getDisplayName()));
		itemMeta.setLore(Chat.translate(itemMeta.getLore()));
		result.setItemMeta(itemMeta);
		return result;
	}



}
