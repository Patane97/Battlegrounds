package com.Patane.Battlegrounds.arena.editor.spawn;

import java.util.ArrayList;
import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.ArenaYML;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorInfo;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.editor.EditorType;
import com.Patane.Battlegrounds.playerData.Inventories;
import com.Patane.Battlegrounds.util.util;

@EditorInfo(
		name = "spawn", permission = ""
	)
public class SpawnEditor implements EditorType{
	Arena arena;
	String arenaName;
	Player creator;
	EditorListeners listener;
	
	DyeColor gameColor 		= DyeColor.GRAY, 
			lobbyColor 		= DyeColor.LIGHT_BLUE, 
			creatureColor 	= DyeColor.RED, 
			spectatorColor 	= DyeColor.LIME;
	
	public SpawnEditor(Plugin plugin, Arena arena, Player creator, Editor editor){
		this.arena 		= arena;
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
		this.listener 	= new SpawnEditListeners(plugin, arena, this, editor);
	}

	@Override
	public void initilize() {
		Messenger.send(creator, "&2Add/Remove Arena &7Spawns&2:"
							+ "\n&a Place/Break the given wool blocks to add/remove their respective spawn locations."
							+ "\n&7&o NOTE: The direction the player places the block is the direction entities will face when being teleported/spawned."
							+ "\n&a Type &7/bg save &awhen you're done!");
		if(!Inventories.isSaved(creator))
			Inventories.save(creator);
		
		refreshPlayerItems();
		
		refreshSpawnBlocks();
	}
	/**
	 * Refreshes the creators editor inventory
	 */
	public void refreshPlayerItems(){
		creator.getInventory().clear();
		creator.getInventory().addItem(createSpawnerItemStack(ChatColor.GRAY + "Game Spawn", gameColor, arena.getGameSpawns()));
		creator.getInventory().addItem(createSpawnerItemStack(ChatColor.BLUE + "Lobby Spawn", lobbyColor, arena.getLobbySpawns()));
		creator.getInventory().addItem(createSpawnerItemStack(ChatColor.DARK_RED + "Creature Spawn", creatureColor, arena.getCreatureSpawns()));
		creator.getInventory().addItem(createSpawnerItemStack(ChatColor.DARK_GREEN + "Spectator Spawn", spectatorColor, arena.getSpectatorSpawns()));
	}
	/**
	 * @param name of the item
	 * @param color of wool
	 * @param spawns linked to the items
	 * @return ItemStack of created woolen spawn item
	 */
	public ItemStack createSpawnerItemStack(String name, DyeColor color, ArrayList<Location> spawns){
		Wool wool = new Wool();
		wool.setColor(color);
		ItemStack item = wool.toItemStack();
		item.setAmount(spawns.size() < 1 ? 1 : spawns.size());
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		return item;
	}
	/**
	 *  Refreshes the woolen spawn blocks placed in the world
	 */
	public void refreshSpawnBlocks(){
		try {
			Block block;
			for(Location gameSpawn : arena.getGameSpawns()){
				block = arena.getWorld().getBlockAt(gameSpawn.getBlockX(),gameSpawn.getBlockY(),gameSpawn.getBlockZ());
				util.setColouredWool(block, gameColor);
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
		} catch (NullPointerException e){
			Messenger.severe("Spawn blocks failed to refresh!");
			e.printStackTrace();
		}
	}
	public boolean addGameSpawn(Location location){
		 return arena.getGameSpawns().add(location);
	}
	public boolean removePlayerSpawn(Location location){
		 return arena.getGameSpawns().remove(location);
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
	@Override
	public EditorListeners getListener(){
		return listener;
	}
	@Override
	public void save() {
		ArenaYML.saveAllSpawns(arenaName);
		arena.clearSpawnBlocks(true);
		Inventories.restore(creator);
		ArrayList<String> emptySpawns = arena.getEmptySpawnLists();
		if(!emptySpawns.isEmpty()){
			StringJoiner emptySpawnString = new StringJoiner("&c, ");
			for(String spawn : emptySpawns)
				emptySpawnString.add("&c" + spawn);
			Messenger.send(creator, "&cWarning! Missing spawns for " + emptySpawnString.toString() 
								+ "\n&cType &7/bg edit [arena] spawns&c to re-edit spawns");
		}
		Messenger.send(creator, "&aSaved &7All Spawns &afor Arena &7" + arenaName + "&a.");
	}
}
