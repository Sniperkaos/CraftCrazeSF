package me.cworldstar.craftcrazesf.items.armors.power;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PowerArmorUpgrade {
	
	private List<PowerArmorAttribute> upgrades = new ArrayList<PowerArmorAttribute>();
	private PotionEffect[] effects = new PotionEffect[] {};
	private Consumer<ItemStack> onPowerArmorUpgradeInstall;
	private Consumer<ItemStack> onPowerArmorUpgradeUninstall;
	
	public PowerArmorUpgrade() {}
	
	public List<PowerArmorAttribute> getAttributeUpgrades() {
		return this.upgrades;
	}
	
	public List<PotionEffect> getEffectUpgrades() {
		return List.of(
				effects		
		);
	}
	
	public PowerArmorUpgrade addUpgrade(PowerArmorAttribute upgrade) {
		
		this.upgrades.add(upgrade);
		
		return this;
	}
	
	//public PowerArmorUpgrade addUpgrade(PowerArmorListener listener) {
	//	
	//	this.upgrades.add(listener);
	//	
	//	return this;
	//}
	
	public PowerArmorUpgrade setPowerArmorUpgradeInstall(Consumer<ItemStack> consumer) {
		this.onPowerArmorUpgradeInstall = consumer;
		return this;
	}

	public PowerArmorUpgrade onPowerArmorUpgradeUninstall(Consumer<ItemStack> consumer) {
		this.onPowerArmorUpgradeUninstall = consumer;
		return this;
	}
	
	public Optional<Consumer<ItemStack>> getInstallListener() {
		return Optional.ofNullable(this.onPowerArmorUpgradeInstall);
	}
	
	public Optional<Consumer<ItemStack>> getUninstallListener() {
		return Optional.ofNullable(this.onPowerArmorUpgradeUninstall);
	}
	
	
}
