package me.cworldstar.craftcrazesf.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.cworldstar.craftcrazesf.utils.Utils;

public class CraftCrazeSF implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch(args[1]) {
			case "reload":
				
			default:
				sender.sendMessage(Utils.formatString("&6> This command does not exist."));
		}
		return true;
	}

}
