package me.cworldstar.craftcrazesf.items.armors;

import java.util.logging.Level;

import javax.annotation.Nullable;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.api.handlers.PlayerEquipArmor;
import me.cworldstar.craftcrazesf.listeners.ArmorEquipEvent;

public class AdvancedHazmat extends AbstractArmor {

	public AdvancedHazmat(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
			@Nullable ProtectionType[] types, @Nullable PotionEffect[] effects) {
		super(itemGroup, item, recipeType, recipe, types, effects);
		
		addItemHandler(new PlayerEquipArmor() {
			@Override
			public void onPlayerEquipArmor(ArmorEquipEvent e) {
				CraftCrazeSF.logger.log(Level.INFO, "handler triggered");
			}
		});
	}

	@Override
	public ProtectionType[] getProtectionTypes() {
		// TODO Auto-generated method stub
		return this.types;
	}

	@Override
	public boolean isFullSetRequired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public NamespacedKey getArmorSetId() {
		// TODO Auto-generated method stub
		return CraftCrazeSF.createKey("AdvancedHazmat");
	}

}
