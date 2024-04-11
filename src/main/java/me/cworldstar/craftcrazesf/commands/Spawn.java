package me.cworldstar.craftcrazesf.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.mooy1.infinitylib.commands.SubCommand;
import me.cworldstar.craftcrazesf.mobs.normal.ViruleanZombie;
import me.cworldstar.craftcrazesf.utils.Utils;

public class Spawn extends SubCommand {

	public Spawn(String name, String description, String perm) {
		super(name, description, perm);

	}

	@Override
	protected void execute(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			if(args.length > 1) {
				sender.sendMessage(Utils.formatString("&6[CraftCrazeSF]: This command requires only 1 arg."));
				return;
			}
			
			switch(args[0]) {
				case "ViruleanZombie":
					new ViruleanZombie(((Player) sender).getLocation());
					break;
				default:
					sender.sendMessage(Utils.formatString("&6[CraftCrazeSF]: Invalid argument " + args[0] + "."));
			}
		}
	}

	@Override
	protected void complete(CommandSender sender, String[] args, List<String> completions) {
		completions.add(
		
				"ViruleanZombie"
				
		);
		
	}

}
