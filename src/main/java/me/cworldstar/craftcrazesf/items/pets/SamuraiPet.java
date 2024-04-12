package me.cworldstar.craftcrazesf.items.pets;

import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.items.APet;
import me.cworldstar.craftcrazesf.items.DataStorageItem;

public class SamuraiPet extends APet implements DataStorageItem {

	public SamuraiPet(ItemGroup itemGroup, SlimefunItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, id, recipeType, recipe);

		this.setFood(new ItemStack(Material.IRON_SWORD));
		this.setEatAmount(1);
		
	}
	
	@Override
	public boolean onPetTrigger(ItemStack i, Player p, Inventory inventory) {
		Optional<String> attacks = this.load(i,"attacks");
		CraftCrazeSF.warn("attacking");
		if(attacks.isPresent()) {
			
			ItemMeta meta = i.getItemMeta();
			List<String> lore = meta.getLore();
			lore.replaceAll(string -> 
				string.replaceAll("%attacks%", attacks.get())
			);
			meta.setLore(lore);
			i.setItemMeta(meta);
			
			CraftCrazeSF.warn(attacks.get());
			if(Integer.parseInt(attacks.get()) > 8) {
				return true;
			} else {
				this.store(i, "attacks", Integer.toString(Integer.parseInt(attacks.get()) + 1));
				return false;
			}
		} 
		CraftCrazeSF.warn("not present");

		
		this.store(i, "attacks", "1");
		
		return false;
	}

}
