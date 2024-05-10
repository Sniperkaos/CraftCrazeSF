package me.cworldstar.craftcrazesf.items.armors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle.DustOptions;
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
import me.cworldstar.craftcrazesf.api.handlers.PlayerEquipArmor;
import me.cworldstar.craftcrazesf.api.handlers.PlayerUnequipArmor;
import me.cworldstar.craftcrazesf.items.armors.set.Set;
import me.cworldstar.craftcrazesf.items.armors.set.Set.Sets;
import me.cworldstar.craftcrazesf.listeners.ArmorEquipEvent;
import me.cworldstar.craftcrazesf.utils.AdvScheduler;
import me.cworldstar.craftcrazesf.utils.Utils;
import me.cworldstar.craftcrazesf.utils.AdvScheduler.RunType;

public class TurboSwaglordLegendArmor extends AbstractArmor {

	// a little funny goof :D
	
	public static void ApplyPotionEffects(LivingEntity e) {
		e.addPotionEffects(Arrays.asList(new PotionEffect[] {
				new PotionEffect(PotionEffectType.HEALTH_BOOST, 40, 40),
				new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 40),
				new PotionEffect(PotionEffectType.FAST_DIGGING, 40, 40),
				new PotionEffect(PotionEffectType.REGENERATION, 40, 40),
				new PotionEffect(PotionEffectType.SATURATION, 40, 40),
				new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 40),
				new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 40),
				new PotionEffect(PotionEffectType.WATER_BREATHING, 40, 40)
		}));
	}
	
	public TurboSwaglordLegendArmor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType,
			ItemStack[] recipe, ProtectionType[] types, PotionEffect[] effects) {
		super(itemGroup, item, recipeType, recipe, types, effects);
		
		addToSet(Sets.TURBO_SWAGLORD_LEGEND_ARMOR.getSet());
		
		addItemHandler(new PlayerUnequipArmor() {
			@Override
			public void onPlayerUnequipArmor(ArmorEquipEvent e) {
				e.getPlayer().sendMessage(Utils.formatString("&7> Armor set unequipped! <"));
				AdvScheduler.cancel(e.getPlayer().getName()+"-Armor");
			}
		});
		
		addItemHandler(new PlayerEquipArmor() {
			@Override
			public void onPlayerEquipArmor(ArmorEquipEvent e) {
				List<ItemStack> armorPieces = new ArrayList<ItemStack>();
				armorPieces.addAll(Arrays.asList(e.getAllArmorPieces()));
				armorPieces.add(e.getNewArmorPiece());
				if(Set.Sets.TURBO_SWAGLORD_LEGEND_ARMOR.getSet().ValidateSet(armorPieces)) {
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

		return null;
	}

	@Override
	public boolean isFullSetRequired() {

		return false;
	}

	@Override
	public NamespacedKey getArmorSetId() {

		return null;
	}

	@Override
	void equip(ArmorEquipEvent e) {
		e.getPlayer().sendMessage(Utils.formatString("&7> Armor set equipped! <"));
		AdvScheduler.schedule(e.getPlayer().getName()+"-Armor", new BukkitRunnable() {

			@Override
			public void run() {
				Location L = e.getPlayer().getLocation().add(0,0.5,0);
				ParticleUtils.drawCube(new DustOptions(Color.FUCHSIA, 1), L.add(-1,-1,-1),L.add(1,1,1) , 0.25D);
				TurboSwaglordLegendArmor.ApplyPotionEffects(e.getPlayer());
			}
			
		}, RunType.LOOP, 2);
	}
}
