package me.cworldstar.craftcrazesf.items;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public abstract class TickingItem extends SlimefunItem {

	public static Map<ItemStack, Runnable> itemTickers = new HashMap<ItemStack, Runnable>();
	
	public void tickItemTickers() {
		
	}
	
	public TickingItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe);
		
		itemTickers.put(item, this.onTick());
	}
	
	public abstract Runnable onTick();

}
