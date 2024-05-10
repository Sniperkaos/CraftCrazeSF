package me.cworldstar.craftcrazesf.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.utils.Utils;
import io.github.mooy1.infinitylib.commands.SubCommand;

public class Reload extends SubCommand {

	public Reload(String name, String description, String permission) {
		super(name, description, permission);
	}

	@Override
	protected void execute(CommandSender sender, String[] args) {
		//-- reload the config
		CraftCrazeSF.instance().reloadConfig();
		//-- reload the spawn listener
		CraftCrazeSF.getMainPlugin().getSpawnListener().reload();
		
		sender.sendMessage(Utils.formatString("&6[CraftCrazeSF]: &fThe config has reloaded."));
	}

	@Override
	protected void complete(CommandSender sender, String[] args, List<String> completions) {
		
	}

}
