package me.cworldstar.craftcrazesf.listeners.pets;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.items.pets.IronGolemPet;

public class IronGolemPetListener implements Listener {

	private CraftCrazeSF plugin;
	
	public IronGolemPetListener() {
		this.plugin = CraftCrazeSF.getPlugin(CraftCrazeSF.class);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player) {
			if(event.getDamager() == null) {
				return;
			}
			
			// get the slimefun item in the player's inventory
			Player p = (Player) event.getEntity();
			Inventory i = p.getInventory();
			for(ItemStack item : i.getContents()) {
				// if this is too expensive we can change it later
				SlimefunItem pet = SlimefunItem.getByItem(item);
				if(pet == null) {
					continue;
				}
				
				if(pet instanceof IronGolemPet) {
					// reduce the damage by 5%
					double event_damage = event.getDamage();
					double new_damage = event_damage - (event_damage * 0.05);
					event.setDamage(new_damage);
					IronGolemPet igp = (IronGolemPet) pet;
					if(igp.has_integrity()) {
						igp.reduce_integrity((int) 0.1);
					} else {
						igp.feed(i);
					}
				}
			}
			
		}
	}
	
}
