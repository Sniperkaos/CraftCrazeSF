package me.cworldstar.craftcrazesf.machines;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import io.github.mooy1.infinitylib.machines.TickingMenuBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.math.RandomUtils;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.api.NonBurnable;
import me.cworldstar.craftcrazesf.utils.Utils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class AutomaticWasher extends TickingMenuBlock implements HologramOwner, NonBurnable {
    private final ItemStack[] dusts = new ItemStack[] {
            SlimefunItems.IRON_DUST,
            SlimefunItems.GOLD_DUST,
            SlimefunItems.COPPER_DUST,
            SlimefunItems.TIN_DUST,
            SlimefunItems.ZINC_DUST,
            SlimefunItems.ALUMINUM_DUST,
            SlimefunItems.MAGNESIUM_DUST,
            SlimefunItems.LEAD_DUST,
            SlimefunItems.SILVER_DUST
        };
	private String last_message = "Washing";
	private int repeat = 0;
    private static final BlockFace[] possibleFaces = {
            BlockFace.UP,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };
    
	public AutomaticWasher(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	
		
		addItemHandler(new BlockBreakHandler(false, false) {
			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				// TODO Auto-generated method stub
				AutomaticWasher.this.removeHologram(e.getBlock());
			}
			
		});
		
		addItemHandler(new BlockPlaceHandler(false) {
			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				AutomaticWasher.this.applyNonBurnable(CraftCrazeSF.getPlugin(CraftCrazeSF.class), e.getBlock());
			}
		});
		
	}

	@Override
	protected boolean synchronous() {
		return true;
	}
	
	
	//-- works without chests
	public void push(Map<Integer, ItemStack> itemsInInput, BlockMenu toPush) {
		for(Entry<Integer, ItemStack> s : itemsInInput.entrySet()) {
			if(s.getValue() != null ) {
				SlimefunItem sfItem = SlimefunItem.getByItem(s.getValue());
				if(sfItem != null && sfItem.getItem().isSimilar(SlimefunItems.SIFTED_ORE)) {
					toPush.consumeItem(s.getKey(), 1);
					toPush.pushItem(this.dusts[RandomUtils.nextInt(this.dusts.length)].clone(), getOutputSlots());
				}
			}
		}
	}
	
	//-- works with chests
	public void push(Map<Integer, ItemStack> itemsInInput, BlockMenu toPush, Inventory to) {
		
		Set<Entry<Integer, ItemStack>> set = itemsInInput.entrySet();
		if(set == null) {
			return;
		}
		for(Entry<Integer, ItemStack> s : set) {
			if(s.getValue() != null && s.getKey() != null) {
				SlimefunItem sfItem = SlimefunItem.getByItem(s.getValue());
				if(sfItem != null && sfItem.getItem().isSimilar(SlimefunItems.SIFTED_ORE)) {
					toPush.consumeItem(s.getKey(), 1);
					to.addItem(this.dusts[RandomUtils.nextInt(this.dusts.length)].clone());
				}
//				} else {
//					toPush.pushItem(this.dusts[RandomUtils.nextInt(this.dusts.length)].clone(), getOutputSlots());
//				}
			}
		}
	}
	
	@Override
	public void tick(Block b, BlockMenu menu) {
		// requires a fire lit under it.
		
		World w = b.getWorld();
		Block underneath = b.getRelative(BlockFace.DOWN);
		Location BlockLocation = underneath.getLocation();
		//-- check below block
		if(underneath.getBlockData().getMaterial() == Material.FIRE) {
			//-- it's fire, so we can continue
			Map<Integer, ItemStack> itemsInInput = this.getItemsInInput(menu);
			if(itemsInInput.size() == 0) {
				//-- reset hologram repeat
				this.repeat = 0;
				this.last_message = "Washing";
				this.updateHologram(b, "Ready!");
				return;
			}
			
			this.updateHologram(b, this.last_message.concat("."));
			this.last_message = this.last_message.concat(".");
			this.repeat += 1;
			if(this.repeat >= 4) {
				//-- reset hologram repeat
				this.repeat = 0;
				this.last_message = "Washing";
			}
			
			boolean pushed = false;
			
			for(BlockFace face : AutomaticWasher.possibleFaces) {
				if(b.getRelative(face).getBlockData().getMaterial().equals(Material.CHEST)) {
					//-- output to the chest
					Block relative = b.getRelative(face);
					BlockState state = relative.getState();
					if(state instanceof Chest) {
						AutomaticWasher.this.push(itemsInInput, menu, ((Chest) state).getInventory());
						pushed = true;
					} else {
						
					}
				}
			}
			
			if (pushed == false) {
				AutomaticWasher.this.push(itemsInInput, menu);
			}

			
			//-- 10% remove the fire
			if(Math.random() * 10 > 9) {
				
				//-- break the fire block underneath
				if(underneath.getBlockData().getMaterial() != Material.AIR) {
					//-- stupid shit about asynchronous block removals go fuck yourself spigot
					new BukkitRunnable() {
						@Override
						public void run() {
							underneath.setType(Material.AIR);
							w.playSound(BlockLocation, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
							for(int i=1; i<10; i++) {
								w.spawnParticle(Particle.LANDING_LAVA, BlockLocation.getX(), BlockLocation.getY(), BlockLocation.getZ(), 0, Math.toRadians(360/i), Math.toRadians(360/i), Math.toRadians(360/i));
							}
							
							//-- notify players
							for(Entity e : w.getNearbyEntities(b.getLocation(), 8, 8, 8)) {
								if(e instanceof Player) {
									e.sendMessage(Utils.formatString("&2[Automatic Washer]: &fThe fire has extinguished."));
								}
							}
						}
						
					}.runTask(CraftCrazeSF.getPlugin(CraftCrazeSF.class));
				}
			}
			//-- try to gc
			itemsInInput.clear();
		} else {
			this.updateHologram(b, "Missing fire below!");
		}
		
	}
	
	
	private Map<Integer, ItemStack> getItemsInInput(BlockMenu menu) {
		
		Map<Integer, ItemStack> returns = new HashMap<Integer, ItemStack>();
		
		for(int slot : this.getInputSlots()) {
			
			ItemStack item = menu.getItemInSlot(slot);
			if(item != null) {
				returns.put(slot, item);
			}
		}
		
		return returns;
		
	}
	
	@Override
	public void setup(BlockMenuPreset preset) {
		
		preset.drawBackground(Utils.IntegerRange(0, 9));
		preset.drawBackground(new int[] {
				12,13,14,17,18,21,22,23,26,27,28,29,30,31,32,33,34,35
		});
	}

	@Override
	public int[] getInputSlots() {
		
		return new int[] {
				10,11,
				19,20
		};
	}

	@Override
	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return new int[] {
				15,16,
				24,25
		};
	}
}
