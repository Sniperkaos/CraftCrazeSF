package me.cworldstar.craftcrazesf.machines;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.machines.AbstractMachineBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import net.advancedplugins.ae.api.AEAPI;

public class AdvancedDisenchanter extends AbstractMachineBlock {

	private int progress = 0;
	private int max_progress = 100;
	
	public AdvancedDisenchanter(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		
		this.energyPerTick(100);
		this.energyCapacity(2000);
	}

	
	private ArrayList<ItemStack> getItemsInSlots( BlockMenu menu, int[] slots) {
		
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		
		for(int slot : slots) {
			ItemStack item_in_slot = menu.getItemInSlot(slot);
			if(item_in_slot != null) {
				stacks.add(item_in_slot);
			}
			
		}
		
		return stacks;
	}
	
	
	@Override
	protected boolean process(Block b, BlockMenu menu) {
		
		if(getCharge(b.getLocation()) < 100) {
			return false;
		}
		
		this.progress += 10;
		if(this.progress >= this.max_progress) {
			this.progress = 0;
			// gets custom enchantments off the items with AEAPI 
			
			ArrayList<ItemStack> items = this.getItemsInSlots(menu, getInputSlots());
			if(items.size() <= 0) {
				return false;
			}
			
			for(ItemStack i : items) {
				Map<String, Integer> enchantments = AEAPI.getEnchantmentsOnItem(i);
				if(enchantments == null || enchantments.isEmpty()) {
					continue;
				}
				
				for(Entry<String, Integer> enchantment : enchantments.entrySet()) {
					String enchantmentID = enchantment.getKey();
					AEAPI.removeEnchantment(i, enchantmentID);
					menu.pushItem(AEAPI.createEnchantmentBook(enchantmentID, enchantment.getValue(), 100, 0), getOutputSlots());
				}
				menu.pushItem(i, getOutputSlots());
				
			}
		}
		
		return true;
	}

	@Override
	protected int getStatusSlot() {

		return 22;
	}

	@Override
	protected void setup(BlockMenuPreset preset) {
		// TODO Auto-generated method stub
		preset.drawBackground(new int[] {
				0,1,2,3,4,5,6,7,8,
				9,10,11,12,13,17,
				18,22,26,
				27,28,29,30,31,35,
				36,37,38,39,40,41,42,43,44
		});
	}

	@Override
	protected int[] getInputSlots() {

		return new int[] {
				19,20,21
		};
	}

	@Override
	protected int[] getOutputSlots() {

		return new int[] {
				14,15,16,
				23,24,25,
				32,33,34,
				
		};
	}

}
