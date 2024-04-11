package me.cworldstar.craftcrazesf.machines.compact;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.mooy1.infinitylib.machines.TickingMenuBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.api.dimensions.WorldBuilder;
import me.cworldstar.craftcrazesf.utils.Utils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;


@SuppressWarnings("deprecation")
public class AbstractCompactMachine extends TickingMenuBlock {

	private static ItemStack TELEPORT_ITEM = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
	private static ItemStack TELEPORT_DENIED_ITEM = new ItemStack(Material.RED_STAINED_GLASS_PANE);
	static {
		ItemMeta meta = TELEPORT_ITEM.getItemMeta();
		meta.setDisplayName(Utils.formatString("&6> Click to teleport!"));
		meta.setLore(Utils.CreateLore("", "&e<- Compact Machine Input", "&6Compact Machine Output ->"));
		TELEPORT_ITEM.setItemMeta(meta);
	}
	
	static {
		ItemMeta meta = TELEPORT_DENIED_ITEM.getItemMeta();
		meta.setDisplayName(Utils.formatString("&c> The dimension is being created."));
		meta.setLore(Utils.CreateLore("", "&4Please wait while the dimension is being generated."));
		TELEPORT_DENIED_ITEM.setItemMeta(meta);
	}
	
	public AbstractCompactMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType,
			ItemStack[] recipe, int machine_size) {
		super(itemGroup, item, recipeType, recipe);
		
		
		addItemHandler(new BlockPlaceHandler(false) {
			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				// do not allow recursive compact machines
				if(e.getBlock().getLocation().getWorld().getName().contains("compact_machine")) {
					e.getPlayer().sendMessage(Utils.formatString("&6> You cannot place a compact machine inside of a compact machine."));
					e.setCancelled(true);
					return;
				}
				
				CraftCrazeSF.warn("BEFORE_ITEM_ID");
				
				// check if the compact machine item has a saved world attached
		
				Optional<String> wid = AbstractCompactMachine.getItemSavedWorldID(e.getItemInHand());
				String world_id;
				
				CraftCrazeSF.warn("AFTER_ITEM_ID");
				
				if(wid.isPresent()) {
					// it does, load it
					CraftCrazeSF.warn("ITEM_ID_EXISTS");
					world_id = wid.get();
					CraftCrazeSF.compactMachineWorlds.put(world_id, WorldBuilder.createCompactMachineWorld(world_id, machine_size));
					CraftCrazeSF.compactMachineStatus.put(world_id, true);
					
					CraftCrazeSF.warn("SAVED_WORLDS");

				} else {
					// it doesn't, make a new one
					CraftCrazeSF.warn("ITEM_ID_DOES_NOT_EXIST");
					UUID dim_id = UUID.randomUUID();
					world_id = "compact_machine-".concat(dim_id.toString().concat("-").concat(e.getPlayer().getName()));
					CraftCrazeSF.compactMachineWorlds.put(world_id, WorldBuilder.createCompactMachineWorld(world_id, machine_size));
					CraftCrazeSF.compactMachineStatus.put(world_id, true);
					CraftCrazeSF.warn("SAVED_WORLDS");
				}
						
				
				CraftCrazeSF.warn("BLOCK_STORAGE_BEGIN");
				BlockStorage.addBlockInfo(e.getBlock(),"world_id", world_id, false);
				CraftCrazeSF.warn("BLOCK_STORAGE_END");
				CraftCrazeSF.compactMachineBlocks.put(world_id, e.getBlock());
						
				CraftCrazeSF.compactMachineIds.put(e.getBlock(), world_id);
					
				
			}
		});
		
		addItemHandler(new BlockBreakHandler(false, false) {
			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack i2, List<ItemStack> drops) {
				drops.clear();
				ItemStack item = null;
				for(ItemStack drop : e.getBlock().getDrops()) {
					CraftCrazeSF.warn(drop.getItemMeta().getDisplayName());
					SlimefunItem sfitem = SlimefunItem.getByItem(drop);
					if(sfitem != null && sfitem instanceof AbstractCompactMachine) {
						item = drop;
					}
				}
				
				if(item == null) {
					//-- couldn't find the machine????
					CraftCrazeSF.warn("The compact machine was unable to break. Machine not found in drops? If you see this, contact a developer.");
					e.getPlayer().sendMessage(Utils.formatString("&6> The compact machine was unable to break. Machine not found in drops? If you see this, contact a developer."));
					e.setCancelled(true);
					return;
				}
				
				String compactMachineID = CraftCrazeSF.compactMachineIds.get(e.getBlock());
				
				
				ItemMeta meta = item.getItemMeta();
				if(meta == null) {
					meta = Bukkit.getItemFactory().getItemMeta(item.getType());
				}
				CraftCrazeSF.warn("pdc start");
				meta.getPersistentDataContainer().set(CraftCrazeSF.createKey("world_id"), PersistentDataType.STRING, compactMachineID);
				CraftCrazeSF.warn("pdc end");
				List<String> lore = meta.getLore();
				if(!lore.contains("WORLD_GENERATED:")) {
					lore.add("");
					lore.add("WORLD_GENERATED:");
					lore.add(compactMachineID);
				}
				meta.setLore(lore);
				item.setItemMeta(meta);
				CraftCrazeSF.warn("clearing block info");
				BlockStorage.clearBlockInfo(e.getBlock());
				drops.add(item.clone());
			}
		});
		
	}
	
	
	private static Optional<String> getItemSavedWorldID(ItemStack i) {
		ItemMeta meta = i.getItemMeta();
		if(meta == null) {
			return Optional.empty();
		}
		PersistentDataContainer container = meta.getPersistentDataContainer();
		String world_id = container.get(CraftCrazeSF.createKey("world_id"), PersistentDataType.STRING);
		
		return Optional.ofNullable(world_id);
	}
	
	public void loadCompactWorld(String world_id) {
		CraftCrazeSF.compactMachineWorlds.put(world_id, WorldBuilder.loadCompactMachineWorld(world_id));
	}

	@Override
	public void onNewInstance(BlockMenu menu, Block b) {
		// hope this works :D
		CraftCrazeSF.warn("NEW INSTANCE DETECTED");
		if (BlockStorage.hasBlockInfo(b)) {
			// exists, load data
			CraftCrazeSF.warn("LOADING INSTANCE DATA");
			String storage_json = BlockStorage.getBlockInfoAsJson(b);
            CraftCrazeSF.logger.log(Level.WARNING, storage_json);
            JsonObject element = JsonParser.parseString(storage_json).getAsJsonObject();
            if(!element.has("world_id")) {
            	CraftCrazeSF.warn("WORLD ID NOT FOUND");
            	return;
            }

            
			String world_id = element.get("world_id").getAsString();
			
            if(CraftCrazeSF.compactMachineWorlds.get(world_id) != null) {
            	CraftCrazeSF.warn("WORLD_ALREADY_EXISTS");
            	return;
            }
			
			CraftCrazeSF.warn("RE-REGISTERING");
			CraftCrazeSF.compactMachineIds.put(b, world_id);
			CraftCrazeSF.compactMachineMenus.put(world_id, menu);
			CraftCrazeSF.compactMachineInventoryIds.put(menu.toInventory(), world_id);
			CraftCrazeSF.compactMachineBlocks.put(world_id, b);
			CraftCrazeSF.compactMachineWorlds.put(world_id, WorldBuilder.loadCompactMachineWorld(world_id));
			CraftCrazeSF.compactMachineStatus.put(world_id, true);
		}
    }
	
	@Override
	protected void tick(Block b, BlockMenu menu) {
		
		Optional<String> compact_machine_id = CraftCrazeSF.getCompactMachineId(b);
		
		if(compact_machine_id.isPresent()) {
			CraftCrazeSF.compactMachineMenus.put(compact_machine_id.get(), menu);
			CraftCrazeSF.compactMachineInventoryIds.put(menu.toInventory(), compact_machine_id.get());
		}
		
		if(menu.getItemInSlot(4) == null) {
			return;
		}
		
		if(menu.getItemInSlot(4).isSimilar(TELEPORT_ITEM)) {
			return;
		}
		
		Optional<String> id = CraftCrazeSF.getCompactMachineId(b);
		if(id.isPresent()) {
			Optional<Boolean> on = CraftCrazeSF.getCompactMachineStatus(id.get());
			
			if(on.isPresent() && on.get().booleanValue() == true && !(menu.getItemInSlot(4).isSimilar(TELEPORT_ITEM))) {
				CraftCrazeSF.warn("updating teleport item");
				menu.replaceExistingItem(4, TELEPORT_ITEM);
			}
		}

		
	}

	@Override
	protected void setup(BlockMenuPreset preset) {
		// TODO Auto-generated method stub
		preset.drawBackground(new int[] {0,8,4});
		preset.addItem(4, TELEPORT_DENIED_ITEM);
		preset.addMenuClickHandler(4, new AdvancedMenuClickHandler() {

			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				return false;
			}

			@Override
			public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
				BlockMenu menu = CraftCrazeSF.menuFromInventory(e.getClickedInventory());
				if(menu == null) {
					p.sendMessage(Utils.formatString("&6> Could not get ChestMenu for this block. If you see this, tell a developer!"));
					return false;
				}
				String world_id = CraftCrazeSF.compactMachineIds.get(menu.getBlock());
				if(world_id == null) {
					p.sendMessage(Utils.formatString("&6> This compact machine has no world id attached. If you see this, tell a developer!"));
					return false;
				}
				World to_teleport = CraftCrazeSF.compactMachineWorlds.get(world_id);
				boolean is_enabled = CraftCrazeSF.compactMachineStatus.get(world_id);
				if(!is_enabled) {
					p.sendMessage(Utils.formatString("&6> This compact machine is still creating the world!"));
					return false;
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						p.teleport(to_teleport.getSpawnLocation().add(0, 1, 0));
					}
				}.runTask(CraftCrazeSF.getPlugin(CraftCrazeSF.class));
				
				return false;
			}
			
		});
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
