package me.cworldstar.craftcrazesf.api.dot;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import io.github.bakedlibs.dough.config.Config;
import me.cworldstar.craftcrazesf.CraftCrazeSF;

/**
 * 
 * Status Effect.
 * @apiNote You need to overwrite the {@link AbstractStatusEffect.getTasks} function.
 * 
 * @author cworldstar
 *
 */

public abstract class AbstractStatusEffect {
	
	private String status_type;
	private BukkitTask task;
	private int duration;
	
	public static Map<String, AbstractStatusEffect> status_effects = new HashMap<String, AbstractStatusEffect>();
	
	public AbstractStatusEffect(LivingEntity e, String StatusID) {
		this.status_type = StatusID;
	}
	
	public static <T extends AbstractStatusEffect> void register(String name, T e) {
		status_effects.put(name, e);
	}
	
	public String getStatusType() {
		return this.status_type;
	}
	
	public static String formatStatusEffect(AbstractStatusEffect e) {
		return e.getStatusType() + "|" + e.getDuration();
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public static Map<? extends Entity, ? extends AbstractStatusEffect> getTasks() {
		return null;
	};
	
	public static void save() {
		Config to_write = new Config("data-storage/CraftCrazeSF/StatusEffects.yml");
		// clear already existing mobs so there's no dead mobs being stored
		to_write.clear();
		
		CraftCrazeSF.log(Level.INFO, "Saving status effects.");
		for(Entry<? extends Entity, ? extends AbstractStatusEffect> mob : getTasks().entrySet()) {
			to_write.setValue(mob.getKey().getUniqueId().toString(), formatStatusEffect(mob.getValue()));
		}

		to_write.save();
		CraftCrazeSF.log(Level.INFO, "Status effects saved!");
	}
	
	public static void load() {
		Config to_write = new Config("data-storage/CraftCrazeSF/StatusEffects.yml");
		CraftCrazeSF.log(Level.INFO, "Loading status effects.");
		for(String keys : to_write.getKeys()) {
			String status_effect = to_write.getString(keys);
			String[] type_and_duration = status_effect.split("|");
			String type = type_and_duration[0];
			String duration = type_and_duration[1];
			if(type != null && duration != null) {
				if(status_effects.get(type) != null) {
					try {
						status_effects.get(type).getClass().getConstructor(LivingEntity.class).newInstance(Bukkit.getEntity(UUID.fromString(keys)));
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
}
