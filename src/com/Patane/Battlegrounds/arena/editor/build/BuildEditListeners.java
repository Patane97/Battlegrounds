package com.Patane.Battlegrounds.arena.editor.build;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Sign;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.collections.Classes;

public class BuildEditListeners extends EditorListeners{
	BuildEditor buildEditor;
	public BuildEditListeners(Plugin plugin, Arena arena, BuildEditor buildEditor, Editor editor) {
		super(plugin, arena, editor);
		this.buildEditor = buildEditor;
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		String playerName = player.getDisplayName();
		if(playerName.equals(creatorName)){
			Block block = event.getBlockPlaced();
			if(spawnBelow(block.getLocation())){
				event.setCancelled(true);
				Messenger.send(player, "&cBlocks cannot be placed above spawn Locations");
				return;
			}
			if(arena.isWithin(block) != 0 && event.isCancelled())
				event.setCancelled(false);
		}		
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		String playerName = player.getDisplayName();
		Block block = event.getBlock();
		if(playerName.equals(creatorName) && 
				arena.isWithin(block) != 0 && 
				event.isCancelled()){
				event.setCancelled(false);
		}
	}
	@EventHandler
	public void onSignChange(SignChangeEvent event){
		Player player = event.getPlayer();
		String playerName = player.getDisplayName();
		if(playerName.equals(creatorName)){
			Pattern p = Pattern.compile("<(.+)>");
			String className;
			for(String line : event.getLines()){
				Matcher m = p.matcher(line);
				if(m.matches()){
					Block signBlock = event.getBlock();
					Sign sign = (Sign) signBlock.getState().getData();
					signBlock.setType(Material.AIR);
					Messenger.send(player, "Detected!");
					className = m.group();
					className = Classes.convertName(className);
					if(className != null){
						Messenger.send(player, "Class: " + className);
						BGClass newClass = Classes.grab(className);
						ItemFrame newClassFrame = (ItemFrame) arena.getWorld().spawnEntity(signBlock.getLocation(), EntityType.ITEM_FRAME);
						newClassFrame.setFacingDirection(sign.getFacing());
						newClassFrame.setItem(newClass.getIcon());
						newClassFrame.setInvulnerable(true);
					}
				}
				
			}
		}
	}

}
