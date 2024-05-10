package me.cworldstar.craftcrazesf.items.armors.power.cores;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.cworldstar.craftcrazesf.items.armors.power.PowerArmorAttribute;
import me.cworldstar.craftcrazesf.items.armors.power.PowerArmorCore;
import me.cworldstar.craftcrazesf.items.armors.power.PowerArmorUpgrade;

public class NanoCore extends PowerArmorCore {

	public static PowerArmorUpgrade upgrade = new PowerArmorUpgrade();
	static {
		
		PowerArmorAttribute HealthModifier = new PowerArmorAttribute()
			.setAmount(20.0)
			.setAttribute(Attribute.GENERIC_MAX_HEALTH)
			.setName("NANO_CORE_MAX_HEALTH_MODIFIER")
			.setOperation(Operation.ADD_NUMBER);
		
		PowerArmorAttribute ArmorModifier = new PowerArmorAttribute()
				.setAmount(20.0)
				.setAttribute(Attribute.GENERIC_ARMOR)
				.setName("NANO_CORE_ARMOR_MODIFIER")
				.setOperation(Operation.ADD_NUMBER);
		
		PowerArmorAttribute DamageModifier = new PowerArmorAttribute()
				.setAmount(20.0)
				.setAttribute(Attribute.GENERIC_MAX_HEALTH)
				.setName("NANO_CORE_MAX_HEALTH_MODIFIER")
				.setOperation(Operation.ADD_NUMBER);
		
		upgrade.addUpgrade(HealthModifier);
		upgrade.addUpgrade(ArmorModifier);
		upgrade.addUpgrade(DamageModifier);

	}
	
	public NanoCore(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		

		
		super(itemGroup, item, recipeType, recipe, upgrade);
	}

}
