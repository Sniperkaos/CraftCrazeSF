package me.cworldstar.craftcrazesf.listeners;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.math.RandomUtils;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.mobs.normal.ViruleanZombie;

public class SpawnListener implements Listener {

	private int virulean_spawn_chance;
	
	public SpawnListener() {
		virulean_spawn_chance = CraftCrazeSF.cfg.getInt("spawning.virulean_zombie.spawn_chance");
		Bukkit.getServer().getPluginManager().registerEvents(this, CraftCrazeSF.getMainPlugin());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntitySpawn(EntitySpawnEvent e) {
		if(e.getEntity() instanceof Zombie) {
			
			List<String> blacklisted_worlds = CraftCrazeSF.cfg.getStringList("spawning.virulean_zombie.blacklisted_dimensions");
			if(blacklisted_worlds.contains(e.getLocation().getWorld().getName())) {
				return;
			}
			// 1/100% chance to spawn a virulean zombie instead
			if(RandomUtils.nextInt(virulean_spawn_chance) >= virulean_spawn_chance-1) {
				e.setCancelled(true);
				CraftCrazeSF.log(Level.INFO, "ViruleanZombie spawned at " + e.getEntity().getLocation().toString());
				new ViruleanZombie(e.getLocation());
			}
		}	
	}
}
