package me.cworldstar.craftcrazesf.items.armors.power;

import java.util.UUID;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.api.DataHolder;
import me.cworldstar.craftcrazesf.api.ui.AbstractMenu;
import me.cworldstar.craftcrazesf.ui.PowerArmorUI;

public class PowerArmor extends SlimefunItem implements Listener, DataHolder {

	public PowerArmor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe);
		
		addItemHandler(new ItemUseHandler() {
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				//-- cancel the event
				e.cancel();
				ItemStack clicked_item = e.getItem();
				if(clicked_item.hasItemMeta() != true) {
					return;
				}
				
				String id = clicked_item.getItemMeta().getPersistentDataContainer().get(CraftCrazeSF.createKey("menu-uuid"), PersistentDataType.STRING);
				//-- display an inventory
			
				if(id != null) {
					UUID the_actual_id = UUID.fromString(id);
					AbstractMenu.menus.get(the_actual_id).display(e.getPlayer());
				} else {
					PowerArmorUI menu = new PowerArmorUI(clicked_item);
					menu.display(e.getPlayer());
				}
			}
		});
	}
}
