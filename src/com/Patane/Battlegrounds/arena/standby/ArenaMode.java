package com.Patane.Battlegrounds.arena.standby;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.listeners.ArenaListener;

public interface ArenaMode{
	public String getColor();
	public boolean addPlayer(Player player);
	public void playerSetupValues(Player player);
	public boolean randomTeleport(Player player, ArrayList<Location> spectatorSpawns);
	public boolean removePlayer(String displayName, boolean check);
	public boolean checkSessionOver();
	public void sessionOver();
	public void sessionOver(ArenaMode newMode);
	public ArenaListener getListener();
	public void unregister();
}
