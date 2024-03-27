package me.cworldstar.craftcrazesf.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.cworldstar.craftcrazesf.CraftCrazeSF;

public class FireBreakBlockListener implements Listener {
	private CraftCrazeSF plugin;
	
	public FireBreakBlockListener() {
		this.plugin = CraftCrazeSF.getPlugin(CraftCrazeSF.class);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onBlockBurnEvent(BlockBurnEvent e) {
		if(e.getBlock().hasMetadata("cannot_burn")) {
			e.setCancelled(true);
		}
	}
}
