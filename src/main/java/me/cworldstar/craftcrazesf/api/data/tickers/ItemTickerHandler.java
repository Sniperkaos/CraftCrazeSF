package me.cworldstar.craftcrazesf.api.data.tickers;

import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.items.TickingItem;

public class ItemTickerHandler {
	public ItemTickerHandler() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Entry<ItemStack, Runnable> entry : TickingItem.itemTickers.entrySet()) {
					entry.getValue().run();
				}
			}
			
		}.runTaskTimer(CraftCrazeSF.instance(), 1, 0);
	}
}
