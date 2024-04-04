package me.cworldstar.craftcrazesf.listeners.pets;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.items.pets.ChickenPet;

public class ChickenPetListener implements Listener {

	private CraftCrazeSF plugin;
	
	public ChickenPetListener() {
		this.plugin = CraftCrazeSF.getPlugin(CraftCrazeSF.class);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getCause().equals(DamageCause.FALL)) {
			if(e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				Inventory i = p.getInventory();
				for(ItemStack item : i.getContents()) {
					// if this is too expensive we can change it later
					SlimefunItem pet = SlimefunItem.getByItem(item);
					if(pet == null) {
						continue;
					}
					if(pet instanceof ChickenPet) {
						// remove the damage
						e.setCancelled(true);
						ChickenPet cp = (ChickenPet) pet;
						if(cp.has_integrity()) {
							cp.reduce_integrity((int) 0.1);
						} else {
							cp.feed(i);
						}
					}
				}
			}
		}
	}
}
