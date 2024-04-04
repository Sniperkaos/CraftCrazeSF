package me.cworldstar.craftcrazesf.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import me.cworldstar.craftcrazesf.utils.Utils;

public abstract class APet extends SlimefunItem implements NotPlaceable {
	
	public double pet_integrity = 100.0; 
	private ItemStack food;
	private int eat_amount = 1;
	
	
	public APet(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, id, recipeType, recipe);
	}
	
	public void setFood(ItemStack item) {
		this.food = item;
	}

	public void setEatAmount(int amount) {
		this.eat_amount = amount;
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

	public void reduce_integrity(double amount) {
		if(this.pet_integrity < amount) {
			this.pet_integrity = 0;
		} else {
			this.pet_integrity -= amount;
		}
	}
	
	public void update_pet_integrity(double newValue) {
		this.pet_integrity = newValue;
	}
	
	public boolean has_integrity() {
		return this.pet_integrity > 0;
	}
	
	public boolean feed(Player player, Inventory i) {
		ItemStack food = this.food;
		for(ItemStack item : i.getContents()) {
			
			if(item == null) {
				continue;
			}
			
			if (item.isSimilar(food)) {
				player.sendMessage(Utils.formatString("&e[CraftCrazeSF]: Your pet has been fed!"));
				item.setAmount(item.getAmount() - this.eat_amount);
				this.pet_integrity = 100;
				return true;
			}
		}
		return false;
	}

}
