package me.cworldstar.craftcrazesf.items.armors.power;

import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.cworldstar.craftcrazesf.CraftCrazeSF;

public class PowerArmorListener implements Listener {
	
	private UUID id;
	
	public PowerArmorListener(Consumer<EntityDamageByEntityEvent> listener) {
		
		this.id = UUID.randomUUID();
		
		CraftCrazeSF.getDamageManager().addListener(id, listener);
	}
	
	
}
