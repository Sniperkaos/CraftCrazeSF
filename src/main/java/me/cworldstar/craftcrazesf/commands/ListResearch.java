package me.cworldstar.craftcrazesf.commands;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.cworldstar.craftcrazesf.CraftCrazeSF;

public class ListResearch implements CommandExecutor {

	private CraftCrazeSF plugin;
	
	public ListResearch(CraftCrazeSF plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("[Server]: Please check your server console.");
		this.plugin.getLogger().log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-");
		this.plugin.getLogger().log(Level.INFO, "     Slimefun Research     ");
		this.plugin.getLogger().log(Level.INFO, "                          ");
		this.plugin.getLogger().log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-");
		for(Research r : Slimefun.getRegistry().getResearches()) {
			this.plugin.getLogger().log(Level.INFO, "");
			this.plugin.getLogger().log(Level.INFO, r.getKey().toString().concat(" ").concat(r.getName((Player) sender)));
		}
		return true;
	}

}
