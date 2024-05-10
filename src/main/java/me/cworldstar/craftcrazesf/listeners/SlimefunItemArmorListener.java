package me.cworldstar.craftcrazesf.listeners;

import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.api.handlers.PlayerEquipArmor;
import me.cworldstar.craftcrazesf.api.handlers.PlayerUnequipArmor;
import me.cworldstar.craftcrazesf.items.armors.AbstractArmor;

public class SlimefunItemArmorListener implements Listener {
	private CraftCrazeSF plugin;
	
	public SlimefunItemArmorListener() {
		this.plugin = CraftCrazeSF.getPlugin(CraftCrazeSF.class);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onArmorEquipEvent(ArmorEquipEvent e) {
		if(e.getNewArmorPiece() != null && e.getOldArmorPiece() == null) {
			SlimefunItem SFArmorPiece = SlimefunItem.getByItem(e.getNewArmorPiece());
			if(SFArmorPiece == null) {
				return;
			}
			
			if(SFArmorPiece instanceof AbstractArmor) {
				SFArmorPiece.callItemHandler(PlayerEquipArmor.class, handler -> handler.onPlayerEquipArmor(e));
			}
		} else {
			SlimefunItem SFArmorPiece = SlimefunItem.getByItem(e.getOldArmorPiece());
			CraftCrazeSF.log(Level.INFO, "unequiped");
			if(SFArmorPiece == null) {
				CraftCrazeSF.log(Level.INFO, "nope");
				return;
			}
			CraftCrazeSF.log(Level.INFO, "yup");
			if(SFArmorPiece instanceof AbstractArmor) {
				CraftCrazeSF.log(Level.INFO, "calling item handler");
				SFArmorPiece.callItemHandler(PlayerUnequipArmor.class, handler -> handler.onPlayerUnequipArmor(e));
			}
		}
	}
	
}
