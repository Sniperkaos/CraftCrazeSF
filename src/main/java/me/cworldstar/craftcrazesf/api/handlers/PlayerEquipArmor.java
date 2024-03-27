package me.cworldstar.craftcrazesf.api.handlers;

import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;
import me.cworldstar.craftcrazesf.listeners.ArmorEquipEvent;


/**
 * This {@link ItemHandler} is triggered when a {@link Player} equips a {@link AbstractArmor}.
 * @author cworldstar
 *
 * @see ItemHandler
 * @see Player
 * @see AbstractArmor
 */
@FunctionalInterface
public interface PlayerEquipArmor extends ItemHandler {

	
	void onPlayerEquipArmor(ArmorEquipEvent e);
	
    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return PlayerEquipArmor.class;
    }
}
