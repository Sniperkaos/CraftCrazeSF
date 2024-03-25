package me.cworldstar.craftcrazesf.machines;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.machines.AbstractMachineBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class QuantumServer extends AbstractMachineBlock {

	public QuantumServer(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean process(Block b, BlockMenu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int getStatusSlot() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void setup(BlockMenuPreset preset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int[] getInputSlots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return null;
	}

}
