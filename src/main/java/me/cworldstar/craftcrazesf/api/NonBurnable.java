package me.cworldstar.craftcrazesf.api;

import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

import me.cworldstar.craftcrazesf.CraftCrazeSF;

public interface NonBurnable {
	default void applyNonBurnable(CraftCrazeSF plugin, Block b) {
		b.setMetadata("cannot_burn", new FixedMetadataValue(plugin, true));
	}
}
