package me.cworldstar.craftcrazesf.machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.mooy1.infinitylib.machines.AbstractMachineBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.utils.Utils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

@SuppressWarnings("deprecation")
public class ExperienceGenerator extends AbstractMachineBlock implements HologramOwner {
	
	private double to_add = 0.02;
	private int max_experience = 1000;
	
	
	public ExperienceGenerator(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		
		this.to_add = CraftCrazeSF.config().getDouble("machines.experience-generator.experience-per-tick");
		this.max_experience = CraftCrazeSF.config().getInt("machines.experience-generator.max-experience-held");
		this.energyPerTick(100);
		this.energyCapacity(2000);
		
		ExperienceGenerator self = this;
		
		addItemHandler(new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				BlockStorage.addBlockInfo(e.getBlock(), "stored-experience", Double.toString(0.0));
			}

		});
		
		addItemHandler(new BlockBreakHandler(false, false) {
			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				// TODO Auto-generated method stub
				BlockStorage.clearBlockInfo(e.getBlock());
				self.removeHologram(e.getBlock());
			}
			
		});
		

		
	}
	
	@Override
	public boolean process(Block b, BlockMenu menu) {
		
		String storage_json = BlockStorage.getBlockInfoAsJson(b);
        CraftCrazeSF.warn(storage_json);
        JsonObject element = JsonParser.parseString(storage_json).getAsJsonObject();
			
        ItemStack item = menu.getItemInSlot(4);
        if(item == null) {
         return true;
        } 
        JsonElement experience_element = element.get("stored-experience");
        if(experience_element == null) {
           	BlockStorage.addBlockInfo(b, "stored-experience", Double.toString(0.0));
           	return true;
        }
		Double stored_experience = experience_element.getAsDouble();
		//if(stored_experience == 0.0) {
		//	CraftCrazeSF.warn("Stored experience is null.");
		//	return false;
		//}
		Double next_experience = Utils.clamp(0, max_experience, stored_experience + to_add);
		BlockStorage.addBlockInfo(b, "stored-experience", next_experience.toString());
		//-- updates the hologram
		updateHologram(b, Utils.formatString("&f&k||&r &eExperience Stored: ".concat(Double.toString(Math.round(next_experience*1000) / 1000).concat("/").concat(Integer.toString(max_experience)).concat(" &f&k||&r"))));
		return true;
	}
	
	@Override 
	public void onNewInstance(BlockMenu menu, Block b) {

		CraftCrazeSF.warn("adding click handler to menu");
		menu.addMenuClickHandler(4, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				String storage_json = BlockStorage.getBlockInfoAsJson(b);
		        CraftCrazeSF.warn(storage_json);
		        JsonObject element = JsonParser.parseString(storage_json).getAsJsonObject();
				
				Double stored_experience = element.get("stored-experience").getAsDouble();
				if(stored_experience <= 1.0) {
					p.sendMessage(Utils.formatString("&6> There is not enough experience stored."));
					return false;
				}
				
				World w = p.getWorld();
				Location L = p.getLocation();
				w.spawnParticle(Particle.EXPLOSION_HUGE, L.getX(), L.getY() + 1, L.getZ(), 2);
				w.playSound(L, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		
				p.sendMessage(Utils.formatString("&6> You have redeemed ".concat(Double.toString(stored_experience)).concat(" experience!")));
				p.giveExp(stored_experience.intValue());
				BlockStorage.addBlockInfo(b, "stored-experience", Double.toString(0.0));
				return false;
			}
			
		});
	}


	@Override
	protected int getStatusSlot() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	protected void setup(BlockMenuPreset preset) {
		
		ItemStack experience_item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta meta = experience_item.getItemMeta();
		String uuid = UUID.randomUUID().toString();
		meta.getPersistentDataContainer().set(CraftCrazeSF.createKey("block"), PersistentDataType.STRING, uuid);
		meta.setDisplayName(Utils.formatString("&aGather Experience"));
		meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add("§a>> Left-Click to gather experience!");
		meta.setLore(lore);
		experience_item.setItemMeta(meta);
		preset.drawBackground(new int[] {0,1,2,3,5,6,7,8});
		preset.drawBackground(experience_item, new int[] {4});

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
