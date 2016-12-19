package com.Patane.Battlegrounds.commands;

import org.bukkit.command.CommandSender;

public interface Command {
	public String execute(CommandSender sender, String... args);
}
