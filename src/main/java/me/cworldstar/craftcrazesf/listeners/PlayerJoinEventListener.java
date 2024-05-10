package me.cworldstar.craftcrazesf.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.nightmarket.Nightmarket;
import me.cworldstar.craftcrazesf.utils.Utils;

public class PlayerJoinEventListener implements Listener {

	private CraftCrazeSF plugin;
	
	public PlayerJoinEventListener() {
		this.plugin = CraftCrazeSF.getPlugin(CraftCrazeSF.class);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Nightmarket.PlayerJoined(e.getPlayer());
		if(p.getLastPlayed() <= 0) {
			for(Player playerToSend : Bukkit.getOnlinePlayers()) {
				
				if(playerToSend.equals(e.getPlayer())) {
					continue;
				}
				
				ArrayList<String> lore = Utils.CreateLore(CraftCrazeSF.config().getStringList("lang.player-first-join"));
				for(String lore_line : lore) {
					if(CraftCrazeSF.PlaceholderAPI_Loaded) {
						playerToSend.sendMessage(PlaceholderAPI.setPlaceholders(e.getPlayer(), lore_line));
					} else {
						playerToSend.sendMessage(lore_line.replaceAll("%player_name%", p.getName()));
					}
				}
			}
		} else {
			for(Player playerToSend : Bukkit.getOnlinePlayers()) {
				
				if(playerToSend.equals(e.getPlayer())) {
					continue;
				}
				
				ArrayList<String> lore = Utils.CreateLore(CraftCrazeSF.config().getStringList("lang.player-welcome-back"));
				for(String lore_line : lore) {
					if(CraftCrazeSF.PlaceholderAPI_Loaded) {
						playerToSend.sendMessage(PlaceholderAPI.setPlaceholders(e.getPlayer(), lore_line));
					} else {
						playerToSend.sendMessage(lore_line.replaceAll("%player_name%", p.getName()));
					}
				}
			}
		}
		
		//motd
		ArrayList<String> motd_lore = Utils.CreateLore(CraftCrazeSF.config().getStringList("lang.player-motd"));
		for(String motd_lore_line : motd_lore) {
			if(CraftCrazeSF.PlaceholderAPI_Loaded) {
				e.getPlayer().sendMessage(PlaceholderAPI.setPlaceholders(e.getPlayer(), motd_lore_line));
			} else {
				e.getPlayer().sendMessage(motd_lore_line.replaceAll("%player_name%", p.getName()));
			}
		}
	}
	
}
