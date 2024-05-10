package me.cworldstar.craftcrazesf.nightmarket;

import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NightmarketHandler {

	private int slot;
	private TriConsumer<Player, NightmarketHandler, NightmarketEntry> consumer;
	private ItemStack give;
	private String id;
	
	public NightmarketHandler(String id, int slot, ItemStack toGive, TriConsumer<Player, NightmarketHandler, NightmarketEntry> consumer) {
		this.id = id;
		this.slot = slot;
		this.consumer = consumer;
		this.give = toGive;
	}
	
	public String getId() {
		return this.id;
	}
	
	public ItemStack getItem() {
		return this.give;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
	public void run(Player p, NightmarketEntry entry) {
		consumer.accept(p, this, entry);
	}

}
