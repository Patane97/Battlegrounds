package com.Patane.Battlegrounds.arena.builder;

import java.util.ArrayList;
import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.collections.ArenaBuilderInstances;
import com.Patane.Battlegrounds.playerData.Inventories;
import com.Patane.Battlegrounds.util.util;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;

public class ArenaBuilder{
	Plugin plugin;
	Player creator;
	String arenaName;
	ArenaHandler arena;
	ItemStack[] savedInventory;
	ArenaEditListeners editListener;
	DyeColor playerColor = DyeColor.GRAY, lobbyColor = DyeColor.LIGHT_BLUE, creatureColor = DyeColor.RED, spectatorColor = DyeColor.LIME;
	
	public ArenaBuilder(Plugin plugin, Player player, String arenaName){
		this.plugin		= plugin;
		this.creator	= player;
		this.arenaName	= arenaName;
		ArenaBuilderInstances.add(this);
	}
	public ArenaBuilder(Plugin plugin, Player player, ArenaHandler arena){
		this.plugin		= plugin;
		this.creator	= player;
		this.arena		= arena;
		this.arenaName	= arena.getName();
		ArenaBuilderInstances.add(this);
	}
	public Player getCreator() {
		return creator;
	}
	public String getCreatorName() {
		return creator.getDisplayName();
	}
	public ArenaHandler getArena() {
		return arena;
	}
	/*
	 * HOW BUILDER WILL WORK:
	 * on command /bg create [arenaname]:
	 *  - checks if arenaname isnt taken
	 *  - run (below) saves the region and arena object
	 *  - saves arena location in arenas.yml with following format:
	 *  	arenas:
	 *  		[arenaname]:
	 *  			world:
	 *  			poly:
	 *  				minY: 50
	 *  				maxY: 58
	 *  				vectors:
	 *  					1: 120,1372 <--- x,z
	 *  					2: 126,1401
	 *  						etc.
	 *  		OR
	 *  			cuboid:
	 *  				min: 23,20,534 <--- x,y,z
	 *  				max: 23,65,343
	 *  - creators (player) inventory gets saved
	 *  - they are given 4 coloured wools named (player spawn, creature spawn, lobby spawn and spectator spawn)
	 *  - they are instructed how placing each chooses its respective location(s)
	 *  
	 *  - listeners are created for detecting when blocks named above are places/destroyed
	 * on item pickup:
	 *  - cancel if player = creator
	 *  
	 * on block place:
	 *  - checks player = creator
	 * 	- checks blocks name (name in players hand)
	 *  - cancels block removal in inventory
	 *  - checks if air is above. if so, continue.
	 *  - checks if block below is not another location in the arraylist
	 *  - places the block
	 *  - adds the location to a temporary array of the type of spawn
	 *  - also adds the pitch (horizontal) of the player and sets yaw (vertical) to be flat
	 * 
	 * on block destroy:
	 * 	- check player = creator
	 *  - checks if location of block break is in any lists
	 *  - if so, remove that entry from list
	 *  
	 * on /bg save
	 *  - grabs all temporary arraylists and saves each as the arena object's arraylists
	 *  - saves in arenas.yml as:
	 *  	arenas:
	 *  		[arenaname]:
	 *  			world:
	 *  			cuboid:
	 *  			spawns:
	 *  				lobby:
	 *  					1: x,y,z,pitch,yaw
	 *  					2: 24,52,12,45,45
	 *  				player:
	 *  					1: 12,32,12,45,45
	 *  					2: 23,12,32,,
	 *  					3: 23,12,43,,
	 *  					4: 43,23,65,,
	 *  				creature:
	 *  					1: 23,43,34,,
	 *  					2: 54,23,76,,
	 *  					3: 65,34,74,,
	 *  				spectator:
	 *  					1: 32,34,65,,
	 *  
	 *  
	 *  
	 */
	
	
	/**
	 *  Creates arena!
	 *  Uses either a polygonal selection or a cuboid selection to save the arena in arenas.yml
	 *  Then sends creator to edit spawns.
	 *  
	 */
	@SuppressWarnings("deprecation")
	public void createArena() {
		WorldEditPlugin worldEditPlugin = (WorldEditPlugin) plugin.getServer().getPluginManager().getPlugin("WorldEdit");
		if(worldEditPlugin == null){
			Messenger.send(creator, "&cAt this time, you need WorldEdit installed to create arenas.");
			return;
		}
		Selection selection = worldEditPlugin.getSelection(creator);
		if(selection == null){
			Messenger.send(creator, "&cYou must make a selection with worldedit before using this command!");
			return;
		}
		World world = selection.getWorld();
		AbstractRegion selectedRegion;
		if (selection instanceof Polygonal2DSelection){
			Polygonal2DSelection polySel = (Polygonal2DSelection) selection;
			int minY = polySel.getNativeMinimumPoint().getBlockY();
			int maxY = polySel.getNativeMaximumPoint().getBlockY();
			selectedRegion = new Polygonal2DRegion (BukkitUtil.getLocalWorld(world), polySel.getNativePoints(), minY, maxY);
		} else if (selection instanceof CuboidSelection) {
			BlockVector min = selection.getNativeMinimumPoint().toBlockVector();
			BlockVector max = selection.getNativeMaximumPoint().toBlockVector();
			selectedRegion = new CuboidRegion (BukkitUtil.getLocalWorld(world), min, max);
		} else {
			Messenger.send(creator, "&cYou must make either a cuboid or polygonal selection with worldedit!");
			return;
		}
		ArenaHandler newArena = new ArenaHandler(plugin, arenaName, world, selectedRegion);
		this.arena = newArena;
		ArenaYML.saveRegion(arenaName, world, selectedRegion);
		editSpawns();
	}
	/**
	 * Where the creator can add/remove spawns.
	 */
	public void editSpawns() {
		Messenger.send(creator, "&2===== &aSpawn Editor! &2=====");
		Messenger.sendRaw(creator, "&a Place/Break the given wool blocks to add/remove their respective spawn locations.");
		Messenger.sendRaw(creator, "&2NOTE: The direction the player places the block is the direction entities will face when being teleported/spawned.");
		arena.setEditMode(true);
		// create listener
		editListener = new ArenaEditListeners(plugin, this, arena);
		plugin.getServer().getPluginManager().registerEvents(editListener, plugin);
		// save creators inventory
		Inventories.save(creator);
		
		refreshPlayerItems("All");
		
		refreshSpawnBlocks();
	}
	
