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
import com.Patane.Battlegrounds.game.GameListeners;
import com.Patane.Battlegrounds.lobby.LobbyHandler;
import com.Patane.Battlegrounds.lobby.LobbyListeners;

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
		LobbyHandler lobby;
		GameHandler game;
		ArenaBuilder builder;
		switch(command){
		case "join":
			arenaName = (args.length > 1 ? args[1] : null);
			// checks if player is in a current game
			if (ActivePlayers.getGame(target) != null){
				Messenger.send(sender, "&7Please leave your current game before starting a new one. Type /bg leave to leave your current game");
				break;
			}
			// checks if game name has been specified
			if (arenaName == null) {
				Messenger.send(sender, "Please specify an arena! Eg. /bg join [arena]");
				break;
			}
			arena = Arenas.get(arenaName);
			if (arena == null){
				Messenger.send(sender, "&cThis arena does not exist.");
				break;
			}
			// checks if arena is in edit mode
			if (arena.getEditMode()){
				Messenger.send(sender, "&cThis arena is in edit mode");
				break;
			}
			// checks if arena is missing any spawns
			if(!arena.getEmptySpawnLists().isEmpty()){
				Messenger.send(sender, "&cThis arena is missing one or more spawns. Please ask a moderator to add missing spawns.");
				break;
			}
			// checks if arena is currently running a game and adds player if it is
			/*
			 * CHANGE THIS TO BE THE LOBBY, NOT THE GAME (so players join if the lobby is open but cant join mid-game)
			 */
			if (arena.getInGame()){
				Messenger.send(sender, "&cThis arena is already in a game! You can spectate with /bg spectate " + arenaName);
				break;
			}
			if (arena.getInLobby()){
				LobbyInstances.getLobby(arena).addPlayer(target);
				break;
			}
			
			LobbyListeners lobbyListeners = new LobbyListeners(plugin);
			lobby = new LobbyHandler(plugin, Arenas.get(arenaName), lobbyListeners);
			lobby.addPlayer(target);
//			// Otherwise, creates a new GameHandler!
//			GameListeners gameListeners = new GameListeners(plugin);
//			
//			game = new GameHandler(plugin, Arenas.get(arenaName), gameListeners);
//			game.addPlayer(target);
//			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, game);
//			// registering the arena builder listener
//			plugin.getServer().getPluginManager().registerEvents(gameListeners, plugin);
			break;
			
		case "leave":
			lobby = ActivePlayers.getLobby(target);
			if(lobby != null){
				lobby.playerLeave(target, true, true);
				break;
			}
			game = ActivePlayers.getGame(target);
			if(game != null){
				game.playerLeave(target, true, true);
				break;
			}
			
			break;
		case "list":
			StringJoiner list = new StringJoiner(", ");
			for(ArenaHandler selectedArena : Arenas.get()){
				String name = "&a" + selectedArena.getName();
				if(selectedArena.getEditMode() || selectedArena.missingSpawns()){
					name = "&c" + selectedArena.getName();
				}
				list.add(name);
			}
			Messenger.send(sender, "&fListed Arenas: ("+ Arenas.get().size() +") " + list.toString());
			break;
		case "edit":
			arenaName = (args.length > 1 ? args[1] : null);
			if(arenaName == null){
				Messenger.send(target, "Please specify an arena! Eg. /bg edit [arena] [build|spawns]");
				break;
			}
			arena = Arenas.get(arenaName);
			if (arena == null){
				Messenger.send(target, "&cThis arena does not exist.");
				break;
			}
			// checks if arena is in edit mode
			if (arena.getEditMode()){
				builder = ArenaBuilderInstances.getBuilder(arena);
				Messenger.send(target, builder.getCreatorName() + " &cis already editing this arena!");
				break;
			}
			String editOption = (args.length > 2 ? args[2] : "null");
			switch(editOption){
			case "build":
				Messenger.send(target, "&cBuilder not Implemented yet!");
				break;
			case "spawns":
				builder = new ArenaBuilder(plugin, target, arena);
				builder.editSpawns();
				break;
			default:
				Messenger.send(target, "&cInvalid argument. Please choose either 'build' or 'spawns' editor.");
				break;
			}
			break;
		case "create":
			arenaName = (args.length > 1 ? args[1] : null);
			if(arenaName == null){
				Messenger.send(target, "You must give the arena a name. Eg. /bg create [arena]");
				break;
			}
			if(Arenas.alreadyArena(arenaName)){
				Messenger.send(target, "'" + arenaName + "' is already an arena name. Please choose another");
				break;
			}
			ArenaBuilder newArena = new ArenaBuilder(plugin, target, args[1]);
			newArena.createArena();
			break;
		case "save":
			builder = ArenaBuilderInstances.getBuilder(target);
			builder.save();
			break;
		case "remove":
			arenaName = (args.length > 1 ? args[1] : null);
			if(arenaName == null){
				Messenger.send(target, "You must specify an arena name. Eg. /bg remove [arena name]");
				break;
			}
			arena = Arenas.get(args[1]);
			if(arena != null && Arenas.remove(arena))
				Messenger.send(target, "Arena '" + arena.getName() + "' removed!");
			else
				Messenger.send(target, "There is no arena with that name");
			break;
		default:
			Messenger.send(target, "Please specify a Battleground Command!");
		}
		return true;
	}

}
