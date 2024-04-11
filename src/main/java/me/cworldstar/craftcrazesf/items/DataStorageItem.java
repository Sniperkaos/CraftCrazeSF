package me.cworldstar.craftcrazesf.items;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import me.cworldstar.craftcrazesf.CraftCrazeSF;

public interface DataStorageItem {
	
	public default void store(ItemStack item, String key, String data) {
		if(item.hasItemMeta()) {
			item.getItemMeta().getPersistentDataContainer().set(CraftCrazeSF.createKey(key), PersistentDataType.STRING, data);
		}
	}
	
	public default Optional<String> load(ItemStack item, String key) {
		if(item.hasItemMeta()) {
			return Optional.ofNullable( item.getItemMeta().getPersistentDataContainer().get(CraftCrazeSF.createKey(key), PersistentDataType.STRING) );
		}
		return Optional.empty();
	}
}
