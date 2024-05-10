package me.cworldstar.craftcrazesf.items.armors.power;

import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public abstract class PowerArmorCore extends SlimefunItem {
	
	public static PowerArmorUpgrade upgrade_to_apply;
	
	public PowerArmorCore(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, PowerArmorUpgrade upgrade_to_apply) {
		super(itemGroup, item, recipeType, recipe);
		PowerArmorCore.upgrade_to_apply = upgrade_to_apply;
	}
	
	
	public void apply_upgrade(ItemStack item) {
		//-- apply attribute upgrades
		ArmorMeta meta = (ArmorMeta) item.getItemMeta();
		PowerArmorCore.upgrade_to_apply.getAttributeUpgrades().forEach((PowerArmorAttribute a)->{
			meta.addAttributeModifier(a.getAttribute(), a.build());
		});
		
		Optional<Consumer<ItemStack>> listener = PowerArmorCore.upgrade_to_apply.getInstallListener();
		if(listener.isEmpty()) {
			return;
		}
		
		listener.get().accept(item);
		
		item.setItemMeta(meta);
	}


	public void un_apply_upgrade(ItemStack armorPiece) {
		
		ArmorMeta meta = (ArmorMeta) armorPiece.getItemMeta();
		
		PowerArmorCore.upgrade_to_apply.getAttributeUpgrades().forEach((PowerArmorAttribute a)->{
			meta.removeAttributeModifier(a.getAttribute(), a.build());
		});
		
		Optional<Consumer<ItemStack>> listener = PowerArmorCore.upgrade_to_apply.getUninstallListener();
		if(listener.isEmpty()) {
			return;
		}
		
		listener.get().accept(armorPiece);
		
		armorPiece.setItemMeta(meta);
		
	}
}
