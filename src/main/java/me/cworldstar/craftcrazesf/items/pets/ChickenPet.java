package me.cworldstar.craftcrazesf.items.pets;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.cworldstar.craftcrazesf.items.APet;

public class ChickenPet extends APet {

	public ChickenPet(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack food, int eat_amount) {
		super(itemGroup, item, id, recipeType, recipe);
		
		this.setFood(food);
		this.setEatAmount(eat_amount);
	}
}
