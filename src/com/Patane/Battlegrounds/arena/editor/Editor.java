package com.Patane.Battlegrounds.arena.editor;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.ArenaYML;
import com.Patane.Battlegrounds.arena.editor.initialize.Initialize;
//import com.Patane.Battlegrounds.arena.editor.spawn.SpawnEditor;
import com.Patane.Battlegrounds.arena.standby.Standby;
import com.Patane.Battlegrounds.util.util;
import com.sk89q.worldedit.regions.AbstractRegion;

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
	 *  Creates arena with ground region.
	 *  
	 */
	public void createArena() {
		AbstractRegion region = util.getAbstractRegion(plugin, creator);
		if(region == null)
			return;
		World world = creator.getWorld();
		Arena newArena = new Arena(plugin, arenaName, world, region);
		Messenger.send(creator, "&aArena &7" + newArena.getName() + "&a created in world &7" + newArena.getWorld().getName());
		this.arena = newArena;
		arena.setMode(this);
		
		if(ArenaYML.saveRegion(arenaName, region, "Ground"))
			newEditorType(new Initialize(plugin, arena, creator, this));
	}
	public EditorType getEditorType(){
		return editorType;
	}
	public void newEditorType(EditorType editorType) {
		try{
			this.editorType.save();
			this.editorType.getListener().unregister();
		} catch (NullPointerException e){}
		this.editorType = editorType;
		this.listener	= editorType.getListener();
		this.editorType.initilize();
	}
	@Override
	public void sessionOver() {
		editorType.save();
		super.sessionOver(new Standby(plugin, arena));
	}
}
