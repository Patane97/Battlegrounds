package com.Patane.Battlegrounds.util;

import java.util.HashMap;
import java.util.StringJoiner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import com.Patane.Battlegrounds.Messenger;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;


public class YML {

	public static String locationFormat(Location location){
		StringJoiner stringLocation = new StringJoiner(",");
		stringLocation.add(location.getWorld().getName());
		stringLocation.add(Double.toString(location.getBlockX()));
		stringLocation.add(Double.toString(location.getBlockY()));
		stringLocation.add(Double.toString(location.getBlockZ()));
		stringLocation.add(Double.toString(location.getYaw()));
		stringLocation.add(Double.toString(location.getPitch()));
		
		return stringLocation.toString();
	}
	public static Location getLocation(String locationYML){
		String[] locationSplit = locationYML.split(",");
		try{
			World world = Bukkit.getWorld(locationSplit[0]);
			double x = Double.parseDouble(locationSplit[1]);
			double y = Double.parseDouble(locationSplit[2]);
			double z = Double.parseDouble(locationSplit[3]);
			float yaw = (float) Double.parseDouble(locationSplit[4]);
			float pitch = (float) Double.parseDouble(locationSplit[5]);
			Location location = new Location(world, x, y, z, yaw, pitch);
			return location;
		} catch (Exception e){
			return null;
		}
	}
	public static String spawnLocationFormat(Location location, boolean yaw){
		StringJoiner stringLocation = new StringJoiner(",");
		stringLocation.add(Integer.toString(location.getBlockX()));
		stringLocation.add(Integer.toString(location.getBlockY()));
		stringLocation.add(Integer.toString(location.getBlockZ()));
		if(yaw)
			stringLocation.add(Double.toString(location.getYaw()));
		return stringLocation.toString();
	}
	public static Location getSpawnLocation(World world, String locationYML){
		String[] locationSplit = locationYML.split(",");
		try{
			double x = Double.parseDouble(locationSplit[0]);
			double y = Double.parseDouble(locationSplit[1]);
			double z = Double.parseDouble(locationSplit[2]);
			Location location;
			if(locationSplit.length > 3){
				float yaw = (float) Double.parseDouble(locationSplit[3]);
				location = new Location(world, x, y, z, yaw, 0);
			} else
				location = new Location(world, x, y, z);
			return location;
		} catch (Exception e){
			return null;
		}
	}
	public static String blockVector2DFormat(BlockVector2D vector){
		String temp = vector.getBlockX() + "," + vector.getBlockZ();
		return temp;
	}
	public static BlockVector2D getBlockVector2D(String blockVectorYML){
		String[] blockVectorSplit = blockVectorYML.split(",");
		try{
			int x = Integer.parseInt(blockVectorSplit[0]);
			int z = Integer.parseInt(blockVectorSplit[1]);
			BlockVector2D blockVector = new BlockVector2D(x,z);
			return blockVector;
		} catch (Exception e){
			return null;
		}
	}
	public static String blockVectorFormat(BlockVector vector){
		String temp = vector.getBlockX() + "," + vector.getBlockY() + "," + vector.getBlockZ();
		return temp;
	}
	public static BlockVector getBlockVector(String blockVectorYML){
		String[] blockVectorSplit = blockVectorYML.split(",");
		try{
			int x = Integer.parseInt(blockVectorSplit[0]);
			int y = Integer.parseInt(blockVectorSplit[1]);
			int z = Integer.parseInt(blockVectorSplit[2]);
			BlockVector blockVector = new BlockVector(x,y,z);
			return blockVector;
		} catch (Exception e){
			return null;
		}
	}
	public static HashMap<Integer, ItemStack> playerInventoryContentsFormat(ItemStack[] items){
		HashMap<Integer, ItemStack> tempItems = new HashMap<Integer, ItemStack>();
		Messenger.info("SIZE: "+ items.length);
		for(int i = 0 ; i < items.length ; i++){
			tempItems.put(i, items[i]);
		}
		return tempItems;
	}
	public static ItemStack[] getPlayerInventoryContents(HashMap<Integer, ItemStack> itemsHash){
		ItemStack[] items = new ItemStack[36];
		for(int i = 0 ; i < items.length ; i++){
			items[i] = null;
			if(itemsHash.containsKey(i))
				items[i] = itemsHash.get(i);
		}
		return items;
	}
}
