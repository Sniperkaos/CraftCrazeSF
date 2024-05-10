package me.cworldstar.craftcrazesf.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageManager {

	private Map<UUID, Consumer<EntityDamageByEntityEvent>> listeners = new HashMap<UUID, Consumer<EntityDamageByEntityEvent>>();
	
	
	public DamageManager() {}
	
	public void addListener(UUID id, Consumer<EntityDamageByEntityEvent> listener) {
		this.listeners.put(id, listener);
	}
	
	public void removeListener(UUID id) {
		this.listeners.remove(id);
	}
	
	public void fire(EntityDamageByEntityEvent e) {
		listeners.entrySet().forEach((Entry<UUID, Consumer<EntityDamageByEntityEvent>> h) -> {
			h.getValue().accept(e);
		});;
	}
	
}
