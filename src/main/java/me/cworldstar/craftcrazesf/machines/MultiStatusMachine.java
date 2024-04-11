package me.cworldstar.craftcrazesf.machines;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.machines.AbstractMachineBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class MultiStatusMachine extends AbstractMachineBlock implements HologramOwner {
	
	private ItemStack STATUS_STACK = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&eStatus", "", "&f> The machine is running at", "maximum efficiency.");
	private Map<String, ItemStack> Statuses = new HashMap<String, ItemStack>();
	
	public MultiStatusMachine(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	
		addItemHandler(new BlockBreakHandler(true, true) {
			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				MultiStatusMachine.this.removeHologram(e.getBlock());
			}		
		});
	}
	
	protected void setStatusItemStack(ItemStack i) {
		this.STATUS_STACK = i;
	}
	
	public void addStatus(ItemStack i, String identifier) {
		this.Statuses.put(identifier, i);
	}

	public abstract void onProcess(Block b, BlockMenu menu);
	
	@Override
	protected boolean process(Block b, BlockMenu menu) {
		this.onProcess(b, menu);
		menu.replaceExistingItem(getStatusSlot(), STATUS_STACK);
		return true;
	}
}
