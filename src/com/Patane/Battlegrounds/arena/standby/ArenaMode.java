package com.Patane.Battlegrounds.arena.standby;

import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.listeners.ArenaListener;

public interface ArenaMode{
	public void addPlayer(Player player);
	public boolean checkSessionOver();
	public void sessionOver();
	public void sessionOver(ArenaMode newMode);
	public ArenaListener getListener();
	public void unregister();
}
