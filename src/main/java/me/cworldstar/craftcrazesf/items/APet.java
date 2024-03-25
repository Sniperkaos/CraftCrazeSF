package me.cworldstar.craftcrazesf.items;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public abstract class APet extends SlimefunItem {
	
	public double pet_integrity = 100.0; 
	public ItemStack food;
	
	public APet(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, id, recipeType, recipe);
		// TODO Auto-generated constructor stub
	}
	
	public void setFood(ItemStack item) {
		this.food = item;
	}
	
	public ItemStack getFood() {
		return this.food;
	}
	
	public void reduce_integrity(int amount) {
		
		if(this.pet_integrity < amount) {
			this.pet_integrity = 0;
		} else {
			this.pet_integrity -= (double) amount;
		}

	}

	public boolean has_integrity() {
		return this.pet_integrity > 0;
	}
	
	public void feed(Inventory i) {
		ItemStack food = this.food;
		for(ItemStack item : i.getContents()) {
			if (item.isSimilar(food)) {
				item.setAmount(item.getAmount() - 1);
				this.pet_integrity = 100;
			}
		}
	}

}
