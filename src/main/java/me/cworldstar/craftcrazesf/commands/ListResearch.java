package me.cworldstar.craftcrazesf.commands;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.mooy1.infinitylib.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.cworldstar.craftcrazesf.CraftCrazeSF;

public class ListResearch extends SubCommand {

	private CraftCrazeSF plugin;
	
	public ListResearch(CraftCrazeSF plugin) {
		super("ListResearch", "Lists all researches in the server console.", true);
		this.plugin = plugin;
	}

	@Override
	protected void execute(CommandSender sender, String[] args) {
		sender.sendMessage("[Server]: Please check your server console.");
		this.plugin.getLogger().log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-");
		this.plugin.getLogger().log(Level.INFO, "     Slimefun Research     ");
		this.plugin.getLogger().log(Level.INFO, "                          ");
		this.plugin.getLogger().log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-");
		for(Research r : Slimefun.getRegistry().getResearches()) {
			this.plugin.getLogger().log(Level.INFO, "");
			this.plugin.getLogger().log(Level.INFO, r.getKey().toString().concat(" ").concat(r.getName((Player) sender)));
		}		
	}

	@Override
	protected void complete(CommandSender sender, String[] args, List<String> completions) {
		
	}
}
