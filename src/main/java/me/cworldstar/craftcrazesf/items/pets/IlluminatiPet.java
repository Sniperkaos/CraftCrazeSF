package me.cworldstar.craftcrazesf.items.pets;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;

public class IlluminatiPet extends SlimefunItem {

	public double pet_integrity = 100.0; 
	public ItemStack food;
	
	public IlluminatiPet(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe);
		addItemHandler(new ItemUseHandler() {

			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				// TODO Auto-generated method stub
				List<SlimefunItem> items = Slimefun.getRegistry().getAllSlimefunItems();
				int random = (int) (Math.random() * items.size());
				SlimefunItem exists = items.get(random);
				if(exists == null) {
					return;
				}
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
				e.getPlayer().sendMessage("§2".concat(exists.getItemName()).concat(" confirmed!"));
				e.getPlayer().getInventory().addItem(exists.getItem());
				
				
			}
			
		});
		
		// TODO Auto-generated constructor stub
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
