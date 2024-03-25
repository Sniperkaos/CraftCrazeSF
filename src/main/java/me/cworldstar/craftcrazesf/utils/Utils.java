package me.cworldstar.craftcrazesf.utils;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.NamespacedKey;


public class Utils {
	
	// useless
	public Utils() {}
	
	public static JavaPlugin getPluginFromString(String plugin) {
		Plugin ThePlugin = Bukkit.getPluginManager().getPlugin(plugin);
		
		return (JavaPlugin) ThePlugin;
	}
	
	public static String replace(String s, String pattern) {
		return s.replaceAll(pattern, s);
	}
	
	public static NamespacedKey createNamespacedKey(JavaPlugin p, String k) {
		return new NamespacedKey(p, k);
	}
	
	public static String formatString(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	public static Integer clamp(int a, int b, int number) {
		
		if(number > a) {
			number = a;
		} 
		
		if (number < b) {
			number = b;
		}
	
		return number;
		
	}
	
	public static int[] IntegerRange(int min, int max) {
		
		ArrayList<Integer> range = new ArrayList<Integer>();
		
		for(int i=0; i<= max; i++) {
			range.add(i);
		}
		
		return range.stream().mapToInt(i -> i).toArray();
		
	}

	public static double clamp(int a, int b, double d) {
		
		if(d >= a) {
			d = (double) a;
		} 
		
		if (d <= b) {
			d = (double) b;
		}
	
		return d;
		
	}
	
}
