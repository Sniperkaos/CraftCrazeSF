package me.cworldstar.craftcrazesf.items;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.utils.Utils;

public abstract class APet extends SlimefunItem implements NotPlaceable {
	
	private ItemStack food;
	private int eat_amount = 1;
	
	
	public APet(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, id, recipeType, recipe);
		
		addItemHandler(new BlockPlaceHandler(false) {
			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				e.setCancelled(true);
				e.getPlayer().sendMessage("&6> You cannot place a pet!");
			}
		});
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
	
	public void reduce_integrity(ItemStack item, int amount) {
		
		ItemMeta meta = item.getItemMeta();
		if(meta == null) {
			CraftCrazeSF.warn("ItemMeta is null. This item cannot be a pet!");
			return;
		}
		
		double pet_integrity = meta.getPersistentDataContainer().get(CraftCrazeSF.createKey("pet-integrity"), PersistentDataType.DOUBLE);
		
		if(pet_integrity < amount) {
			this.update_pet_integrity(item, 0.0);
		} else {
			this.update_pet_integrity(item, (double) (-amount));
		}

	}

	public void reduce_integrity(ItemStack item, double amount) {
		
		ItemMeta meta = item.getItemMeta();
		if(meta == null) {
			CraftCrazeSF.warn("ItemMeta is null. This item cannot be a pet!");
			return;
		}
		
		double pet_integrity = meta.getPersistentDataContainer().get(CraftCrazeSF.createKey("pet-integrity"), PersistentDataType.DOUBLE);
		
		
		
		if(pet_integrity < amount) {
			this.update_pet_integrity(item, 0.0);
		} else {
			this.update_pet_integrity(item, (double) (-amount));
		}
	}
	
	public void update_pet_integrity(ItemStack item, double newValue) {
		
		ItemMeta meta = item.getItemMeta();
		if(meta == null) {
			CraftCrazeSF.warn("ItemMeta is null. This item cannot be a pet!");
			return;
		}
		
		meta.getPersistentDataContainer().set(CraftCrazeSF.createKey("pet-integrity"), PersistentDataType.DOUBLE, newValue);
	}
	
	public Optional<Double> get_pet_integrity(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if(meta == null) {
			CraftCrazeSF.warn("ItemMeta is null. This item cannot be a pet!");
			return Optional.empty();
		}
		
		return Optional.ofNullable(meta.getPersistentDataContainer().get(CraftCrazeSF.createKey("pet-integrity"), PersistentDataType.DOUBLE));
	}
	
	public boolean onPetTrigger(ItemStack i, Player p, Inventory inventory) {
		return true;
	}
	
	public boolean has_integrity(ItemStack item) {
		
		Optional<Double> pet_integrity = this.get_pet_integrity(item);
		if(pet_integrity.isPresent() && pet_integrity.get() > 0) {
			return true;
		}
		
		return false;
	}
	
	public boolean feed(ItemStack pet, Player player, Inventory i) {
		ItemStack food = this.food;
		for(ItemStack item : i.getContents()) {
			
			if(item == null) {
				continue;
			}
			
			if (item.isSimilar(food)) {
				player.sendMessage(Utils.formatString("&e[CraftCrazeSF]: Your pet has been fed!"));
				item.setAmount(item.getAmount() - this.eat_amount);
				this.update_pet_integrity(pet, 100);
				return true;
			}
		}
		return false;
	}

}
