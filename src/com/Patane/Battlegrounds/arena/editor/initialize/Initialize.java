package com.Patane.Battlegrounds.arena.editor.initialize;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorInfo;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.editor.EditorType;
import com.Patane.Battlegrounds.playerData.Inventories;

@EditorInfo(
		name = "initilize", permission = ""
	)
public class Initialize implements EditorType{
	Arena arena;
	String arenaName;
	Player creator;
	EditorListeners listener;
	
	public Initialize(Plugin plugin, Arena arena, Player creator, Editor editor){
		this.arena 		= arena;
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
		this.listener	= new InitializeListeners(plugin, arena, this, editor);
	}
	@Override
	public void initilize() {
		Messenger.send(creator, "&2Create your &7Lobby Region&2:"
							+ "\n&a Make a region selection with worldedit and place"
							+ "\n&a the given anvil within it to save your Lobby Region!");
		if(!Inventories.isSaved(creator))
			Inventories.save(creator);
		
		refreshPlayerItems();
	}
	public void refreshPlayerItems() {
		creator.getInventory().clear();
		creator.getInventory().addItem(new ItemStack(Material.WOOD_AXE));
		creator.getInventory().addItem(new ItemStack(Material.ANVIL));
	}
	@Override
	public void save() {
		Inventories.restore(creator);
		if(arena.hasLobby()){
			Messenger.send(creator, "&aSaved &7Lobby Region &afor Arena &7" + arenaName + "&a.");
			return;
		}
		Messenger.send(creator, "&cFailed to save &7Lobby Region &cfor Arena &7" + arenaName + "&c."
							+ "\n&c Type &7/bg edit [arena] lobby &cto re-edit &7Lobby Region");
	}

	@Override
	public EditorListeners getListener() {
		return listener;
	}


	
}
