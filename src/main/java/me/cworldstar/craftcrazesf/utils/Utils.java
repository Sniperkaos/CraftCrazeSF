package me.cworldstar.craftcrazesf.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.mooy1.infinitylib.metrics.json.JsonObjectBuilder;
import io.github.mooy1.infinitylib.metrics.json.JsonObjectBuilder.JsonObject;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONObject;
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
	
	//public static JsonObject unserialize(String JSON) {
	//	new JsonObjectBuilder().
	//}
	
	public static int RandomInteger(int min, int max) {
		return (int) Math.floor(Math.random() * max + min);
	}
	
	public static ArrayList<String> CreateLore(String...strings) {
		ArrayList<String> returns = new ArrayList<String>();
		for(String s : strings) {
			returns.add(Utils.formatString(s));
		}
		
		return returns;
	}
	
	public static ArrayList<String> CreateLore(List<String> strings) {
		ArrayList<String> returns = new ArrayList<String>();
		for(String s : strings) {
			returns.add(Utils.formatString(s));
		}
		
		return returns;
	}
	
	public static int[] IntegerRange(int min, int max) {
		
		ArrayList<Integer> range = new ArrayList<Integer>();
		
		for(int i=min; i<= max; i++) {
			range.add(i);
		}
		
		return range.stream().mapToInt(i -> i).toArray();
		
	}

	public static double clamp(int min, int max, double d) {
		
		if(d >= max) {
			d = (double) max;
		} 
		
		if (d <= min) {
			d = (double) min;
		}
	
		return d;
		
	}
	
}
