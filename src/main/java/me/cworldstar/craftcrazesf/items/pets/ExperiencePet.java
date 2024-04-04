package me.cworldstar.craftcrazesf.items.pets;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.cworldstar.craftcrazesf.items.APet;
import me.cworldstar.craftcrazesf.utils.Utils;

public class ExperiencePet extends APet {

	public ExperiencePet(ItemGroup itemGroup, ItemStack item, String id, RecipeType ancientAltar, ItemStack[] itemStacks, ItemStack food, int eat_amount) {
		super(itemGroup, item, id, ancientAltar, itemStacks);
		this.setFood(food);
		this.setEatAmount(eat_amount);
		addItemHandler(new ItemUseHandler() {
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				
				ExperiencePet pet = ExperiencePet.this;
				
				if(pet.pet_integrity > 5.0) {
					e.getPlayer().sendMessage(Utils.formatString("&fYour pet is not hungry right now. Current pet integrity: ").concat(Double.toString(pet.pet_integrity)));
				} else {
					if(ExperiencePet.this.feed(e.getPlayer(), e.getPlayer().getInventory())) {
						e.getPlayer().sendMessage(Utils.formatString("&fYou've fed your pet! Current pet integrity: ").concat(Double.toString(pet.pet_integrity)));
						e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1, 1);
						e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.ENTITY_VILLAGER_CELEBRATE,1, 1);
					} else {
						e.getPlayer().sendMessage(Utils.formatString("&fYou don't have enough food for your pet to enjoy."));
					}
				}

			}
		});
		
	}

}
