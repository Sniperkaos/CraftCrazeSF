package me.cworldstar.craftcrazesf.items.armors.set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.inventory.ItemStack;

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
	
	public enum Sets {
		ADVANCED_HAZMAT(new Set("ADVANCED_HAZMAT", new ItemStack[] {}));
		
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
		CraftCrazeSF.log(Level.INFO, "validating set");
		for(ItemStack item : armor_contents) {
			for(ItemStack to_compare : items) {
				if(to_compare == null) {
					continue;
				}
				CraftCrazeSF.log(Level.INFO, "comparing set: " + item.toString() + " | AGAINST |  " + to_compare.toString());
				if(item.isSimilar(to_compare)) {
					CraftCrazeSF.log(Level.INFO, "parts: " + Integer.toString(parts) + " : " + items.size());
					CraftCrazeSF.log(Level.INFO, "yes");
					parts += 1;
				}
			}
		}
		CraftCrazeSF.log(Level.INFO, "validated? " + Boolean.toString(parts >= items.size()));
		return (parts >= items.size());
	}
	
	public void AddArmorPart(ItemStack i) {
		this.armor_contents.add(i);
	}
}
