package me.cworldstar.craftcrazesf.machines.basic;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.machines.MultiStatusMachine;
import me.cworldstar.craftcrazesf.utils.LoreFormatter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class CircuitFabricator extends MultiStatusMachine {

	private static ItemStack STATUS_BASE = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&6Machine Status:", "","&e> |percent|% completed.");
	private LoreFormatter formatter = CraftCrazeSF.getLoreFormatter();
	
	
	public CircuitFabricator(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);

		this.energyCapacity(2000);
		this.energyPerTick(100);
		
	}

	@Override
	public void onProcess(Block b, BlockMenu menu) {
		
		formatter.setItem(STATUS_BASE);
		
		JsonObject object = JsonParser.parseString(BlockStorage.getBlockInfoAsJson(b)).getAsJsonObject();
		JsonElement element = object.get("process");
		
		this.setStatusItemStack(formatter.format("|percent|", element.getAsString()));
		if(element.getAsInt() >= 100) {
			menu.pushItem(SlimefunItems.BASIC_CIRCUIT_BOARD, getOutputSlots());
		} else if(element.getAsInt() <= 0) {
			for(int slot : getInputSlots()) {
				if (menu.getItemInSlot(slot).isSimilar(SlimefunItems.STEEL_PLATE)) {
					menu.consumeItem(slot);
					BlockStorage.addBlockInfo(b, "process", Integer.toString(element.getAsInt() + 10));
					return;
				}
			}
		} else {
			BlockStorage.addBlockInfo(b, "process", Integer.toString(element.getAsInt() + 10));
		}
		
		return;
	}
	
	@Override
	public void onNewInstance(BlockMenu menu, Block b) {
		if(BlockStorage.hasBlockInfo(b)) {
			return;
		}
		BlockStorage.addBlockInfo(b, "process", Integer.toString(0));
	}

	@Override
	protected int getStatusSlot() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	protected void setup(BlockMenuPreset preset) {
		preset.drawBackground(new int[] {0,8,4});
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
