package me.cworldstar.craftcrazesf.items.pets;

import java.util.Optional;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
				Optional<Double> pet_integrity = pet.get_pet_integrity(e.getItem());
				if(pet_integrity.isPresent()) {
					double p_integ = pet_integrity.get();
					if(p_integ > 5.0) {
						e.getPlayer().sendMessage(Utils.formatString("&fYour pet is not hungry right now. Current pet integrity: ").concat(Double.toString(p_integ)));
					} else {
						if(ExperiencePet.this.feed(e.getItem(), e.getPlayer(), e.getPlayer().getInventory())) {
							e.getPlayer().sendMessage(Utils.formatString("&fYou've fed your pet! Current pet integrity: ").concat(Double.toString(100.0)));
							e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1, 1);
							e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.ENTITY_VILLAGER_CELEBRATE,1, 1);
						} else {
							e.getPlayer().sendMessage(Utils.formatString("&fYou don't have enough food for your pet to enjoy."));
						}
					}
				}
			}
		});
	}
	
	@Override
	public boolean onPetTrigger(ItemStack i, Player p, Inventory inventory) {
		p.sendMessage("&6> Your pet has increased your experience gain!");
		return true;
	}
}
