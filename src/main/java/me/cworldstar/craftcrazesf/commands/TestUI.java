package me.cworldstar.craftcrazesf.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.mooy1.infinitylib.commands.SubCommand;
import me.cworldstar.craftcrazesf.api.ui.AbstractMenu;

public class TestUI extends SubCommand {

	public TestUI(String name, String description, String perm) {
		super(name, description, perm);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(AbstractMenu.menus.containsKey(UUID.fromString(args[0]))) {
			AbstractMenu.menus.get(UUID.fromString(args[0])).display((Player) sender);
		}
	}

	@Override
	public void complete(CommandSender sender, String[] args, List<String> completions) {
		for(UUID s : AbstractMenu.menus.keySet()) {
			completions.add(s.toString());
		}
	}
}
