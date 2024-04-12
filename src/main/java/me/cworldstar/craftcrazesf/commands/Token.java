package me.cworldstar.craftcrazesf.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.mooy1.infinitylib.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.cworldstar.craftcrazesf.Registry;
import me.cworldstar.craftcrazesf.utils.Utils;

public class Token extends SubCommand  {

	public Token(String name, String description, String permission) {
		super(name, description, permission);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		Player p = Bukkit.getPlayer(args[1]);
		if(p == null) {
			sender.sendMessage(Utils.formatString("&6[CraftCrazeSF]: A valid player is required!"));
		}
		
		switch(args[0]) {
			case "basic_machine_token":  
				p.getInventory().addItem(new CustomItemStack(Registry.TOKEN_BASIC_MACHINES));
				break;
			case "advanced_machine_token":
				p.getInventory().addItem(new CustomItemStack(Registry.TOKEN_ADVANCED_MACHINES));
				break;
			case "elite_machine_token":
				p.getInventory().addItem(new CustomItemStack(Registry.TOKEN_ELITE_MACHINES));
				break;
			case "future_machine_token":
				p.getInventory().addItem(new CustomItemStack(Registry.TOKEN_FUTURE_MACHINES));
				break;
		default:
			sender.sendMessage("The token does not exist.");
	}
	}

	@Override
	public void complete(CommandSender sender, String[] args, List<String> completions) {
		// TODO Auto-generated method stub
		
		if(args.length == 1) {
			completions.add("basic_machine_token");
			completions.add("advanced_machine_token");
			completions.add("elite_machine_token");
			completions.add("future_machine_token");
		} else if(args.length == 2) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				completions.add(p.getName());
			}
		}

	}


}
