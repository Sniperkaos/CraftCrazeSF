package me.cworldstar.craftcrazesf.mobs.normal;

import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Optional;

import dev.sefiraat.sefilib.misc.ParticleUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
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
						
						living_target.damage(14, DamageSource.builder(DamageType.MOB_ATTACK).withCausingEntity(self.getEntity()).withDamageLocation(living_target.getLocation()).withDirectEntity(self.getEntity()).build());
						
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
					((LivingEntity) e).damage(10, DamageSource.builder(DamageType.MOB_ATTACK).withCausingEntity(self.getEntity()).withDamageLocation(e.getLocation()).withDirectEntity(self.getEntity()).build());;
					
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
				}
				
				for(Entity e : self.getEntity().getNearbyEntities(8, 8, 8)) {
					if(!(e instanceof LivingEntity)) {
						continue;
					}
					
					Location loc = self.getEntity().getLocation();
					
					ParticleUtils.drawCube(Particle.REDSTONE, loc.add(8,8,8), loc.add(-8,-8,-8), 0.2, new DustOptions(Color.LIME, 1F));
					LivingEntity entity = ((LivingEntity) e);
					if(!entity.hasPotionEffect(PotionEffectType.WEAKNESS)) {
						e.sendMessage(Utils.formatString("&2&lA strange aura permeates your muscles..."));
						e.sendMessage(Utils.formatString("&aYou have been affected by weakness aura!"));
						entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 3));
					}
					
				}
				
			}
			
			
		}, 20));
	}

	@Override
	protected void applyEntityEdits(LivingEntity e) {
		ItemStack ZombieHead = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQyNzQzNWYxZWNkZTY3Njg4Y2Q3YmFhZDllZjBmZDViYjM4NDk3NmU4MTQyZTY0NGY5OGNlNzRkNWQwMjZiNiJ9fX0=");
		ItemStack Boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta BootsMeta = (LeatherArmorMeta) Boots.getItemMeta();
		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		BootsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("pv_modifier", 4 , Operation.ADD_NUMBER));
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
		LeggingsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("pv_modifier", 4 , Operation.ADD_NUMBER));
		LeggingsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier("pv_modifier", 581, Operation.ADD_NUMBER));
		LeggingsMeta.setDisplayName(Utils.formatString("&eLeggings of the Plaguemaster"));
		LeggingsMeta.setColor(Color.fromRGB(161, 227, 57));		
		LeggingsMeta.setUnbreakable(true);
		Leggings.setItemMeta(LeggingsMeta);
		
		
		ItemStack Chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta ChestplateMeta = (LeatherArmorMeta) Chestplate.getItemMeta();
		ChestplateMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("pv_modifier", 6 , Operation.ADD_NUMBER));
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
		});
	}

	@Override
	protected void onEntityDamagedModifier(EntityDamageByEntityEvent e) {
		// cramming sux bro
		if(e.getDamageSource().getDamageType() == DamageType.CRAMMING) {
			e.setCancelled(true);
		}
	}

	@Override
	protected void onEntityDamagingModifier(EntityDamageByEntityEvent e) {
		e.setDamage(e.getDamage() * 2);
	}

	@Override
	protected void death() {
		// TODO Auto-generated method stub
		
	}

}
