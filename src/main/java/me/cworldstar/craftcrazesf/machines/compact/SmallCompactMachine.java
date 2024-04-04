package me.cworldstar.craftcrazesf.machines.compact;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class SmallCompactMachine extends AbstractCompactMachine {

	public SmallCompactMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) 
	{
		super(itemGroup, item, recipeType, recipe, 6);
	}

}
