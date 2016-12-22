package com.Patane.Battlegrounds;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.collections.ActivePlayers;
import com.Patane.Battlegrounds.game.GameHandler;

public class Messenger {
	private static final Logger logger = Logger.getLogger("Minecraft");
	
	public static boolean send(CommandSender sender, String msg) {
        // If the input sender is null or the string is empty, return.
        if (sender == null || msg.equals("")) {
            return false;
        }

        // Otherwise, send the message with the plugin prefix.
        sender.sendMessage(Chat.PLUGIN_PREFIX + ChatColor.translateAlternateColorCodes('&', msg));
        return true;
    }
	public static void broadcast(String string){
		Bukkit.broadcastMessage(Chat.PLUGIN_PREFIX + string);
	}
	public static void info(String msg) {
		logger.info(Chat.STRIPPED_PLUGIN_PREFIX + msg);
	}

	public static void warning(String msg) {
		logger.warning(Chat.STRIPPED_PLUGIN_PREFIX + msg);
	}

	public static void severe(String msg) {
		logger.severe(Chat.STRIPPED_PLUGIN_PREFIX + msg);
	}
	// sends only to people in specific game
	public static void gameCast(GameHandler game, String string) {
		for(Player selectedplayer : game.getPlayers()){
			if(ActivePlayers.getGame(selectedplayer).equals(game)){
				send(selectedplayer, string);
			}
		}
		
	}
}
