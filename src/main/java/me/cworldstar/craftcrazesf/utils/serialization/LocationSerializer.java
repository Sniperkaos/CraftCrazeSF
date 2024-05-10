package me.cworldstar.craftcrazesf.utils.serialization;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializer {
	public LocationSerializer() {}
	
	
	public static String to(Location L) {
		return L.getWorld().getName() + ":" + L.getBlockX()+":"+L.getBlockY()+":"+L.getBlockZ();
	}
	public static Location from(String s) {
		String[] digits = s.split(":");
		World world; 
		int x;
		int y;
		int z;
		world = Bukkit.getServer().getWorld(digits[0]);
		x = Integer.parseInt(digits[1]);
		y = Integer.parseInt(digits[2]);
		z = Integer.parseInt(digits[3]);
		return new Location(world, x, y, z);
	}
}
