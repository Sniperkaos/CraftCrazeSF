package me.cworldstar.craftcrazesf.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.api.ui.AbstractMenu;
import me.cworldstar.craftcrazesf.api.ui.MenuClickHandler;
import me.cworldstar.craftcrazesf.items.armors.power.PowerArmorCore;
import me.cworldstar.craftcrazesf.items.armors.power.PowerArmorItemUpgrade;
import me.cworldstar.craftcrazesf.utils.Utils;

public class PowerArmorUI extends AbstractMenu {

	private ItemStack armor;
	
	private static ItemStack INFORMATIVE_PANE = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
	static {
		ItemMeta meta = INFORMATIVE_PANE.getItemMeta();
		meta.setDisplayName("");
		List<String> lore = meta.getLore();
		lore.add(Utils.formatString("&e> Place a core in the slot below."));
		lore.add(Utils.formatString("&e> Upgrades go in the 6 slots."));
		meta.setLore(lore);
		INFORMATIVE_PANE.setItemMeta(meta);
	}
	
	private static ItemStack BARRIER = new ItemStack(Material.BARRIER);
	static {
		ItemMeta meta = BARRIER.getItemMeta();
		meta.setDisplayName("");
		List<String> lore = meta.getLore();
		lore.add(Utils.formatString("&e> Place an upgrade into this slot."));
		meta.setLore(lore);
		BARRIER.setItemMeta(meta);
	}
	
	
	public PowerArmorUI(ItemStack armorPiece) {
		super(27);
		
		this.armor = armorPiece;
		
		Map<Integer, ItemStack> to_add = new HashMap<Integer, ItemStack>();
		for(int i=0; i<=26; i++) {
			to_add.put(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		}
		to_add.put(13, new ItemStack(Material.AIR));
		to_add.put(4, INFORMATIVE_PANE);
		
		int[] upgrade_slots = {19,20,21,23,24,25};

		for(int slot : upgrade_slots) {
			this.addClickableItem(slot, BARRIER, new MenuClickHandler((InventoryClickEvent e) -> 
			{
				e.getWhoClicked().sendMessage(Utils.formatString("&6WIP"));
				if(e.getCurrentItem() != null) {
					//-- you're removing an upgrade
					SlimefunItem item = SlimefunItem.getByItem(e.getCurrentItem());
					if(item instanceof PowerArmorItemUpgrade) {
						//-- item is an instance of PowerArmorItemUpgrade, apply upgrades now
						((PowerArmorItemUpgrade) item).un_apply_upgrade(armorPiece);
					}
				} else {
					//-- you're adding an upgrade
					SlimefunItem item = SlimefunItem.getByItem(e.getCurrentItem());
					if(item instanceof PowerArmorItemUpgrade) {
						//-- item is an instance of PowerArmorItemUpgrade, apply upgrades now
						((PowerArmorItemUpgrade) item).apply_upgrade(armorPiece);
					}
				}
			}));
		}
		
		
		this.addNullItemHandler(new MenuClickHandler((InventoryClickEvent e) -> {
			
			e.getWhoClicked().sendMessage(Utils.formatString("&6empty slot clicked."));	
			
			if(e.getCurrentItem() != null) {
				//-- there's an item in the slot, you're taking an item out
				SlimefunItem item = SlimefunItem.getByItem(e.getCurrentItem());
				if(item instanceof PowerArmorCore) {
					//-- item is an instance of PowerArmorCore, apply upgrades now
					((PowerArmorCore) item).un_apply_upgrade(armorPiece);
				}
			} else {
				//-- no item in the slot you're putting an item in
				SlimefunItem item = SlimefunItem.getByItem(e.getCurrentItem());
				if(item instanceof PowerArmorCore) {
					//-- item is an instance of PowerArmorCore, apply upgrades now
					((PowerArmorCore) item).apply_upgrade(armorPiece);
				}
			}
			
			armorPiece.getItemMeta().getPersistentDataContainer().set(CraftCrazeSF.createKey("power-armor-core-buffs"), PersistentDataType.STRING, "");
		}));
		this.addUnclickableItems(to_add);
	
	}

}
	