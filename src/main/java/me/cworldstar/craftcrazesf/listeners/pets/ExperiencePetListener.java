package me.cworldstar.craftcrazesf.listeners.pets;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.items.pets.ExperiencePet;

public class ExperiencePetListener implements Listener {
	private CraftCrazeSF plugin;
	
	public ExperiencePetListener() {
		this.plugin = CraftCrazeSF.getPlugin(CraftCrazeSF.class);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerExperienceChange(PlayerExpChangeEvent e) {
		//-- get the player inventory
		Player p = e.getPlayer();
		Inventory i = p.getInventory();
		for(ItemStack item : i.getContents()) {
			// if this is too expensive we can change it later
			SlimefunItem pet = SlimefunItem.getByItem(item);
			if(pet == null) {
				continue;
			}
			if(pet instanceof ExperiencePet) {
				// increase exp gained by 50%

				ExperiencePet ep = (ExperiencePet) pet;
				if(ep.has_integrity(item)) {
					if(ep.onPetTrigger(item, p, i)) {
						e.setAmount(e.getAmount() + (e.getAmount() / 2));
						ep.reduce_integrity(item, Math.random() * 12);
					}
				} else {
					ep.feed(item, e.getPlayer(), i);
				}
			}
		}
	}
	
}
