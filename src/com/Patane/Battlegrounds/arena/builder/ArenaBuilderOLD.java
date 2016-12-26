package com.Patane.Battlegrounds.arena.builder;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.collections.Arenas;
import com.Patane.Battlegrounds.util.Locations;
/**
 * This class is run whenever a player is creating a new arena.
 * @author Patane97
 *
 */
public class ArenaBuilderOLD implements Runnable{
	boolean ready = false;
	public Plugin plugin;
	public Player creator;
	
	public String arenaName;
	
	public World world;

	public Location lobby1;
	public Location lobby2;
	public Location lobbySpawn;
	
	public Location arena1;
	public Location arena2;
	public Location playerSpawn;
	public Location creatureSpawn;
	
	public Location spectatorSpawn;
	
	public Location newLocation;
	
	boolean saveOnFinish;
	
	public ArenaBuilderListeners listener;
	
	
	public ArenaBuilder(Plugin plugin, Player creator, String arenaName, ArenaBuilderListeners listener, boolean saveOnFinish){
		this.plugin					= plugin;
		this.creator				= creator;
		this.arenaName 				= arenaName;
		this.world 					= creator.getWorld();	
		this.saveOnFinish			= saveOnFinish;
		this.listener				= listener;
		listener.setBuilder(this);
	}
	public void run(){
		try {
			setLobbyPoints();
			setLobbySpawn();
			
			setArenaPoints();
			setPlayerSpawn();
			setCreatureSpawn();
			
			setSpectatorSpawn();
			
			ArenaHandler newArena = new ArenaHandler(this);
			if(saveOnFinish){
				// saves arena to arenas.yml
				ArenaYML.saveArena(this);
				// adds arena to ArrayList of all active arenas
				Arenas.add(newArena);
			}
			HandlerList.unregisterAll(listener);
			Messenger.send(creator, "Arena '" + newArena.getName() + "' created!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private void setSpectatorSpawn() throws InterruptedException {
		Messenger.send(creator, "Right Click on the Spectators Spawn Location.");
		while (!ready)
			Thread.sleep(100);
		if(Locations.isWithin(newLocation, arena1, arena2) || Locations.isWithin(newLocation, lobby1, lobby2)){
			Messenger.send(creator, "Spectator Spawn Location CAN NOT be inside the Lobby or Arena.");
			ready = false;
			setSpectatorSpawn();
			return;
		}
		spectatorSpawn = newLocation;
		ready = false;
		Messenger.send(creator, "Spectator Spawn Location saved...");
	}
	private void setCreatureSpawn() throws InterruptedException {
		Messenger.send(creator, "Right Click on the Creature Spawn Location.");
		while (!ready)
			Thread.sleep(100);
		if(!Locations.isWithin(newLocation, arena1, arena2)){
			Messenger.send(creator, "Creature Spawn must be inside the Arena.");
			ready = false;
			setCreatureSpawn();
			return;
		}
		creatureSpawn = newLocation;
		ready = false;
		Messenger.send(creator, "Creature Spawn Location saved...");
	}
	private void setPlayerSpawn() throws InterruptedException {
		Messenger.send(creator, "Right Click on the Arena Spawn Location.");
		while (!ready)
			Thread.sleep(100);
		if(!Locations.isWithin(newLocation, arena1, arena2)){
			Messenger.send(creator, "Arena Spawn must be inside the Arena.");
			ready = false;
			setPlayerSpawn();
			return;
		}
		playerSpawn = newLocation;
		ready = false;
		Messenger.send(creator, "Arena Spawn Location saved...");
		
	}
	private void setArenaPoints() throws InterruptedException {
		Messenger.send(creator, "Right Click on first Arena Point.");
		while (!ready)
			Thread.sleep(100);
		arena1 = newLocation;
		ready = false;
		Messenger.send(creator, "Right Click on second Arena Point.");
		while (!ready)
			Thread.sleep(100);
		arena2 = newLocation;
		ready = false;
		Messenger.send(creator, "Arena Points saved...");
	}
	private void setLobbySpawn() throws InterruptedException {
		Messenger.send(creator, "Right Click on the Lobby Spawn Location.");
		while (!ready)
			Thread.sleep(100);
		if(!Locations.isWithin(newLocation, lobby1, lobby2)){
			Messenger.send(creator, "Lobby Spawn must be inside the Lobby.");
			ready = false;
			setLobbySpawn();
			return;
		}
		lobbySpawn = newLocation;
		ready = false;
		Messenger.send(creator, "Lobby Spawn Location saved...");
		
	}
	private void setLobbyPoints() throws InterruptedException {
		Messenger.send(creator, "Right Click on first Lobby Point.");
		// waits until newLocation has been changed by the EventHandler 'onInteraction'
		while (!ready)
			Thread.sleep(100);
		lobby1 = newLocation;
		ready = false;
		Messenger.send(creator, "Right Click on second Lobby Point.");
		while (!ready)
			Thread.sleep(100);
		lobby2 = newLocation;
		ready = false;
		Messenger.send(creator, "Lobby Points saved...");
		
	}
	public Player getCreator(){
		return creator;
	}
	public void setNewLocation(Location location){
		newLocation = location;
		ready = true;
		//Messenger.send(creator, "newLocation updated to: " + newLocation.toString());
	}
}
