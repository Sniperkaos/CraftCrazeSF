package me.cworldstar.craftcrazesf.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.mooy1.infinitylib.commands.SubCommand;
import me.cworldstar.craftcrazesf.mobs.normal.ViruleanZombie;
import me.cworldstar.craftcrazesf.mobs.normal.WanderingSwordsman;
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
					
				case "WanderingSwordsman":
					new WanderingSwordsman(((Player) sender).getLocation());
					break;
				default:
					sender.sendMessage(Utils.formatString("&6[CraftCrazeSF]: Invalid argument " + args[0] + "."));
			}
		} else {
			if(args.length <= 1) {
				sender.sendMessage(Utils.formatString("&6[CraftCrazeSF]: You need to specify the location! Usage: <world> <x> <y> <z>"));
			}
			
			switch(args[0]) {
				case "ViruleanZombie":
					new ViruleanZombie(new Location(Bukkit.getWorld(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4])));
					break;
				case "WanderingSwordsman":
					new WanderingSwordsman(new Location(Bukkit.getWorld(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4])));
					break;
				default:
					sender.sendMessage(Utils.formatString("&6[CraftCrazeSF]: Invalid argument " + args[0] + "."));
		}
		}
	}

	@Override
	protected void complete(CommandSender sender, String[] args, List<String> completions) {
		
		if(sender instanceof Player) {
			if(args.length == 1) {
				completions.addAll(List.of(
						
						"ViruleanZombie",
						"WanderingSwordsman"
						
				));
			}	
		} else { 
			if(args.length == 1) {
				completions.addAll(List.of(
						
						"ViruleanZombie",
						"WanderingSwordsman"
						
				));
			} else if(args.length == 2) {
				for(World w : Bukkit.getWorlds()) {
					completions.add(w.getName());
				}
			}
		}
		

	}

}
