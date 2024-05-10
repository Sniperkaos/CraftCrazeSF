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
import me.cworldstar.craftcrazesf.items.armors.set.Set;
import me.cworldstar.craftcrazesf.listeners.ArmorEquipEvent;

/**
 * AbstractArmor class for slimefun.
 * Pretty much just armor sets,
 * but baked into slimefun's item handler.
 * 
 * @author cworldstar
 *
 */

public abstract class AbstractArmor extends SlimefunItem implements ProtectiveArmor {

	protected PotionEffect[] effects;
	protected ProtectionType[] types;
	
	/**
	 * 
	 * Creates a new AbstractArmor item.
	 * 
	 * @see SlimefunItem
	 * 
	 * @param ItemGroup group
	 * @param SlimefunItemStack item
	 * @param RecipeType recipeType
	 * @param ItemStack[] recipe
	 * @param ProtectionType[] types
	 * @param PotionEffect[] effects
	 */
	public AbstractArmor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ProtectionType[] types, @Nullable PotionEffect[] effects) {
		super(itemGroup, item, recipeType, recipe);
		
		this.effects = effects;
		this.types = types;
		
	}
	
	abstract void equip(ArmorEquipEvent e);
	
	public void setProtectionTypes(ProtectionType[] types) {
		this.types = types;
	}
	
	public void addToSet(Set s) {
		s.AddArmorPart(this.getItem());
	}
}
