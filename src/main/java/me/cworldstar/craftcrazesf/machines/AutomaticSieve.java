package me.cworldstar.craftcrazesf.machines;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Effect;
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
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.OutputChest;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.NetherGoldPan;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.api.NonBurnable;
import me.cworldstar.craftcrazesf.utils.Utils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;


@SuppressWarnings("deprecation")
public class AutomaticSieve extends TickingMenuBlock implements HologramOwner, NonBurnable {
	
    private final GoldPan goldPan = SlimefunItems.GOLD_PAN.getItem(GoldPan.class);
    private final NetherGoldPan netherGoldPan = SlimefunItems.NETHER_GOLD_PAN.getItem(NetherGoldPan.class);
	private String last_message = "Working";
    private static final BlockFace[] possibleFaces = {
            BlockFace.UP,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
        };
    
	public AutomaticSieve(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	
		
		addItemHandler(new BlockBreakHandler(false, false) {
			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				// TODO Auto-generated method stub
				AutomaticSieve.this.removeHologram(e.getBlock());
			}
			
		});
		
		addItemHandler(new BlockPlaceHandler(false) {
			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				AutomaticSieve.this.applyNonBurnable(CraftCrazeSF.getPlugin(CraftCrazeSF.class), e.getBlock());
			}
		});
		
	}

	@Override
	protected boolean synchronous() {
		return true;
	}
	
	public void push(Map<Integer, ItemStack> itemsInInput, BlockMenu toPush) {
		for(Entry<Integer, ItemStack> s : itemsInInput.entrySet()) {
			if(s.getValue() != null) {
				if(goldPan.isValidInput(s.getValue())) {
					toPush.consumeItem(s.getKey(), 1);
					toPush.pushItem(goldPan.getRandomOutput().clone(), getOutputSlots());	
				} else if(netherGoldPan.isValidInput(s.getValue())) {
					toPush.consumeItem(s.getKey(), 1);
					toPush.pushItem(netherGoldPan.getRandomOutput().clone(), getOutputSlots());
				}
			}
		}
	}
	
	public void push(Map<Integer, ItemStack> itemsInInput, BlockMenu toPush, Inventory to) {
		
		Set<Entry<Integer, ItemStack>> set = itemsInInput.entrySet();
		if(set == null) {
			return;
		}
		
		for(Entry<Integer, ItemStack> s : set) {
			if(s.getValue() != null && s.getKey() != null) {
				if(goldPan.isValidInput(s.getValue())) {
					toPush.consumeItem(s.getKey(), 1);
					if(to != null) {
						to.addItem(goldPan.getRandomOutput().clone());
					} else {
						toPush.pushItem(goldPan.getRandomOutput().clone());
					}
						
				} else if(netherGoldPan.isValidInput(s.getValue())) {
					toPush.consumeItem(s.getKey(), 1);
					if(to != null) {
						to.addItem(netherGoldPan.getRandomOutput().clone());
					} else {
						toPush.pushItem(netherGoldPan.getRandomOutput().clone());
					}
				}
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
			this.updateHologram(b, this.last_message.concat("."));
			if(this.last_message.length() > 10) {
				this.last_message = "Working";
			}
	
			//-- it's fire, so we can continue
			Map<Integer, ItemStack> itemsInInput = this.getItemsInInput(menu);
			if(itemsInInput.size() == 0) {
				return;
			}
			
			boolean pushed = false;
			
			for(BlockFace face : AutomaticSieve.possibleFaces) {
				if(b.getRelative(face).getBlockData().getMaterial().equals(Material.CHEST)) {
					//-- output to the chest
					Block relative = b.getRelative(face);
					BlockState state = relative.getState();
					if(state instanceof Chest) {
						AutomaticSieve.this.push(itemsInInput, menu, ((Chest) state).getInventory());
						pushed = true;
					} else {
						
					}
				}
			}
			
			if (pushed == false) {
				AutomaticSieve.this.push(itemsInInput, menu);
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
									e.sendMessage(Utils.formatString("&2[Automatic Sieve]: &fThe fire has extinguished."));
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
