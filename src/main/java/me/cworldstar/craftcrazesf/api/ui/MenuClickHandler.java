package me.cworldstar.craftcrazesf.api.ui;

import java.util.function.Consumer;

import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuClickHandler {

	private Consumer<InventoryClickEvent> consumer;	
	
	public MenuClickHandler(Consumer<InventoryClickEvent> toRun) {
		this.consumer = toRun;
	}
	
	public void run(InventoryClickEvent e) {
		this.consumer.accept(e);
	}

}
