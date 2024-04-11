package me.cworldstar.craftcrazesf.items.blocks;

import org.bukkit.block.Block;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.cworldstar.craftcrazesf.items.TickingBlock;
import me.cworldstar.craftcrazesf.utils.Utils;

public class RedstoneTimerBlock extends TickingBlock {

	public RedstoneTimerBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe);
	}
	
	@Override
	public void tick(Block b, SlimefunItem item) {
		for(Block side_block : Utils.getSurroundingBlocks(b)) {
			if(side_block.getState() instanceof RedstoneWire) {
				RedstoneWire wire = (RedstoneWire) side_block.getState();
				if(wire.getPower() == 8) {
					wire.setPower(0);
				} else {
					wire.setPower(8);
				}
			}
		}
	}

}
