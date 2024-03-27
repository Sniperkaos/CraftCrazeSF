package me.cworldstar.craftcrazesf.machines;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.ConfigurationException;

import io.github.mooy1.infinitylib.machines.AbstractMachineBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.utils.Utils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

@SuppressWarnings("deprecation")
public class ExperienceGenerator extends AbstractMachineBlock implements HologramOwner {
	
	private double stored_experience = 0;
	private double to_add = 0.02;
	private int max_experience = 1000;
	
	
	public ExperienceGenerator(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		
		this.energyPerTick(100);
		this.energyCapacity(2000);
		
		ExperienceGenerator self = this;
		
		addItemHandler(new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				try {
					double ept = CraftCrazeSF.config().getDouble("experience-generator.experience-per-tick");
					int meh = CraftCrazeSF.config().getInt("experience-generator.max-experience-held");
					
					//-- not null
					Validate.notNull(ept);
					Validate.notNull(meh);
					
					if(ept == 0.0 || meh == 0) {
						throw new InvalidConfigurationException();
					}
					
					self.to_add = ept;
					self.max_experience = meh;
				} catch(InvalidConfigurationException exception) {
					CraftCrazeSF.logger.warning("A configuration exception has occured. Please check your configuration!");
				} 
				finally {}
			}

		});
		
		addItemHandler(new BlockBreakHandler(false, false) {
			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				// TODO Auto-generated method stub
				self.removeHologram(e.getBlock());
			}
			
		});
		

		
	}
	
	@Override
	public boolean process(Block b, BlockMenu menu) {
		if(getCharge(b.getLocation()) > 100) {
			stored_experience = Utils.clamp(0, max_experience, stored_experience + to_add);
			//-- updates the hologram
			updateHologram(b, Utils.formatString("&eExperience Stored: ".concat(Double.toString(Math.round(this.stored_experience*100) / 100).concat("/").concat(Integer.toString(max_experience)))));
			return true;
		}
		return false;
	}


	@Override
	protected int getStatusSlot() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	protected void setup(BlockMenuPreset preset) {
		// TODO Auto-generated method stub
		ExperienceGenerator self = this;
		
		ItemStack experience_item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta meta = experience_item.getItemMeta();
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
		preset.addMenuClickHandler(4, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				// TODO Auto-generated method stub
				
				if(self.stored_experience <= 1.0) {
					p.sendMessage(Utils.formatString("&6> There is not enough experience stored."));
					return false;
				}
				
				World w = p.getWorld();
				Location L = p.getLocation();
				w.spawnParticle(Particle.EXPLOSION_HUGE, L.getX(), L.getY() + 1, L.getZ(), 2);
				w.playSound(L, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		
				p.sendMessage(Utils.formatString("&6> You have redeemed ".concat(Double.toString(self.stored_experience)).concat(" experience!")));
				p.giveExp((int) self.stored_experience);
				self.stored_experience = 0;
				
				return false;
				
			}
			
		});
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
