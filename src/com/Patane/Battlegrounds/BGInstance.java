package com.Patane.Battlegrounds;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.Player;

public interface BGInstance {
	public String getName();
	
	public World getWorld();

	public ArrayList<String> getPlayerNames();
	
	public ArrayList<Player> getPlayers();
}
