package com.Patane.Battlegrounds.game;

import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.BGInstance;
import com.Patane.Battlegrounds.arena.ArenaHandler;

public interface GameManager extends BGInstance{
	
	public ArenaHandler getArena();
	
	public String getName();
	
	public RoundHandler getRoundHandler();
		
	public void addPlayer(Player player);
	
	public boolean kickPlayer(String player, Boolean silent);
	
	public boolean playerKilled(Player player);

	public boolean playerLeave(Player player, Boolean check);
	
	public boolean checkGameEnd();

	public void gameOver();
}
