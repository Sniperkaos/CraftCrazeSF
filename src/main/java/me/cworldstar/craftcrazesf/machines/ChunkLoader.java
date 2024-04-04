package me.cworldstar.craftcrazesf.machines;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.mooy1.infinitylib.machines.TickingMenuBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.cworldstar.craftcrazesf.utils.Utils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

@SuppressWarnings("deprecation")
public class ChunkLoader extends TickingMenuBlock {

	private boolean loaded = false;
	private BlockMenu menu;
	private static ItemStack STATUS_ITEM_LOADED;
	private static ItemStack STATUS_ITEM_UNLOADED;
	
	static {
		STATUS_ITEM_LOADED = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta meta = STATUS_ITEM_LOADED.getItemMeta();
		meta.setDisplayName(Utils.formatString("&6&lLoading."));
		meta.setLore(null);
		STATUS_ITEM_LOADED.setItemMeta(meta);
	
		STATUS_ITEM_UNLOADED = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta meta2 = STATUS_ITEM_UNLOADED.getItemMeta();
		meta2.setDisplayName(Utils.formatString("&c&lNot Loading."));
		meta2.setLore(null);
		STATUS_ITEM_UNLOADED.setItemMeta(meta2);
		
	}
	
	
	
	private Chunk block_chunk;
	
	public ChunkLoader(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		
		addItemHandler(new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				ChunkLoader.this.loaded = true;
				ChunkLoader.this.block_chunk = e.getBlock().getChunk();
				ChunkLoader.this.loadChunk(block_chunk, true);
			}
			
		});
		
		addItemHandler(new BlockBreakHandler(false, false) {

			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				ChunkLoader.this.loaded = false;
				ChunkLoader.this.block_chunk = e.getBlock().getChunk();
				ChunkLoader.this.loadChunk(block_chunk, false);
			}
			
		});
		
	}
	
	protected void loadChunk(Chunk c, boolean load) {
		if(load == true) {
			c.setForceLoaded(true);
		} else {
			c.setForceLoaded(false);
		}
	}
	
	private void toggle() {
		this.loaded = !this.loaded;
		this.loadChunk(block_chunk, loaded);
	}
	
	@Override
	public void onNewInstance(BlockMenu menu, Block b) {
		
	}
	

	@Override
	protected void tick(Block b, BlockMenu menu) {
		this.menu = menu;
	}

	@Override
	protected void setup(BlockMenuPreset preset) {
		// TODO Auto-generated method stub
		preset.drawBackground(Utils.IntegerRange(0, 8));
		preset.drawBackground(STATUS_ITEM_UNLOADED, new int[] {4});
		preset.addMenuClickHandler(4, new MenuClickHandler() {

			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				ChunkLoader.this.toggle();
				if(ChunkLoader.this.loaded) {
					ChunkLoader.this.menu.replaceExistingItem(4, STATUS_ITEM_LOADED);
				} else {
					ChunkLoader.this.menu.replaceExistingItem(4, STATUS_ITEM_UNLOADED);
				}
				return false;
			}
			
		});
		
	}

	@Override
	protected int[] getInputSlots() {
		// TODO Auto-generated method stub
		return new int[] {};
	}

	@Override
	protected int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return new int[] {};
	}

}
