package me.cworldstar.craftcrazesf.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.cworldstar.craftcrazesf.CraftCrazeSF;

public class EntityDamageListener implements Listener {
	
	public EntityDamageListener() {
		Bukkit.getServer().getPluginManager().registerEvents(this, CraftCrazeSF.getMainPlugin());
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		CraftCrazeSF.getDamageManager().fire(e);
	}
	
}
