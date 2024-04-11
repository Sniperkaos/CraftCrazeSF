package me.cworldstar.craftcrazesf.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LoreFormatter {
	
	private ItemStack toFormat;
	
	public LoreFormatter() {
		
	}
	
	public void setItem(ItemStack item) {
		this.toFormat = item;
	}
	
	@Nullable
	public ItemStack format(String pattern, String toReplace) {
		if(toFormat == null) {
			return null;
		}
		
		ItemMeta formatItemMeta = toFormat.getItemMeta();
		List<String> lore = formatItemMeta.getLore();
		List<String> newLore = new ArrayList<String>();
		for(String line : lore) {
			newLore.add(line.replaceAll(pattern, toReplace));
		}
		
		formatItemMeta.setLore(newLore);
		
		ItemStack returnable = this.toFormat.clone();
		returnable.setItemMeta(formatItemMeta);
		
		return returnable;
	}
}
