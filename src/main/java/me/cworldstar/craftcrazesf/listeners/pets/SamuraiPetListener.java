package me.cworldstar.craftcrazesf.listeners.pets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.items.pets.SamuraiPet;

public class SamuraiPetListener implements Listener {
	private CraftCrazeSF plugin;
	
	public SamuraiPetListener() {
		this.plugin = CraftCrazeSF.getPlugin(CraftCrazeSF.class);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent e) {
		//-- get the player inventory
		Entity player = e.getDamager();
		if(player instanceof Player) {
			Player p = (Player) player;
			Inventory i = p.getInventory();
			for(ItemStack item : i.getContents()) {
				// if this is too expensive we can change it later
				SlimefunItem pet = SlimefunItem.getByItem(item);
				if(pet == null) {
					continue;
				}
				if(pet instanceof SamuraiPet) {
					// increase DMG by 175%
					SamuraiPet sp = (SamuraiPet) pet;
					if(sp.has_integrity(item)) {
						if(sp.onPetTrigger(item, p, i)) {
							e.setDamage(e.getDamage() * 1.75);
							sp.reduce_integrity(item, Math.random() * 12);
							return;
						}
					} else {
						sp.feed(item, p, i);
					}
				}
			}
		}

	}
	
}