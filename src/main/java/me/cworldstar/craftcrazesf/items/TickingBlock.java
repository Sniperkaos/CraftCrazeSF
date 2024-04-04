package me.cworldstar.craftcrazesf.items;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;

@SuppressWarnings("deprecation")
public abstract class TickingBlock extends SlimefunItem {

	protected boolean in_sync = false;
	
	public TickingBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe);
		
		addItemHandler(new BlockTicker() {

			@Override
			public boolean isSynchronized() {
				return TickingBlock.this.in_sync;
			}
			
			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				TickingBlock.this.tick(b,item);
			}
			
		});
	}
	
	public void setSynchronized(boolean b) {
		this.in_sync = b;
	}
	
	abstract void tick(Block b, SlimefunItem item);

}
