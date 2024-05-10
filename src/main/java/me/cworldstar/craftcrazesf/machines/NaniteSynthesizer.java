package me.cworldstar.craftcrazesf.machines;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import io.github.mooy1.infinitylib.machines.AbstractMachineBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.Registry;
import me.cworldstar.craftcrazesf.api.DataHolder;
import me.cworldstar.craftcrazesf.utils.Speak;
import me.cworldstar.craftcrazesf.utils.Utils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class NaniteSynthesizer extends AbstractMachineBlock implements HologramOwner, DataHolder {

	private int maxX = 5000;
	private int maxZ = 5000;
	
	
	//status items
	private static ItemStack ConstraintsMet = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
	{
		ItemMeta m = ConstraintsMet.getItemMeta();
		m.setDisplayName(Speak.format("&eDistance Requirement Met"));
		ConstraintsMet.setItemMeta(m);
	}
	
	private static ItemStack ConstraintsNotMet = new ItemStack(Material.RED_STAINED_GLASS_PANE);
	{
		ItemMeta m = ConstraintsNotMet.getItemMeta();
		m.setDisplayName(Speak.format("&cDistance Requirement Not Met"));
		ConstraintsNotMet.setItemMeta(m);
	}
	
	private static ItemStack Working = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
	{
		ItemMeta m = Working.getItemMeta();
		m.setDisplayName(Speak.format("&6Working"));
		Working.setItemMeta(m);
	}
	
	public NaniteSynthesizer(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		
		this.energyPerTick(1000);
		this.energyCapacity(10000);
		
		addItemHandler(new BlockBreakHandler(false, false) {
			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				removeHologram(e.getBlock());
			}
		});
	}
	
	private boolean isInConstraint(Block b) {
		return (maxX <= Math.abs(b.getLocation().getBlockX()) && maxZ <= Math.abs(b.getLocation().getBlockZ()));
	}
	
	
	@Override
	public boolean process(Block b, BlockMenu menu) {
		

		
		if(this.isInConstraint(b)) {
			
			double data = this.load("process", b.getLocation());
			double heat = this.load("heat", b.getLocation());
			
			if(heat >= 600) {
				// stops a runtime
				Location loc = b.getLocation();
				CraftCrazeSF.runSync(() -> {
					b.getWorld().createExplosion(loc, 16F);
					b.breakNaturally();
				});
			}
			
			ItemStack customized_working_item = Working.clone();
			ItemMeta newMeta = customized_working_item.getItemMeta();
			newMeta.setLore(Utils.CreateLore(List.of("","&cHeat: " + Double.toString(heat) + " / 600." ,"&6Processing: " + Double.toString(data) + " / 900 seconds.")));
			customized_working_item.setItemMeta(newMeta);
			menu.replaceExistingItem(4, customized_working_item);
			//if ( this.hasDarkMatter(b) && this.getDarkMatter(b) >= NaniteSynthesizer.DarkMatterConsumeRate) {
				this.save("process", b.getLocation(), data + 1);
				if (data>=900) {
					this.save("process", b.getLocation(),  1D);
					this.save("heat", b.getLocation(), heat + 10);
					menu.pushItem(Registry.NANO_PARTICLES.clone(), this.getOutputSlots());
					return true;
				}
			//}
		} else {
			menu.replaceExistingItem(this.getStatusSlot(), ConstraintsNotMet);
		}
		
		return true;
	}

	@Override
	public int getStatusSlot() {
		// TODO Auto-generated method stub
		return 4;
	}

	
	@Override
	public void setup(BlockMenuPreset preset) {
		preset.drawBackground(Utils.IntegerRange(0, 8));	
		preset.drawBackground(new int[] {9,17});
		preset.addMenuClickHandler(40,ChestMenuUtils.getEmptyClickHandler());
	}

	@Override
	public int[] getInputSlots() {
		// TODO Auto-generated method stub
		return new int[] {
				
		};
	}

	@Override
	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return new int[] {
				10, 11, 12, 13, 14, 15, 16
		};
	}

}
