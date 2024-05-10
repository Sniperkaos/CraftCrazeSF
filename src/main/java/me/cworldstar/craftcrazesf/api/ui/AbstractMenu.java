package me.cworldstar.craftcrazesf.api.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import me.cworldstar.craftcrazesf.CraftCrazeSF;

public class AbstractMenu implements Listener {
	
	public static Map<UUID, AbstractMenu> menus = new HashMap<UUID, AbstractMenu>();
	
	private Inventory menu;
	
	private Map<Integer, MenuClickHandler> handlers = new HashMap<Integer, MenuClickHandler>();
	private MenuClickHandler void_handler;
	private UUID id;
	
	public AbstractMenu(int size) {
		Bukkit.getServer().getPluginManager().registerEvents(this, CraftCrazeSF.getMainPlugin());
		menu = Bukkit.createInventory(null, size);
		
		this.id = UUID.randomUUID();
		
		menus.put(id, this);
	}
	
	public void addUnclickableItems(Map<Integer, ItemStack> items) {
		for(Entry<Integer, ItemStack> entry : items.entrySet()) {
			entry.getValue().getItemMeta().getPersistentDataContainer().set(CraftCrazeSF.createKey("no-click"), PersistentDataType.BOOLEAN, true);
			menu.setItem(entry.getKey(), entry.getValue());
		}
	}
	
	public void addNullItemHandler(MenuClickHandler h) {
		this.void_handler = h;
	}
	
	public void addClickableItem(int slot, ItemStack item, MenuClickHandler handler) {
		
		handlers.putIfAbsent(slot, handler);
		
		item.getItemMeta().getPersistentDataContainer().set(CraftCrazeSF.createKey("no-click"), PersistentDataType.BOOLEAN, true);
		menu.setItem(slot, item);
	}
	
	public Inventory asInventory() {
		return this.menu;
	}
	
	public void display(Player p) {
		p.openInventory(this.menu);
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		
		if(e.getClickedInventory() != this.asInventory()) {
			return;
		}
		
		if(e.getCurrentItem() == null) {
			if(this.void_handler != null) {
				this.void_handler.run(e);
			}
			return;
		}
		
		if(e.getCurrentItem().hasItemMeta()) {
			
			if(handlers.get(e.getSlot()) != null && e.getClickedInventory() == this.menu) {
				handlers.get(e.getSlot()).run(e);
			}
			
			if(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(CraftCrazeSF.createKey("no-click"), PersistentDataType.BOOLEAN) == true) {
				e.setCancelled(true);
			}
			return;
		}
		//-- wtf?
		e.setCancelled(true);
	}
	
}
