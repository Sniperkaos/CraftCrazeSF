package me.cworldstar.craftcrazesf.api.dot;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Plague extends BukkitRunnable {

	private Player player;
	
	@Override
	public void run() {
		
		if(this.isCancelled()) {
			return;
		}
		
		PotionEffect decay = new PotionEffect(PotionEffectType.POISON, 4, 5);
		decay.apply(player);
		
		Location l = player.getLocation();
		World w = player.getWorld();
		for(int i=1; i<10; i++) {
			w.spawnParticle(Particle.SLIME, l.getX(), l.getY() + 1, l.getZ(), 0, Math.toRadians(360/i), Math.toRadians(360/i), Math.toRadians(360/i));
		}
	}
	
	public void setPlayer(Player p) {
		this.player = p;
	}
	
	public Player getPlayer() {
		return this.player;
	}
}
