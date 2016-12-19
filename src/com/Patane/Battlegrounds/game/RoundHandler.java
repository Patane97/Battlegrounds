package com.Patane.Battlegrounds.game;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.collections.*;

public class RoundHandler {
	int roundNo;
	Player target;
	GameHandler gameInstance;
	ArrayList<Entity> mobs = new ArrayList<Entity>();
	
	RoundHandler(Player host, GameHandler game){
		target = host;
		gameInstance = game;
		roundNo = 1;
		RoundInstances.roundAdd(this);
	}
	RoundHandler(Player host, GameHandler game, int a){
		target = host;
		gameInstance = game;
		roundNo = a;
		RoundInstances.roundAdd(this);
	}
	public void nextRound(){
		Messenger.gameCast(gameInstance, "Round " + roundNo + " Started!");
		// determining how many mobs/zombies to spawn
		for (int i=0; i < roundNo*2; i++){
			Entity mob = target.getWorld().spawnEntity(target.getLocation(), EntityType.ZOMBIE);
			mob.setCustomName(mob.getCustomName());
			mob.setCustomNameVisible(true);
			mobs.add(mob);
		}
	}
	public void checkRoundEnd(Entity mob){
		if(mobs.remove(mob)) Messenger.gameCast(gameInstance, "Mob killed!");
		if(mobs.isEmpty()){
			roundNo++;
			nextRound();
		}
	}
	public int getRoundNo(){
		return roundNo;
	}
	public Player getTarget(){
		return target;
	}
	public ArrayList<Entity> getActiveMobs(){
		return mobs;
	}
	public void clearMobs() {
		for(Entity mob : mobs){
			mob.remove();
		}
		
	}
}
