package me.cworldstar.craftcrazesf.nightmarket;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NightmarketEntry {
	
	private int chance;
	private ItemStack item;
	private String id;
	private int price;
	private Map<Player, Boolean> canPurchase = new HashMap<Player, Boolean>();
	
	public NightmarketEntry(int chance, ItemStack item, String id, int price) {
		this.setChance(chance);
		this.setItem(item);
		this.id = id;
		this.setPrice(price);
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public String getId() {
		return id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean getCanPurchase(Player p) {	
		return this.canPurchase.get(p);
	}
	
	public void setCanPurchase(Player p, boolean b) {
		this.canPurchase.put(p, b);
	}
}
