package me.cworldstar.craftcrazesf.items.armors;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import dev.sefiraat.sefilib.misc.ParticleUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.api.handlers.PlayerEquipArmor;
import me.cworldstar.craftcrazesf.items.armors.set.Set;
import me.cworldstar.craftcrazesf.listeners.ArmorEquipEvent;
import me.cworldstar.craftcrazesf.utils.AdvScheduler;
import me.cworldstar.craftcrazesf.utils.Utils;
import me.cworldstar.craftcrazesf.utils.AdvScheduler.RunType;

/**
 * 
 * Implemented version of {@link AbstractArmor}.
 * Example version of an AdvancedHazmat suit effect.
 * 
 * This {@link AbstractArmor} will create itself in the
 * ADVANCED_HAZMAT {@link Set}.
 * 
 * @author cworldstar
 *
 */

public class AdvancedHazmat extends AbstractArmor {

	public static void ApplyPotionEffects(LivingEntity e) {
		e.addPotionEffects(Arrays.asList(new PotionEffect[] {
				new PotionEffect(PotionEffectType.HEALTH_BOOST, 40, 2),
				new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 1),
				new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 0),
				new PotionEffect(PotionEffectType.WATER_BREATHING, 40, 0)
		}));
	}
	
	public AdvancedHazmat(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
			@Nullable ProtectionType[] types, @Nullable PotionEffect[] effects) {
		super(itemGroup, item, recipeType, recipe, types, effects);
		
		addToSet(Set.Sets.ADVANCED_HAZMAT.getSet());
		
		addItemHandler(new PlayerEquipArmor() {
			@Override
			public void onPlayerEquipArmor(ArmorEquipEvent e) {
				List<ItemStack> armorPieces = Arrays.asList(e.getAllArmorPieces());
				armorPieces.add(e.getNewArmorPiece());
				if(Set.Sets.ADVANCED_HAZMAT.getSet().ValidateSet(armorPieces)) {
					equip(e);
				} else {
					if(AdvScheduler.IsScheduled(e.getPlayer().getName() + "-Armor")) {
						AdvScheduler.cancel(e.getPlayer().getName()+"-Armor");
					}
				}
			}
		});
		
	}

	@Override
	public ProtectionType[] getProtectionTypes() {
		// TODO Auto-generated method stub
		return this.types;
	}

	@Override
	public boolean isFullSetRequired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public NamespacedKey getArmorSetId() {
		// TODO Auto-generated method stub
		return CraftCrazeSF.createKey("AdvancedHazmat");
	}

	@Override
	void equip(ArmorEquipEvent e) {
		e.getPlayer().sendMessage(Utils.formatString("&7> Armor set equipped! <"));
		AdvScheduler.schedule(e.getPlayer()+"-Armor", new BukkitRunnable() {

			@Override
			public void run() {
				ParticleUtils.drawCube(Particle.REDSTONE, e.getPlayer().getLocation().add(-1,-1,-1),e.getPlayer().getLocation().add(1,1,1) , 0.25D);
				AdvancedHazmat.ApplyPotionEffects(e.getPlayer());
			}
			
		}, RunType.LOOP, 20);
	}

}
