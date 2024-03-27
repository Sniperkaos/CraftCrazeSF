package me.cworldstar.craftcrazesf.items.armors;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectiveArmor;

public abstract class AbstractArmor extends SlimefunItem implements ProtectiveArmor {

	protected PotionEffect[] effects;
	protected ProtectionType[] types;
	
	public AbstractArmor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ProtectionType[] types, @Nullable PotionEffect[] effects) {
		super(itemGroup, item, recipeType, recipe);
		
		this.effects = effects;
		this.types = types;
		
	}
	
	public void setProtectionTypes(ProtectionType[] types) {
		this.types = types;
	}
	

}
