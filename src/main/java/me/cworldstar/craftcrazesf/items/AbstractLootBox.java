package me.cworldstar.craftcrazesf.items;

import java.util.ArrayList;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.math.RandomUtils;
import me.cworldstar.craftcrazesf.utils.Utils;

public class AbstractLootBox extends SlimefunItem {

	private ArrayList<ItemStack> loot = new ArrayList<ItemStack>();
	
	public AbstractLootBox(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe);
		
		addItemHandler(new ItemUseHandler() {
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1F, 1F);
				e.getItem().setAmount(e.getItem().getAmount()-1);
				
				for(int i=0; i<=Utils.clamp(3, 5, RandomUtils.nextInt(5)); i++ ) {
					ItemStack drop = Utils.getRandomListObject(loot);
					e.getPlayer().getLocation().getWorld().dropItemNaturally(e.getPlayer().getLocation(), drop);
				}
			}
		});
		
		addItemHandler(new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				e.setCancelled(true);
			}
			
		});
	}
	
	public void addDrop(ItemStack drop, int chance) {
		for(int i=0; i<=chance; i++) {
			this.loot.add(drop);
		}
	}

}
