package me.cworldstar.craftcrazesf.nightmarket;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.entity.Player;



public class Nightmarket {
	
	public static Map<Player, NightmarketData> NightmarketData = new HashMap<Player, NightmarketData>();
	
	public static Optional<NightmarketData> getNightmarketData(Player p) {
		return Optional.ofNullable(NightmarketData.get(p));
	}
	
	public Nightmarket() {
		
	}

	public void display(Player player) {
		NightmarketData playerData = Nightmarket.NightmarketData.get(player);
		playerData.display(player);
	}
	
	public void refresh(Player player) {
		NightmarketData playerData = Nightmarket.NightmarketData.get(player);
		playerData.refresh(player);
	}
	
	public static void PlayerJoined(Player player) {
		NightmarketData.putIfAbsent(player, new NightmarketData(player));
	}
}
