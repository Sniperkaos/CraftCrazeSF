package me.cworldstar.craftcrazesf.mobs;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Level;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import io.github.bakedlibs.dough.config.Config;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.mobs.normal.ViruleanZombie;

public class StaticMobController {
	public static Map<String, LivingEntity> mobs = new HashMap<String, LivingEntity>();

	public StaticMobController() {
		CraftCrazeSF.log(Level.WARNING, "StaticMobController is a static class!");
	}
	
	public static void save() {
		Config to_write = new Config("data-storage/CraftCrazeSF/Mobs.yml");
		// clear already existing mobs so there's no dead mobs being stored
		to_write.clear();
		
		CraftCrazeSF.log(Level.INFO, "Saving stored mobs.");
		for(Entry<String, LivingEntity> mob : mobs.entrySet()) {
			to_write.setValue(mob.getKey(), mob.getValue().getUniqueId().toString());
		}

		to_write.save();
		CraftCrazeSF.log(Level.INFO, "Stored mobs saved!");
	}
	
	public static Optional<Entity> getEntityFromUUID(UUID identifier) {
		return Optional.ofNullable(Bukkit.getEntity(identifier));
	}
	
	public static void load() {
		Config to_write = new Config("data-storage/CraftCrazeSF/Mobs.yml");
		CraftCrazeSF.log(Level.INFO, "Loading stored mobs.");
		for(String keys : to_write.getKeys()) {
			String mob = to_write.getString(keys);
			UUID identifier = UUID.fromString(mob);
			Entity e = Bukkit.getEntity(identifier);
			if(e instanceof LivingEntity) {
				//new ViruleanZombie(UUID.fromString(keys), identifier);
				mobs.put(keys, (LivingEntity) e);
			}
		}
		CraftCrazeSF.log(Level.INFO, "Loaded stored mobs!");
	}
}
