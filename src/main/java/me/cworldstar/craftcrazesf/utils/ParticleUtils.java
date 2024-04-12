package me.cworldstar.craftcrazesf.utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public class ParticleUtils {
	public ParticleUtils() {}
    private static ArrayList<Location> getCircle(Location center, double radius, int amount)
    {
        World world = center.getWorld();
        double increment = (2 * Math.PI)/amount;
        ArrayList<Location> locations = new ArrayList<Location>();
        for(int i = 0;i < amount; i++)
        {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }
    public static void SpawnInCircleWithOffset(Location center, Particle p,double radius, int amountOfPoints,int ParticlesPerPoint,double offset) {
    	ArrayList<Location> Particles = getCircle(center,radius,amountOfPoints);
    	for(Location L : Particles) {
    		L.getWorld().spawnParticle(p,L,ParticlesPerPoint,offset,offset,offset,0.0);
    	}
    }
    public static void SpawnInCircle(Location center, Particle p,double radius, int amountOfPoints,int ParticlesPerPoint, DustOptions options) {
    	ArrayList<Location> Particles = getCircle(center,radius,amountOfPoints);
    	for(Location L : Particles) {
    		L.getWorld().spawnParticle(p,L,ParticlesPerPoint,0.0,0.0,0.0,0.0, options);
    	}
    }
	public static void DripFromHead(LivingEntity e, Particle crimsonSpore) {
		// TODO Auto-generated method stub
		Location L = e.getEyeLocation();
		for(int i = 0; i<=10; i++) {
			L.getWorld().spawnParticle(crimsonSpore, L, 4,0.2,0.2,0.2);
		}
		
	}
	public static void SpawnParticle(Location L, Particle P, int x, int y, int z) {
		L.getWorld().spawnParticle(P, x, y, z, 0, 0, 0, 0, 0, null);
	}
}
