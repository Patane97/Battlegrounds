package com.Patane.Battlegrounds.commands;

import java.util.StringJoiner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.collections.*;
import com.Patane.Battlegrounds.game.GameHandler;

public class CommandHandler implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String command = (args.length > 0 ? args[0] : "");

		Player target = ((sender instanceof Player) ? (Player) sender : null);
		if (target == null){
			Messenger.send(sender, "Command cannot be executed from console!");
			return false;
		}
		String gameName;
		switch(command){
		case "start":
			gameName = (args.length > 1 ? args[1] : null);
			// checks if game name has been specified
			if (gameName == null) {
				Messenger.send(sender, "Please specify a game name! Eg. /bg start [game name]");
				break;
			}
			// checks if game name is already taken
			if (GameInstances.gameHasName(gameName)){
				Messenger.send(sender, "Please choose a different game name.");
				break;
			}
			// checks if player is in a current game
			if (ActivePlayers.getGame(target) != null){
				Messenger.send(sender, "Please leave your current game before starting a new one. Type /bg leave to leave your current game");
				break;
			}
			// Otherwise, creates a new GameHandler! Adds the player and new GameHandler to 'ActivePlayers' hash table
			ActivePlayers.add(target, new GameHandler(target, gameName));
			break;
			
		case "leave":
			GameHandler game = ActivePlayers.getGame(target);
			if(game != null){
				game.playerLeaveGameCheck(target);
				}
			else Messenger.send(sender, "You must be in a game to leave one!");
			break;
		case "join":
			gameName = (args.length > 1 ? args[1] : null);
			GameInstances.getGame(gameName).addPlayer(target);
			break;
		case "list":
			StringJoiner list = new StringJoiner(",");
			for(String name : GameInstances.listGameNames()){
				list.add(name);
			}
			Messenger.send(sender, "Listed Games: " + list.toString());
			break;
		default:
			Messenger.send(sender, "Please specify a Battleground Command!");
		}
		return true;
	}

}
