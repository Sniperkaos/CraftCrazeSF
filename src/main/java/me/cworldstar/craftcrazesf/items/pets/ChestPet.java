package me.cworldstar.craftcrazesf.items.pets;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.cworldstar.craftcrazesf.items.APet;

public class ChestPet extends APet {

	private Inventory chest = Bukkit.createInventory(null, 27);
	private UUID uuid = UUID.randomUUID();
	
	private void setInventory(Inventory i) {
		this.chest = i;
	}
	
	private void saveInventory() {
		//chest.
	}
	
	public ChestPet(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack food, int eat_amount) {
		super(itemGroup, item, id, recipeType, recipe);
		this.setFood(food);
		this.setEatAmount(eat_amount);
		
		addItemHandler(new ItemUseHandler() {

			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				
			}
			
		});
	}
}
