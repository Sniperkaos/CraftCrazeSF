package me.cworldstar.craftcrazesf.machines;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.machines.AbstractMachineBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.utils.Utils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

//todo: config
public class BlockBreaker extends AbstractMachineBlock {

	private ArrayList<ItemStack> blacklisted_items = new ArrayList<ItemStack>();
	
	
	public BlockBreaker(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		
		List<String> configged_items = CraftCrazeSF.config().getStringList("machines.block-breaker.blacklisted-items");
		//for(String itemId : configged_items) {
			//blacklisted_items.add(Slimefun.getRegistry().)
		//}
		
	}

	@Override
	protected boolean process(Block b, BlockMenu menu) {
		if(getCharge(b.getLocation()) > 100) {
			
			Block front = b.getRelative(BlockFace.NORTH);
			if(front.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE))) {
				return true;
			}
			
			return false;
		}
		return false;
	}

	@Override
	protected int getStatusSlot() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	protected void setup(BlockMenuPreset preset) {
		preset.drawBackground(Utils.IntegerRange(0, 2));
		preset.drawBackground(new int[] {5,8,9});
	}

	@Override
	protected int[] getInputSlots() {
		// TODO Auto-generated method stub
		return new int[] {3,4,6,7};
	}

	@Override
	protected int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return null;
	}

}
