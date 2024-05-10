package me.cworldstar.craftcrazesf.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.persistence.PersistentDataType;

import me.cworldstar.craftcrazesf.CraftCrazeSF;

public class DurabilityModifier implements Listener {
	
	public DurabilityModifier() {
		Bukkit.getServer().getPluginManager().registerEvents(this, CraftCrazeSF.getMainPlugin());
	}
	
	
	@EventHandler(priority = EventPriority.LOW)
	public void onItemDurabilityDamage(PlayerItemDamageEvent e) {
		if(e.getItem().hasItemMeta()) {			
			if(!e.getItem().getItemMeta().getPersistentDataContainer().has(CraftCrazeSF.createKey("durability"))) {
				return;
			}
			int durability = (e.getItem().getItemMeta().getPersistentDataContainer().get(CraftCrazeSF.createKey("durability"), PersistentDataType.INTEGER));
			if(durability > 0) {
				e.setCancelled(true);
				e.getItem().getItemMeta().getPersistentDataContainer().set(CraftCrazeSF.createKey("durability"), PersistentDataType.INTEGER, durability - e.getDamage());
			}
		}
	}
}
