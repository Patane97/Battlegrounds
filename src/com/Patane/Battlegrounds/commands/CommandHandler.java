package com.Patane.Battlegrounds.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Patane.Battlegrounds.Battlegrounds;
import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.commands.all.*;

public class CommandHandler implements CommandExecutor{
	
	private Battlegrounds plugin;
	
	private HashMap<String, BGCommand> commands;
	
	public CommandHandler(Battlegrounds battlegrounds) {
		this.plugin = battlegrounds;
		registerAll();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String command = (args.length > 0 ? args[0] : "");
		// checking if sender is from console
		Player target = ((sender instanceof Player) ? (Player) sender : null);
		if (target == null){
			Messenger.send(target, "Command cannot be executed from console!");
			return false;
		}
		BGCommand newCommand = getCommand(command);
		if(newCommand == null){
			Messenger.send(target, "Command not found!");
			return false;
		}
		return newCommand.execute(plugin, target, args);
	}
	private BGCommand getCommand(String cmd){
		for(String commandName : commands.keySet()){
			if(cmd.contains(commandName))
				return commands.get(commandName);
		}
		return null;
	}
	private void registerAll() {
		commands = new HashMap<String, BGCommand>();
		register(joinCommand.class);
		register(leaveCommand.class);
		register(editCommand.class);
		register(createCommand.class);
		register(listCommand.class);
		register(removeCommand.class);
		register(saveCommand.class);
		register(classCommand.class);
		register(statusCommand.class);
	}
	public void register(Class< ? extends BGCommand> command){
		CommandInfo cmdInfo = command.getAnnotation(CommandInfo.class);
		if(cmdInfo == null) {
			Messenger.warning("A command is missing its attached CommandInfo Annotation!");
			return;
		}
		try {
			commands.put(cmdInfo.name(), command.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

}
