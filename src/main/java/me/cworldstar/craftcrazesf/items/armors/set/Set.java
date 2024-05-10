package me.cworldstar.craftcrazesf.items.armors.set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import me.cworldstar.craftcrazesf.CraftCrazeSF;

/**
 *
 * Implementation for sets.
 * Allows set effects.
 * 
 * @author cworldstar
 *
 *
 */
public class Set {
	
	public static Map<String, Set> sets = new HashMap<String, Set>();
	
	/**
	 * When making a new set, add the set to the Sets enum.
	 * 
	 * @author cworldstar
	 *
	 */
	
	public enum Sets {
		ADVANCED_HAZMAT(new Set("ADVANCED_HAZMAT", new ItemStack[] {})),
		TURBO_SWAGLORD_LEGEND_ARMOR(new Set("TURBO_SWAGLORD_LEGEND_ARMOR_SET", new ItemStack[] {}));
		
		private final Set set;
		
		Sets(Set set){
			this.set = set;
		}
		
		public Set getSet() {
			return this.set;
		}
	}
	
	protected List<ItemStack> armor_contents = new ArrayList<ItemStack>();
	protected String set_id;
	
	public Set(String set_id, ItemStack[] set_armor_contents) {
		this.set_id = set_id;
		this.armor_contents.addAll(Arrays.asList(set_armor_contents));
		
		Set.sets.putIfAbsent(set_id, this);
	}
	
	public boolean ValidateSet(List<ItemStack> items) {
		int parts = 0;
		items.removeAll(Collections.singleton(null));
		for(ItemStack item : armor_contents) {
			for(ItemStack to_compare : items) {
				if(to_compare == null) {
					continue;
				}
				if(SlimefunItem.getById(to_compare.getItemMeta().getPersistentDataContainer().get(CraftCrazeSF.createKey("id"), PersistentDataType.STRING)) != null) {
					//-- clone item
					ItemStack comparable_item = SlimefunItem.getById(to_compare.getItemMeta().getPersistentDataContainer().get(CraftCrazeSF.createKey("id"), PersistentDataType.STRING)).getItem();
					if(comparable_item.isSimilar(item)) {

						parts += 1;
						continue;
					}
				} else {
					if(item.isSimilar(to_compare)) {
						parts += 1;
						continue;
					}
				}


			}
		}
		return (parts >= armor_contents.size());
	}
	
	public List<ItemStack> getArmorParts() {
		return this.armor_contents;
	}
	
	public void AddArmorPart(ItemStack i) {
		this.armor_contents.add(i);
	}
}
