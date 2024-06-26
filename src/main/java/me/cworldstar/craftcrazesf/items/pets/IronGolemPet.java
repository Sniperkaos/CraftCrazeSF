package me.cworldstar.craftcrazesf.items.pets;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;

public class IronGolemPet extends SlimefunItem implements NotPlaceable {

	public double pet_integrity = 100.0; 
	public ItemStack food;
	public boolean enabled = false;
	
	public IronGolemPet(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack food) {
		super(itemGroup, item, id, recipeType, recipe);
		this.food = food;
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
			
			if(item == null) {
				continue;
			}
			
			if (item.isSimilar(food)) {
				item.setAmount(item.getAmount() - 1);
				this.pet_integrity = 100;
			}
		}
	}
	
}
