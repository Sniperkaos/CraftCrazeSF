package me.cworldstar.craftcrazesf.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.cworldstar.craftcrazesf.Registry;

public class Token implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission("ccsf.token")) {
			return false;
		}
	
		Player p = Bukkit.getPlayer(args[0]);
		if(p == null) {
			sender.sendMessage("The player is invalid.");
			return true;
		}
		
		//-- 2nd arg, e.g /token Player token_identifier
		switch(args[1]) {
			case "basic_machine_token":  
				p.getInventory().addItem(new CustomItemStack(Registry.TOKEN_BASIC_MACHINES));
				break;
			case "advanced_token":
				p.getInventory().addItem(new CustomItemStack(Registry.TOKEN_ADVANCED_MACHINES));
				break;
			default:
				sender.sendMessage("The token does not exist.");
		}
		
		return true;
	}

}
