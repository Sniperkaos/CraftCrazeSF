package me.cworldstar.craftcrazesf.api.handlers;

import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;

public interface ItemTicker extends ItemHandler  {

	void tick();
	
	@Override
	default Class<? extends ItemHandler> getIdentifier() {
		// TODO Auto-generated method stub
		return ItemTicker.class;
	}
}
