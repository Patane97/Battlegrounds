package com.Patane.Battlegrounds.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class util {
	public static void setLocationYMLFormat(Config config, String prefix, Location location){
		config.set(prefix + ".x", location.getX());
		config.set(prefix + ".y", location.getY());
		config.set(prefix + ".z", location.getZ());
		config.set(prefix + ".pitch", location.getPitch());
		config.set(prefix + ".yaw", location.getYaw());
		config.set(prefix + ".world", location.getWorld().getName());
	}
	public static Location getLocationYMLFormat(Config config, String prefix){
		double x, y, z;
		float pitch, yaw;
		World world;
		
		x = config.getDouble(prefix + ".x");
		y = config.getDouble(prefix + ".y");
		z = config.getDouble(prefix + ".z");
		pitch = (float) config.getDouble(prefix + ".pitch");
		yaw = (float) config.getDouble(prefix + ".yaw");
		world = Bukkit.getServer().getWorld(config.get(prefix + ".world").toString());
		
		return new Location(world, x, y, z, pitch, yaw);
	}
}
