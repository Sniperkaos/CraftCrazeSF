package me.cworldstar.craftcrazesf.mobs.normal;

import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.sefiraat.sefilib.misc.ParticleUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.cworldstar.craftcrazesf.Registry;
import me.cworldstar.craftcrazesf.mobs.AbstractBoss;
import me.cworldstar.craftcrazesf.mobs.EntityDialog;
import me.cworldstar.craftcrazesf.mobs.EntityDialog.Personality;
import me.cworldstar.craftcrazesf.mobs.skills.Skill;
import me.cworldstar.craftcrazesf.mobs.skills.Skill.SkillType;
import me.cworldstar.craftcrazesf.utils.Utils;

public class ViruleanZombie extends AbstractBoss {

	
	public ViruleanZombie(Location l) {
		super(l, Zombie.class, "&eVirulean Zombie");

		// register the boss so it starts working
		register();
	}
	
	public ViruleanZombie(UUID mob_id) {
		super(mob_id, mob_id);
		
		re_register();
	}
	
	@Override
	protected void onRegister() {
		
		addSkill("plague_vomit", new Skill(SkillType.ACTIVE, new BukkitRunnable() {
			@Override
			public void run() {
				ViruleanZombie self = ViruleanZombie.this;
				Optional<Entity> target = self.getTarget();
				if(target.isPresent()) {
					Location loc = self.getEntity().getLocation();
					for(int i=-4; i<4; i++) {
						ParticleUtils.drawLine(Particle.REDSTONE, loc.add(new Vector(i/8,0,i/8)), target.get().getLocation(), 0.2, new DustOptions(Color.LIME, 1F));
					}
					Entity entity_target = target.get();
					if(entity_target instanceof LivingEntity) {
						
						LivingEntity living_target = (LivingEntity) entity_target;
						
						living_target.damage(22, DamageSource.builder(DamageType.MOB_ATTACK).withCausingEntity(self.getEntity()).withDamageLocation(living_target.getLocation()).withDirectEntity(self.getEntity()).build());
						
						living_target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 120, 4));
					}
				}
			}
		}));
		
		addSkill("plague_explosion", new Skill(SkillType.ACTIVE, new BukkitRunnable() {

			@Override
			public void run() {
				ViruleanZombie self = ViruleanZombie.this;
				for(Entity e : self.getEntity().getNearbyEntities(12, 12, 12)) {
					if(!(e instanceof LivingEntity)) {
						continue;
					}
					
					Location loc = self.getEntity().getLocation();
					
					ParticleUtils.drawLine(Particle.REDSTONE, loc, e.getLocation(), 0.2, new DustOptions(Color.LIME, 1F));
					((LivingEntity) e).damage(18, DamageSource.builder(DamageType.MOB_ATTACK).withCausingEntity(self.getEntity()).withDamageLocation(e.getLocation()).withDirectEntity(self.getEntity()).build());;
					
				}
			}
			
		}));
		
		addSkill("plague_aura", new Skill(SkillType.PASSIVE, new BukkitRunnable() {

			@Override
			public void run() {
				
				ViruleanZombie self = ViruleanZombie.this;
				
				// need to cancel because this skill is passive
				if(self.getEntity().isDead()) {
					this.cancel();
					return;
				}
				
				for(Entity e : self.getEntity().getNearbyEntities(8, 8, 8)) {
					if(!(e instanceof LivingEntity)) {
						continue;
					}
					
					Location loc = self.getEntity().getLocation();
					
					me.cworldstar.craftcrazesf.utils.ParticleUtils.SpawnInCircle(loc, Particle.REDSTONE, 16.0, 128, 2, new DustOptions(Color.LIME, 1F));
					LivingEntity entity = ((LivingEntity) e);
					if(!(entity.hasPotionEffect(PotionEffectType.WEAKNESS))) {
						e.sendMessage(Utils.formatString("&2&lA strange aura permeates your muscles..."));
						e.sendMessage(Utils.formatString("&aYou have been affected by weakness aura!"));
					}
					entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 5));
					
				}
				
			}
			
			
		}, 20));
	}

	@Override
	protected void applyEntityEdits(LivingEntity e) {
		
		e.setCustomName(Utils.formatString("&aVirulean Zombie"));
		e.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(300);
		e.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(10);
		AttributeInstance ms = e.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
		ms.setBaseValue(ms.getBaseValue() * 1.2);
		
		
		ItemStack ZombieHead = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQyNzQzNWYxZWNkZTY3Njg4Y2Q3YmFhZDllZjBmZDViYjM4NDk3NmU4MTQyZTY0NGY5OGNlNzRkNWQwMjZiNiJ9fX0=");
		ItemStack Boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta BootsMeta = (LeatherArmorMeta) Boots.getItemMeta();
		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		BootsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("pv_modifier", 6 , Operation.ADD_NUMBER));
		BootsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier("pv_modifier", 481, Operation.ADD_NUMBER));
		BootsMeta.setDisplayName(Utils.formatString("&eBoots of Plague"));
		BootsMeta.setColor(Color.fromRGB(181, 232, 100));
		BootsMeta.setUnbreakable(true);
		Boots.setItemMeta(BootsMeta);
		
		//TODO: Add a "Thorns" effect to EscapedTestSubject and a "Regeneration" effect. Attacks should apply "Decaying" unless player is wearing armored hazmat.
		//TODO: Increase zombie speed and strength. Make it slightly faster than the player and give it a warden-like ranged projectile attack.
		
		
		ItemStack Leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta LeggingsMeta = (LeatherArmorMeta) Leggings.getItemMeta();
		LeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		LeggingsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("pv_modifier", 6 , Operation.ADD_NUMBER));
		LeggingsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier("pv_modifier", 581, Operation.ADD_NUMBER));
		LeggingsMeta.setDisplayName(Utils.formatString("&eLeggings of the Plaguemaster"));
		LeggingsMeta.setColor(Color.fromRGB(161, 227, 57));		
		LeggingsMeta.setUnbreakable(true);
		Leggings.setItemMeta(LeggingsMeta);
		
		
		ItemStack Chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta ChestplateMeta = (LeatherArmorMeta) Chestplate.getItemMeta();
		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ChestplateMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("pv_modifier", 8 , Operation.ADD_NUMBER));
		ChestplateMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier("pv_modifier", 1581, Operation.ADD_NUMBER));
		ChestplateMeta.setDisplayName(Utils.formatString("&e&lPlaguemaster's Leather Coat"));
		ChestplateMeta.setColor(Color.fromRGB(143, 222, 20));
		ChestplateMeta.setUnbreakable(true);
		Chestplate.setItemMeta(ChestplateMeta);
		
		e.getEquipment().setArmorContents(new ItemStack[] {
				Boots,
				Leggings,
				Chestplate,
				ZombieHead
		});

	}

	@Override
	public void registerDialogs() {
		EntityDialog DialogManager = this.chat_manager;
		// Neutral personality dialog
		DialogManager.registerAllDialogs(Personality.AGGRESSIVE, new String[] {
				"&2&lROOOAR...",
				"&2&lGROAUR...",
				
		});
		DialogManager.registerSkillDialogs("plague_vomit", new String[] {
				"&2&lAUUURGH!!!",
				"&2&lAUUURGH!!!",
		});
		
		DialogManager.registerSkillDialogs("plague_explosion", new String[] {
				"&2&lGRAAAUGH!!!",
				"&2&lGRAAAUGH!!!",
		});
	}
	
	private List<DamageCause> blacklisted_damage_types = List.of(
				DamageCause.DROWNING, // no drowned cheating
				DamageCause.CRAMMING, // no cramming cheating
				DamageCause.FALL // no fall dmg
	);

	

	@Override
	protected void onEntityDamagedModifier(EntityDamageEvent e) {
		if(blacklisted_damage_types.contains(e.getCause())) {
			e.setCancelled(true);
		}
	}

	@Override
	protected void onEntityDamagingModifier(EntityDamageEvent e) {
		e.setDamage(e.getDamage() * 2);
	}

	@Override
	protected void death(EntityDeathEvent e) {
		e.setDroppedExp(1000);
		e.getDrops().add(Registry.UNCOMMON_CHEST.clone());
		if(Math.random() * 10 >= 9) {
			//-- 10% to drop this item
		}
	}

	@Override
	protected void dropItem(EntityDropItemEvent e) {
		
		
	}
}
