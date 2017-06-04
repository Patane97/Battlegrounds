package com.Patane.Battlegrounds;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.Patane.Battlegrounds.Chat;
import com.Patane.Battlegrounds.arena.Arena;

public class Messenger {
	private static final Logger logger = Logger.getLogger("Minecraft");
	
	public static boolean send(CommandSender sender, String msg) {
        // If the input sender is null or the string is empty, return.
        if (sender == null || msg.equals("")) {
            return false;
        }

        // Otherwise, send the message with the plugin prefix.
        sender.sendMessage(Chat.PLUGIN_PREFIX_SMALL + ChatColor.translateAlternateColorCodes('&', msg));
        return true;
    }
//	public static boolean sendRaw(CommandSender sender, String msg) {
//		if (sender == null || msg.equals("")) {
//            return false;
//        }
//
//        // Otherwise, send the message with the plugin prefix.
//        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
//        return true;
//	}
	public static void broadcast(String msg){
		Bukkit.broadcastMessage(Chat.PLUGIN_PREFIX_SMALL + ChatColor.translateAlternateColorCodes('&', msg));
	}
	public static void info(String msg) {
		logger.info(Chat.STRIPPED_PLUGIN_PREFIX + ChatColor.translateAlternateColorCodes('&', msg));
	}

	public static void warning(String msg) {
		logger.warning(Chat.STRIPPED_PLUGIN_PREFIX + ChatColor.translateAlternateColorCodes('&', msg));
	}

	public static void severe(String msg) {
		logger.severe(Chat.STRIPPED_PLUGIN_PREFIX + ChatColor.translateAlternateColorCodes('&', msg));
	}
	// sends only to people in specific game
	public static void arenaCast(Arena arena, String string) {
		for(String selectedPlayer : arena.getPlayers()){
			send(Bukkit.getPlayerExact(ChatColor.stripColor(selectedPlayer)), string);
		}
		
	}
}
