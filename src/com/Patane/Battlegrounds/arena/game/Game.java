package com.Patane.Battlegrounds.arena.game;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Battlegrounds;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.standby.Standby;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionFactory;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.registry.WorldData;

public class Game extends Standby{
	
	boolean spawningCreature;
	
	RoundHandler roundHandler;
	
	public Game (Plugin plugin, Arena arena) {
		super(plugin, arena, null);

		this.listener		= new GameListeners(plugin, arena, this);
		this.roundHandler 	= new RoundHandler(plugin, this);
		this.colorCode	= "&e";
		this.defaultLocations = arena.getGameSpawns();
		
		File f = new File(Battlegrounds.get().getDataFolder(), "/ArenaData/"+arena.getName()+"/grounds");
		Closer closer = Closer.create();
        try {
            FileInputStream fis = closer.register(new FileInputStream(f));
            BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
            ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(bis);
            
            WorldData worldData = BukkitUtil.getLocalWorld(arena.getWorld()).getWorldData();
            Clipboard clipboard = reader.read(worldData);
            ClipboardHolder holder = new ClipboardHolder(clipboard, worldData);
            EditSessionFactory test1 = WorldEdit.getInstance().getEditSessionFactory();
            @SuppressWarnings("deprecation")
			EditSession test = test1.getEditSession(BukkitUtil.getLocalWorld(arena.getWorld()), 999999999);
            Operation operation = holder.createPaste(test, worldData)
                    .to(arena.getGround().getMinimumPoint())
                    .build();
            Operations.completeLegacy(operation);
            Messenger.broadcast("PASTED");
//            log.info(player.getName() + " loaded " + f.getCanonicalPath());
//            player.print(filename + " loaded. Paste it with //paste");
        } catch (IOException | MaxChangedBlocksException e) {
        	e.printStackTrace();
//            player.printError("Schematic could not read or it does not exist: " + e.getMessage());
//            log.log(Level.WARNING, "Failed to load a saved clipboard", e);
        } finally {
            try {
                closer.close();
            } catch (IOException ignored) {
            }
        }
		for(String playerName : arena.getPlayers())
			addPlayer(Bukkit.getPlayerExact(ChatColor.stripColor(playerName)));
		
		this.defaultLocations = arena.getSpectatorSpawns();
		roundHandler.startRound();
	}
	@Override
	protected void initilizeMessage(){
		Messenger.arenaCast(arena, "&cGame Started. &cPrepare for battle!");
		// Maybe show some basic game information eg. max rounds (if any) or description??
		// Also leave room to allow for custom game-start msg
	}
	
	public RoundHandler getRoundHandler(){
		return roundHandler;
	}
	public void setSpawning(boolean spawning){
		this.spawningCreature = spawning;
	}
	public boolean getSpawning(){
		return spawningCreature;
	}
//	@Override
//	public boolean randomTeleport(Player player, ArrayList<Location> locations){
//		if(!arena.getSettings().SPECTATE_DEATH){
//			/*
//			 * Need to change this 'removePlayer' to a soft remove that removes the physical person
//			 * but keeps the player in the arena data for scoreboard and such. This data then gets
//			 * removed when game session ends.
//			 */
//			removePlayer(player.getDisplayName(), false);
//			return true;
//		}
//		return super.randomTeleport(player, locations);
//	}
	@Override
	public void updateExp(){
		setAllLevel(roundHandler.getRoundNo());
		if(roundHandler.getTotalMobs() == 0)
			setAllExp(0);
		else
			setAllExp((float)(roundHandler.getTotalMobs() 
					- roundHandler.getAmountMobs())/roundHandler.getTotalMobs());
	}
	@Override
	public boolean addPlayer(Player player){
		if(randomTeleport(player, arena.getGameSpawns())){
			updateExp();
			return true;
		}
		return false;
	}
	/**
	 * @param player to check and kill
	 * @return true if player was killed in HashMap
	 */
	public boolean playerKilled(Player player){
		if(arena.getPlayers().contains(player.getDisplayName())){
			playerExit(player);
			Messenger.arenaCast(arena, "&6" + player.getPlayerListName() + " has been eliminated!");
			return true;
		}
		return false;
	}
	public void finalRoundEnd(){
		for(String playerName : arena.getPlayers()){
			Player player = Bukkit.getPlayerExact(playerName);
			playerExit(player);
		}
		// cleaning up mobs
		roundHandler.stopAllTasks();
		roundHandler.clearMobs();
		if(arena.getSettings().GLOBAL_END_ANNOUNCE){
			Messenger.broadcast("&2The game in &a" + arena.getName() + "&2 just finished! They made it past the final round (&a" + roundHandler.getRoundNo() + "&2)!"
							+ "\n&7 Type /bg join " + arena.getName() + " to start a new game at that arena!");
		}
	}
	private void playerExit(Player player){
		if(arena.hasPlayer(player))
			arena.putPlayer(player, false);
		if(arena.getSettings().SPECTATE_DEATH)
			arena.joinSpectator(player);
		else
			removePlayer(player.getDisplayName(), false);
	}
	@Override
	public boolean checkSessionOver(){
		// if players does not contain a player that is alive=true
		if(!arena.anyPlayers(true) || arena.getPlayers().isEmpty())
			return true;
		return false;
	}
	@Override
	public void sessionOver() {
		Messenger.arenaCast(arena, "&aAll players &2have been eliminated!");
		
		// running through players
		for(String selectedPlayer : arena.getPlayers()){
			arena.removePlayerFromList(selectedPlayer);
		}
		// cleaning up mobs
		roundHandler.stopAllTasks();
		roundHandler.clearMobs();
		if(arena.getSettings().GLOBAL_END_ANNOUNCE){
			Messenger.broadcast("&2The game in &a" + arena.getName() + "&2 just finished! They made it to round &a" + roundHandler.getRoundNo() + "&2!"
							+ "\n&7 Type /bg join " + arena.getName() + " to start a new game at that arena!");
		}
		sessionOver(new Standby(plugin, arena));
	}
}
