package com.Patane.Battlegrounds.arena.builder;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

public class ArenaBuilder implements Runnable{
	Plugin plugin;
	Player creator;
	
	ArenaBuilder(Plugin plugin, Player player){
		this.plugin		= plugin;
		this.creator	= player;
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
	 *  		arenaname:
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
	@Override
	public void run() {
		WorldEditPlugin worldEditPlugin = (WorldEditPlugin) plugin.getServer().getPluginManager().getPlugin("WorldEdit");
		if(worldEditPlugin == null){
			Messenger.send(creator, "At this time, you need WorldEdit installed to create arenas.");
			return;
		}
		Selection selection = worldEditPlugin.getSelection(creator);
		if (selection instanceof Polygonal2DSelection){
			//Polygonal2DRegion polyRegion = new Polygonal2DRegion((World) selection.getWorld(), ((Polygonal2DSelection) selection).getNativePoints(), selection.getNativeMinimumPoint().getBlockY(), selection.getNativeMaximumPoint().getBlockY());
			Polygonal2DSelection polySel = (Polygonal2DSelection) selection;
			int minY = polySel.getNativeMinimumPoint().getBlockY();
			int maxY = polySel.getNativeMaximumPoint().getBlockY();
			Polygonal2DRegion polyRegion = new Polygonal2DRegion ((World) polySel.getWorld(), polySel.getNativePoints(), minY, maxY);
			
		} else if (selection instanceof CuboidSelection) {
			BlockVector min = selection.getNativeMinimumPoint().toBlockVector();
			BlockVector max = selection.getNativeMaximumPoint().toBlockVector();
			CuboidRegion cuboidRegion = new CuboidRegion ((World) selection.getWorld(), min, max);
		}
	}

}
