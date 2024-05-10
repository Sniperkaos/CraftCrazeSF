package me.cworldstar.craftcrazesf.nightmarket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.math.RandomUtils;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.utils.Utils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import net.milkbowl.vault.economy.AbstractEconomy;

@SuppressWarnings("deprecation")
public class NightmarketData {

	private ChestMenu nightmarket_ui;
	private Map<String, ItemStack> items_to_display = new HashMap<String, ItemStack>();
	private Map<Integer, ItemStack> items_in_ui = new HashMap<Integer, ItemStack>();
	private Map<Integer, NightmarketHandler> display_handlers = new HashMap<Integer, NightmarketHandler>();
	private Map<String, NightmarketEntry> entries = new HashMap<String, NightmarketEntry>();
	
	
	private void nightmarket_log(String s) {
		CraftCrazeSF.log(Level.INFO, "[Nightmarket]: " + s);
	}
	
	public NightmarketData(Player player) {
		this.nightmarket_ui = new ChestMenu(player.getDisplayName());
	}	
	
	public void populate(int amount) {
		
		//-- remove previous entries
		if(entries.size() != 0) {
			entries = new HashMap<String, NightmarketEntry>();
		}
		
		for(int i=0; i<=amount; i++) {
			NightmarketEntry[] nm_entries = (NightmarketEntry[]) entries.values().toArray();
			NightmarketEntry entry = nm_entries[RandomUtils.nextInt(nm_entries.length)];
			addDisplayItem(entry.getId(), entry.getChance(), entry.getItem(), entry.getPrice());
		}
	}
	
	public void refresh(Player p) {
		this.populate(p.getPersistentDataContainer().get(CraftCrazeSF.createKey("nightmarket.max_nightmarket_entries"), PersistentDataType.INTEGER));
	}
	
	public void addNightmarketEntry(String entry_id, int chance, ItemStack item, int price) {
		entries.putIfAbsent(entry_id, new NightmarketEntry( chance, item, entry_id, price));
	}
	
	public void addDisplayItem(String id, int slot, ItemStack display_item, int price) {
		if(this.items_to_display.get(id) != null) {
			this.nightmarket_log("Attempt to overwrite a display item.");
			return;
		}
		
		ItemStack stack = display_item.clone();
		ItemMeta meta = stack.getItemMeta();
		meta.getPersistentDataContainer().set(CraftCrazeSF.createKey("price"), PersistentDataType.INTEGER, price);
		meta.setDisplayName("&6&lFOR SALE -&r " + meta.getDisplayName());
		List<String> lore = meta.getLore();
		lore.add(Utils.formatString("&cPrice: " + Integer.toString(price)));
		meta.setLore(lore);
		stack.setItemMeta(meta);
	
		NightmarketHandler handler = new NightmarketHandler(id, slot, display_item, (Player p, NightmarketHandler h, NightmarketEntry e) ->{
			ItemStack item = h.getItem();
			if(p.getInventory().firstEmpty() == -1) {
				p.sendMessage("&6&l[Nightmarket]:&r &fYou have a full inventory.");
				return;
			}
			if(CraftCrazeSF.getEconomy().getBalance(p) < e.getPrice()) {
				p.sendMessage("&6&l[Nightmarket]:&r &fYou cannot afford this item.");
				return;
			}
			if(e.getCanPurchase(p) == false) {
				p.sendMessage("&6&l[Nightmarket]:&r &fYou have already purchased this item.");
				return;
			}
			e.setCanPurchase(p, false);
			p.getInventory().addItem(item);
		});
		
		setDisplayHandler(slot, id, handler);
		
		nightmarket_ui.addItem(slot, stack);
		items_in_ui.putIfAbsent(slot, stack);
		this.items_to_display.putIfAbsent(id, stack);
	}
	
	public void setDisplayHandler(int slot, String id, NightmarketHandler handler) {
		if(this.items_to_display.get(id) == null) {
			this.nightmarket_log("Attempted to set a display handler of a null item.");
			return;
		}
		this.display_handlers.putIfAbsent(slot, handler);
	}
	
	public int getMaxNightmarketEntries(Player p) {
		int entries = p.getPersistentDataContainer().get(CraftCrazeSF.createKey("nightmarket.max_nightmarket_entries"), PersistentDataType.INTEGER);
		if (entries == 0) {
			p.getPersistentDataContainer().set(CraftCrazeSF.createKey("nightmarket.max_nightmarket_entries"), PersistentDataType.INTEGER, 6);
			return 6;
		}
		return entries;
	}
	
	public long getNightmarketRefreshCooldown(Player p) {
		long cd = p.getPersistentDataContainer().get(CraftCrazeSF.createKey("nightmarket.nightmarket_cd"), PersistentDataType.LONG);
		long start = p.getPersistentDataContainer().get(CraftCrazeSF.createKey("nightmarket.nightmarket_cd"), PersistentDataType.LONG);
		
		if (cd == 0 && start == 0) {
			p.getPersistentDataContainer().set(CraftCrazeSF.createKey("nightmarket.nightmarket_cd"), PersistentDataType.LONG, 86400L);
			p.getPersistentDataContainer().set(CraftCrazeSF.createKey("nightmarket.nightmarket_cd_start"), PersistentDataType.LONG,System.currentTimeMillis());
			return 86400L;
		}
		
		if(System.currentTimeMillis() - start >= cd) {
			p.getPersistentDataContainer().set(CraftCrazeSF.createKey("nightmarket.nightmarket_cd"), PersistentDataType.LONG, 0L);
			p.getPersistentDataContainer().set(CraftCrazeSF.createKey("nightmarket.nightmarket_cd_start"), PersistentDataType.LONG,0L);
			return 0L;
		}
		
		return (System.currentTimeMillis() - start);
		
	}
	
	public void display(Player p) {
		nightmarket_ui.open(p);
	}
	
	public void callDisplayHandler(Player p, int slot) {
		//-- get the display handler
		NightmarketHandler handler = this.display_handlers.get(slot);
		NightmarketEntry entry = this.entries.get(handler.getId());
		handler.run(p, entry);
	}
	
	public ChestMenu getNightmarketUI() {
		return this.nightmarket_ui;
	}

}