	public void refreshPlayerItems(String spawnType){
		creator.getInventory().clear();
		creator.getInventory().addItem(createSpawnerItemStack(ChatColor.GRAY + "Player Spawn", playerColor, arena.getPlayerSpawns()));
		creator.getInventory().addItem(createSpawnerItemStack(ChatColor.BLUE + "Lobby Spawn", lobbyColor, arena.getLobbySpawns()));
		creator.getInventory().addItem(createSpawnerItemStack(ChatColor.DARK_RED + "Creature Spawn", creatureColor, arena.getCreatureSpawns()));
		creator.getInventory().addItem(createSpawnerItemStack(ChatColor.DARK_GREEN + "Spectator Spawn", spectatorColor, arena.getSpectatorSpawns()));
	}

	public ItemStack createSpawnerItemStack(String displayName, DyeColor color, ArrayList<Location> spawns){
		Wool wool = new Wool();
		wool.setColor(color);
		ItemStack item = wool.toItemStack();
		item.setAmount(spawns.size() < 1 ? 1 : spawns.size());
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(displayName);
		item.setItemMeta(itemMeta);
		return item;
	}
	public boolean save() {
		// save all blocks/spawn locations
		ArenaYML.saveSpawns(arenaName, "Player" , arena.getPlayerSpawns());
		ArenaYML.saveSpawns(arenaName, "Lobby" , arena.getLobbySpawns());
		ArenaYML.saveSpawns(arenaName, "Creature" , arena.getCreatureSpawns());
		ArenaYML.saveSpawns(arenaName, "Spectator" , arena.getSpectatorSpawns());
		// return original inventory
		if(!Inventories.restore(creator))
			Messenger.warning("Failed to restore " + creator.getDisplayName() + "'s inventory.");
		// remove all wool from spawn locations
		removeSpawnBlocks();
		// return original blocks to each spawn location (Just have air for now)
		// set arenaEditMode to false
		arena.setEditMode(false);
		// remove this object from instances
		ArenaBuilderInstances.remove(this);
		HandlerList.unregisterAll(editListener);
		ArrayList<String> emptySpawns = arena.getEmptySpawnLists();
		if(!emptySpawns.isEmpty()){
			StringJoiner emptySpawnString = new StringJoiner(", ");
			for (String spawn : emptySpawns){
				emptySpawnString.add(spawn);
			}
			Messenger.send(creator, "&cWarning! Missing spawns for: &7" + emptySpawnString);
			Messenger.sendRaw(creator, "&cType '/bg edit [arena] spawns' to re-edit spawns.");
		}
		Messenger.send(creator, "&aArena '&7" + arenaName + "&a' succesfully saved!");
		return true;
	}
	public void refreshSpawnBlocks(){
		try {
			Block block;
			for(Location playerSpawn : arena.getPlayerSpawns()){
				block = arena.getWorld().getBlockAt(playerSpawn.getBlockX(),playerSpawn.getBlockY(),playerSpawn.getBlockZ());
				util.setColouredWool(block, playerColor);
			}
			for(Location lobbySpawn : arena.getLobbySpawns()){
				block = arena.getWorld().getBlockAt(lobbySpawn.getBlockX(),lobbySpawn.getBlockY(),lobbySpawn.getBlockZ());
				util.setColouredWool(block, lobbyColor);
			}
			for(Location creatureSpawn : arena.getCreatureSpawns()){
				block = arena.getWorld().getBlockAt(creatureSpawn.getBlockX(),creatureSpawn.getBlockY(),creatureSpawn.getBlockZ());
				util.setColouredWool(block, creatureColor);
			}
			for(Location spectatorSpawn : arena.getSpectatorSpawns()){
				block = arena.getWorld().getBlockAt(spectatorSpawn.getBlockX(),spectatorSpawn.getBlockY(),spectatorSpawn.getBlockZ());
				util.setColouredWool(block, spectatorColor);
			}
		} catch (NullPointerException e){}
	}
	public void removeSpawnBlocks(){
		Block block;
		for(Location playerSpawn : arena.getPlayerSpawns()){
			block = arena.getWorld().getBlockAt(playerSpawn.getBlockX(),playerSpawn.getBlockY(),playerSpawn.getBlockZ());
			block.setType(Material.AIR);
			Location particleLocation = new Location(playerSpawn.getWorld(), playerSpawn.getX(), playerSpawn.getY()+0.5, playerSpawn.getZ());
			particleLocation.getWorld().spawnParticle(Particle.CLOUD, particleLocation, 10, 0, 0, 0, 0.05);
		}
		for(Location lobbySpawn : arena.getLobbySpawns()){
			block = arena.getWorld().getBlockAt(lobbySpawn.getBlockX(),lobbySpawn.getBlockY(),lobbySpawn.getBlockZ());
			block.setType(Material.AIR);
			Location particleLocation = new Location(lobbySpawn.getWorld(), lobbySpawn.getX(), lobbySpawn.getY()+0.5, lobbySpawn.getZ());
			particleLocation.getWorld().spawnParticle(Particle.CLOUD, particleLocation, 10, 0, 0, 0, 0.05);
		}
		for(Location creatureSpawn : arena.getCreatureSpawns()){
			block = arena.getWorld().getBlockAt(creatureSpawn.getBlockX(),creatureSpawn.getBlockY(),creatureSpawn.getBlockZ());
			block.setType(Material.AIR);
			Location particleLocation = new Location(creatureSpawn.getWorld(), creatureSpawn.getX(), creatureSpawn.getY()+0.5, creatureSpawn.getZ());
			particleLocation.getWorld().spawnParticle(Particle.CLOUD, particleLocation, 10, 0, 0, 0, 0.05);
		}
		for(Location spectatorSpawn : arena.getSpectatorSpawns()){
			block = arena.getWorld().getBlockAt(spectatorSpawn.getBlockX(),spectatorSpawn.getBlockY(),spectatorSpawn.getBlockZ());
			block.setType(Material.AIR);
			Location particleLocation = new Location(spectatorSpawn.getWorld(), spectatorSpawn.getX(), spectatorSpawn.getY()+0.5, spectatorSpawn.getZ());
			particleLocation.getWorld().spawnParticle(Particle.CLOUD, particleLocation, 10, 0, 0, 0, 0.05);
		}
	}
	public boolean addPlayerSpawn(Location location){
		 return arena.getPlayerSpawns().add(location);
	}
	public boolean removePlayerSpawn(Location location){
		 return arena.getPlayerSpawns().remove(location);
	}
	public boolean addLobbySpawn(Location location){
		 return arena.getLobbySpawns().add(location);
	}
	public boolean removeLobbySpawn(Location location){
		 return arena.getLobbySpawns().remove(location);
	}
	public boolean addCreatureSpawn(Location location){
		 return arena.getCreatureSpawns().add(location);
	}
	public boolean removeCreatureSpawn(Location location){
		 return arena.getCreatureSpawns().remove(location);
	}
	public boolean addSpectatorSpawn(Location location){
		 return arena.getSpectatorSpawns().add(location);
	}
	public boolean removeSpectatorSpawn(Location location){
		 return arena.getSpectatorSpawns().remove(location);
	}

}
