package me.cworldstar.craftcrazesf.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.mooy1.infinitylib.commands.SubCommand;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.utils.Utils;

public class Nightmarket extends SubCommand {

	public Nightmarket(String name, String description, String perm) {
		super(name, description, perm);
	}

	@Override
	protected void execute(CommandSender sender, String[] args) {
		switch(args[0]) {
			case "open":
				CraftCrazeSF.getNightmarket().display((Player) sender);
				break;
			case "refresh":
				CraftCrazeSF.getNightmarket().refresh((Player) sender);
				break;
			default:
				sender.sendMessage(Utils.formatString("&6The command does not exist."));
		}
	}

	@Override
	protected void complete(CommandSender sender, String[] args, List<String> completions) {
		completions.addAll(List.of(
			
				"open",
				"refresh"
				
		));
	}

}
