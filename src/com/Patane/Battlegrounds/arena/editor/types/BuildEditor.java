package com.Patane.Battlegrounds.arena.editor.types;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.material.Sign;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.classes.BGClass;
import com.Patane.Battlegrounds.arena.editor.Editor;
import com.Patane.Battlegrounds.arena.editor.EditorInfo;
import com.Patane.Battlegrounds.arena.editor.EditorListeners;
import com.Patane.Battlegrounds.arena.editor.EditorType;
import com.Patane.Battlegrounds.collections.Classes;
import com.Patane.Battlegrounds.util.RelativePoint;

@EditorInfo(
		name = "build", permission = ""
	)
public class BuildEditor implements EditorType{
	Arena arena;
	String arenaName;
	Player creator;
	EditorListeners listener;
	public BuildEditor(Plugin plugin, Arena arena, Player creator, Editor editor){
		this.arena 		= arena;
		this.arenaName 	= arena.getName();
		this.creator 	= creator;
		this.listener	= new Listener(plugin, arena, this, editor);
		
	}
	@Override
	public void initilize() {
		Messenger.send(creator, "&2Build/Break blocks within &7Ground &2and &7Lobby Regions&2:"
							+ "\n&a You can now break and place blocks in this arena's ground and lobby regions."
							+ "\n&a Type &7/bg save &ato save arena!");
	}
	@Override
	public void save() {
		Messenger.send(creator, "&aSaved &7All Blocks &awithin Arena &7" + arenaName + "&a.");
	}

	@Override
	public EditorListeners getListener() {
		return listener;
	}
	
	public class Listener extends EditorListeners{
		BuildEditor buildEditor;
		public Listener(Plugin plugin, Arena arena, BuildEditor buildEditor, Editor editor) {
			super(plugin, arena, editor);
			this.buildEditor = buildEditor;
		}
		@Override
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockBreak(BlockBreakEvent event){
			if(arena.isWithin(event.getBlock()) != RelativePoint.OUTSIDE){
				Block block = event.getBlock();
				Player player = event.getPlayer();
				UUID playerUUID = player.getUniqueId();
				if(!playerUUID.equals(creatorUUID)){
					event.setCancelled(true);
					return;
				}
				if (spawnAt(block.getLocation())){
					event.setCancelled(true);
					return;
				}
				event.setCancelled(false);
			}
		}
		@Override
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockPlace(BlockPlaceEvent event){
			if(arena.isWithin(event.getBlockPlaced()) != RelativePoint.OUTSIDE){
				Block block = event.getBlockPlaced();
				Player player = event.getPlayer();
				UUID playerUUID = player.getUniqueId();
				if(playerUUID.equals(creatorUUID)){
					event.setCancelled(false);
					return;
				}
				if (spawnAt(block.getLocation())){
					event.setCancelled(true);
					Messenger.send(player, "&cYou can not place a block on a spawn location!");
					return;
				} else if(spawnBelow(block.getLocation())){
					event.setCancelled(true);
					Messenger.send(player, "&cYou can not place a block above a spawn location!");
					return;
				}
				event.setCancelled(false);
			}		
		}
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onHangingBreak(HangingBreakByEntityEvent event){
			if(arena.isWithin(event.getEntity()) != RelativePoint.OUTSIDE){
				event.setCancelled(false);
			}
		}
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onHangingPlace(HangingPlaceEvent event){
			if(arena.isWithin(event.getEntity()) != RelativePoint.OUTSIDE){
				event.setCancelled(false);
			}
		}
		@EventHandler(priority = EventPriority.HIGHEST)
	    public void onItemFrameHit(EntityDamageEvent event) {
			if(arena.isWithin(event.getEntity()) != RelativePoint.OUTSIDE){
				if (event.getEntity() instanceof ItemFrame) {
					event.setCancelled(false);
		        }
			}
	    }
		@EventHandler
		public void onSignChange(SignChangeEvent event){
			Player player = event.getPlayer();
			UUID playerUUID = player.getUniqueId();
			if(playerUUID.equals(creatorUUID)){
				Pattern p = Pattern.compile("<(.+)>");
				String className;
				for(String line : event.getLines()){
					Matcher m = p.matcher(line);
					if(m.matches()){
						Block signBlock = event.getBlock();
						Sign sign = (Sign) signBlock.getState().getData();
						signBlock.setType(Material.AIR);
						className = m.group();
						className = Classes.partlyContains(className);
						if(className != null){
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
}
