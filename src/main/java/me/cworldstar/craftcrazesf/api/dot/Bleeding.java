package me.cworldstar.craftcrazesf.api.dot;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.utils.ParticleUtils;

public class Bleeding extends AbstractStatusEffect {

	public static Map<LivingEntity, BukkitTask> bleeding = new HashMap<LivingEntity, BukkitTask>();
	
	public static boolean isBleeding(LivingEntity e) {
		return (bleeding.get(e) != null);
	}

 	
	public Bleeding(LivingEntity to_bleed) {
		super(to_bleed, "bleeding");
		bleeding.put(to_bleed, 
			new BukkitRunnable() {

				@Override
				public void run() {
					ParticleUtils.DripFromHead(to_bleed, Particle.DRIP_LAVA);
				}
				
			}.runTaskTimer(CraftCrazeSF.getMainPlugin(), 0, 20)
		);
	}
	
}
