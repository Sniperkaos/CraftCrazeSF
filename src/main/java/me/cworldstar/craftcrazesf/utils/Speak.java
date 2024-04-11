package me.cworldstar.craftcrazesf.utils;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import me.cworldstar.craftcrazesf.CraftCrazeSF;
import net.md_5.bungee.api.ChatColor;

public class Speak {
	public static CraftCrazeSF plugin;
	public Speak(CraftCrazeSF plugin) {
		Speak.plugin = plugin;
	}
    public Speak(Player p, String text) {
    	p.getWorld().playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 0.6F, 0.2F);
    	p.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
    }
    public Speak(Entity Origin,List<Entity> Entities, String text) {
    	for(Entity e : Entities) {
    		Origin.getWorld().playSound(Origin.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 0.6F, 0.2F);
    		e.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
    	}
    }
    public Speak(Entity Origin,String text,int Surrounding, Sound to_play) {
    	for(Entity e : Origin.getNearbyEntities(Surrounding, 100, Surrounding)) {
    		Origin.getWorld().playSound(Origin.getLocation(), to_play, 0.6F, 0.2F);
    		e.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
    	}
    }
    public Speak(LivingEntity e, String text) {
    	e.getWorld().playSound(e.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 0.6F, 0.2F);
    	e.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
    }
    public Speak(ItemMeta i, String text) {
    	i.setDisplayName(text);
    }
    public Speak() {}
	public static String format(String text) {
    	return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	public static NamespacedKey key;
	public static NamespacedKey NamespacedKey(String str) {
		return new NamespacedKey(plugin, str);
	}
	public static void AOEMessage(Entity Origin, String text, int Surrounding, Sound to_play) {
		// TODO Auto-generated method stub
		new Speak(Origin, text, Surrounding, to_play);		
	}
}
