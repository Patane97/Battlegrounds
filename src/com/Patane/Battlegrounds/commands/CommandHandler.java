package com.Patane.Battlegrounds.commands;

import java.util.StringJoiner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Battlegrounds;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.ArenaHandler;
import com.Patane.Battlegrounds.arena.builder.*;
import com.Patane.Battlegrounds.collections.*;
import com.Patane.Battlegrounds.game.GameHandler;

public class CommandHandler implements CommandExecutor{
	
	Battlegrounds plugin;
	
	public CommandHandler(Battlegrounds battlegrounds) {
		this.plugin = battlegrounds;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String command = (args.length > 0 ? args[0] : "");
		// checking if sender is from console
		Player target = ((sender instanceof Player) ? (Player) sender : null);
		if (target == null){
			Messenger.send(sender, "Command cannot be executed from console!");
			return false;
		}
		String arenaName;
		ArenaHandler arena;
		GameHandler game;
		switch(command){
		case "join":
			arenaName = (args.length > 1 ? args[1] : null);
			// checks if game name has been specified
			if (arenaName == null) {
				Messenger.send(sender, "Please specify an arena name! Eg. /bg join [arena]");
				break;
			}
			arena = Arenas.get(arenaName);
			// checks if arena is currently running a game and adds player if it is
			/*
			 * CHANGE THIS TO BE THE LOBBY, NOT THE GAME (so players join if the lobby is open but cant join mid-game)
			 */
			if (arena.isInGame()){
				GameInstances.getGame(arena).addPlayer(target);
				break;
			}
			
			// checks if player is in a current game
			if (ActivePlayers.getGame(target) != null){
				Messenger.send(sender, "Please leave your current game before starting a new one. Type /bg leave to leave your current game");
				break;
			}
			// Otherwise, creates a new GameHandler!
			game = new GameHandler(Arenas.get(arenaName));
			game.addPlayer(target);
			break;
			
		case "leave":
			game = ActivePlayers.getGame(target);
			try{
				game.playerLeave(target, true);
			
			} catch (Exception e){
				Messenger.send(sender, "You must be in a game to leave one!");
			}
			break;
		case "list":
			StringJoiner list = new StringJoiner(",");
			for(String name : Arenas.listArenaNames()){
				list.add(name);
			}
			Messenger.send(sender, "Active Arenas: " + list.toString());
			break;
		case "create":
			arenaName = (args.length > 1 ? args[1] : null);
			if(arenaName == null){
				Messenger.send(sender, "You must give the arena a name. Eg. /bg create [arena name]");
				break;
			}
			for(String name : Arenas.listArenaNames()){
				if(arenaName.equals(name));{
					Messenger.send(sender, "'" + arenaName + "' is already an arena name. Please choose another");
					break;
				}
			}
			ArenaBuilderListeners arenaBuilderListener = new ArenaBuilderListeners(plugin);
			
			ArenaBuilder newArena = new ArenaBuilder(plugin, target, args[1], arenaBuilderListener, true);
			Thread newArenaT = new Thread(newArena);
			// registering the arena builder listener
			plugin.getServer().getPluginManager().registerEvents(arenaBuilderListener, plugin);
			
			newArenaT.start();
			break;
		case "remove":
			arenaName = (args.length > 1 ? args[1] : null);
			if(arenaName == null){
				Messenger.send(sender, "You must specify an arena name. Eg. /bg remove [arena name]");
				break;
			}
			arena = Arenas.get(args[1]);
			if(arena != null && Arenas.remove(arena))
				Messenger.send(sender, "Arena '" + arena.getName() + "' removed!");
			else
				Messenger.send(sender, "There is no arena with that name");
				
			break;
		default:
			Messenger.send(sender, "Please specify a Battleground Command!");
		}
		return true;
	}

}
