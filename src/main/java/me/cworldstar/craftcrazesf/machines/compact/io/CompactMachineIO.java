package me.cworldstar.craftcrazesf.machines.compact.io;

import java.util.List;
import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.mooy1.infinitylib.machines.TickingMenuBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.utils.Utils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

@SuppressWarnings("deprecation")
public class CompactMachineIO extends TickingMenuBlock {

	public CompactMachineIO(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		
		addItemHandler(new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				if(!e.getBlock().getWorld().getName().contains("compact_machine")) {
					
					e.getPlayer().sendMessage(Utils.formatString("&6> This block can only be placed in a compact machine world."));
					e.setCancelled(true);
					return;
				}	
				
				Optional<BlockMenu> CompactMachineMenuOptional = CraftCrazeSF.menuFromWorld(e.getBlock().getWorld());
				if(CompactMachineMenuOptional.isPresent()) {

					JsonObject element = JsonParser.parseString(BlockStorage.getBlockInfoAsJson(CompactMachineMenuOptional.get().getBlock())).getAsJsonObject();
					JsonElement io = element.get("IO");
					if(!io.isJsonNull() && io.getAsBoolean() == true) {
						e.getPlayer().sendMessage(Utils.formatString("&6> You already have an IO in this compact machine!"));
						e.setCancelled(true);
						return;
					}
					
					BlockStorage.addBlockInfo(CompactMachineMenuOptional.get().getBlock(), "IO", Boolean.toString(true));
				}
				

			}
			
			
			
		});
		
		addItemHandler(new BlockBreakHandler(false, false) {

			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				Optional<BlockMenu> CompactMachineMenuOptional = CraftCrazeSF.menuFromWorld(e.getBlock().getWorld());
				if(CompactMachineMenuOptional.isPresent()) {
					BlockStorage.addBlockInfo(CompactMachineMenuOptional.get().getBlock(), "IO", Boolean.toString(false));
				}
			}
			
		});
		
	}

	@Override
	protected void tick(Block b, BlockMenu menu) {
		Optional<BlockMenu> CompactMachineMenuOptional = CraftCrazeSF.menuFromWorld(b.getWorld());
		
		if(CompactMachineMenuOptional.isPresent()) {
			BlockMenu CompactMachineMenu = CompactMachineMenuOptional.get();
			// handle output
			for(int slot : getOutputSlots()) {
				if(menu.getItemInSlot(slot) != null) {
					CompactMachineMenu.pushItem(menu.getItemInSlot(slot), new int[] {5,6,7});
					menu.consumeItem(slot);
				}
			}
			//handle input
			for(int slot : getInputSlots()) {
				if(CompactMachineMenu.getItemInSlot(slot) != null) {
					menu.pushItem(CompactMachineMenu.getItemInSlot(slot), getInputSlots());
					CompactMachineMenu.consumeItem(slot);
				}
			}
		}
		
	}

	@Override
	protected void setup(BlockMenuPreset preset) {
		// TODO Auto-generated method stub
		preset.drawBackground(new int[] {0,8,4});
	}

	@Override
	protected int[] getInputSlots() {
		// TODO Auto-generated method stub
		return new int[] {1,2,3};
	}

	@Override
	protected int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return new int[] {5,6,7};
	}

}
