package com.Patane.Battlegrounds.arena.modes.editor;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.ArenaYML;
import com.Patane.Battlegrounds.arena.modes.Standby;
import com.Patane.Battlegrounds.arena.modes.editor.types.SpawnEditor;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;

public class Editor extends Standby{
	String arenaName;
	Player creator;
	EditorType editorType;
	/**
	 * Constructor for a newly created arena
	 * 
	 * @param plugin
	 * @param arenaName
	 * @param creator
	 */
	public Editor(Plugin plugin, String arenaName, Player creator){
		this.plugin 	= plugin;
		this.arenaName 	= arenaName;
		this.creator 	= creator;
		//this.listener 	= new EditorListeners(plugin, arena);
		createArena();
	}
	/**
	 * Constructor for editing an arena
	 * 
	 * @param plugin
	 * @param arena
	 */
	public Editor(Plugin plugin, Arena arena, Player creator){
		super(plugin, arena);
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
	}
	public Player getCreator(){
		return creator;
	}
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
			Messenger.arenaCast(arena, "&cYou must make either a cuboid or polygonal selection with worldedit!");
			return;
		}
		Arena newArena = new Arena(plugin, arenaName, world, selectedRegion);
		this.arena = newArena;
		arena.setMode(this);
		ArenaYML.saveRegion(arenaName, world, selectedRegion);
		
		newEditorType(new SpawnEditor(plugin, arena, creator, this));
	}
	public EditorType getEditorType(){
		return editorType;
	}
	public void newEditorType(EditorType editorType) {
		this.editorType = editorType;
		this.listener	= editorType.getListener();
	}
	@Override
	public void sessionOver() {
		editorType.save();
		super.sessionOver(new Standby(plugin, arena));
	}
}
