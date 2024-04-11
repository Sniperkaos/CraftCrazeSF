package me.cworldstar.craftcrazesf.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import me.cworldstar.craftcrazesf.CraftCrazeSF;

public class PlayerDamageByEntityEvent implements Listener {
	private CraftCrazeSF plugin;
	
	public PlayerDamageByEntityEvent() {
		this.plugin = CraftCrazeSF.getPlugin(CraftCrazeSF.class);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerDamageByEntityEvent(PlayerDamageByEntityEvent e) {
		
	}
}
