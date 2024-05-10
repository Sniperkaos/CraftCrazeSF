package me.cworldstar.craftcrazesf.mobs.normal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import dev.sefiraat.sefilib.misc.ParticleUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.math.RandomUtils;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.Registry;
import me.cworldstar.craftcrazesf.mobs.AbstractBoss;
import me.cworldstar.craftcrazesf.mobs.skills.Skill;
import me.cworldstar.craftcrazesf.mobs.skills.Skill.SkillType;
import me.cworldstar.craftcrazesf.utils.Utils;

public class WanderingSwordsman extends AbstractBoss {

	public WanderingSwordsman(Location loc) {
		super(loc, Skeleton.class, "&7&lWandering Swordsman");
	
		
		this.chat_manager.setDialogSound(Sound.ENTITY_SKELETON_CONVERTED_TO_STRAY);
		
		// register the boss so it starts working
		register();
	}
	
	public WanderingSwordsman(UUID mob_id) {
		super(mob_id, mob_id);
		
		re_register();
	}

	@Override
	protected void dropItem(EntityDropItemEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void death(EntityDeathEvent e) {
		e.setDroppedExp(5000);
		e.getDrops().add(Registry.UNCOMMON_CHEST.clone());
		e.getDrops().add(Registry.UNCOMMON_CHEST.clone());
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
	
	private boolean ItemIsSword(ItemStack i) {
		switch(i.getType()) {
			case WOODEN_SWORD:
				return true;
			case STONE_SWORD:
				return true;
			case IRON_SWORD:
				return true;
			case GOLDEN_SWORD:
				return true;
			case DIAMOND_SWORD:
				return true;
			case NETHERITE_SWORD:
				return true;
			default:
				return false;
		}
	}
	

	@Override
	protected void onEntityDamagingModifier(EntityDamageEvent e) {
		if(e.getEntity() instanceof LivingEntity) {
			
			// he will do extra durability damage
			
			LivingEntity entity = (LivingEntity) e.getEntity();
			for(ItemStack item : entity.getEquipment().getArmorContents()) {
				if(item == null) {
					continue;
				}
				
				ItemMeta meta = item.getItemMeta();
				if(meta instanceof Damageable) {
					Damageable damageable_meta = (Damageable) meta;
					damageable_meta.setDamage(damageable_meta.getDamage() + 1);
					item.setItemMeta(damageable_meta);
				}
				
			}
			ItemStack item = entity.getEquipment().getItemInOffHand();
			if(item != null && ItemIsSword(item) ) {
				e.setDamage(e.getDamage() * 2); // doubles the damage
			}
		}
		
	}
	


	@Override
	protected void registerDialogs() {
		chat_manager.registerSkillDialogs("teleport", Utils.CreateLore(new String[] {
				
				"&7I'm already behind you...",
				"&7How foolish.",
				"&7Fear my sword!"
				
		}));
		
		chat_manager.registerSkillDialogs("vortex", Utils.CreateLore(new String[] {
				
				"&7You cannot escape!",
				"&7Face my steel!",
				"&7Nobody will leave here alive!"
				
		}));
		
		chat_manager.registerSkillDialogs("jump", Utils.CreateLore(new String[] {
				
				"&7Where will I land?!",
				
		}));
		
	}

	@Override
	protected void onRegister() {
		addSkill("teleport", new Skill(SkillType.ACTIVE, new BukkitRunnable() {

			@Override
			public void run() {
				WanderingSwordsman self = WanderingSwordsman.this;
				Optional<Entity> target = self.getTarget();
				if(target.isPresent()) {
					ParticleUtils.drawLine(Particle.REDSTONE, self.getEntity().getLocation(), target.get().getLocation(), 0.2, new DustOptions(Color.ORANGE, 2F));
					self.getEntity().teleport(target.get());
					for(int i=0; i<RandomUtils.nextInt(7)+1; i++) {
						ParticleUtils.displayParticleRandomly(target.get(), 1, new DustOptions(Color.RED, 3));
						self.getEntity().attack(target.get());
					}
					if(target.get() instanceof LivingEntity) {
						((LivingEntity) target.get()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 4));
						((LivingEntity) target.get()).damage(22);
					}
				}
			}
		}));
		
		addSkill("vortex", new Skill(SkillType.ACTIVE, new BukkitRunnable() {

			@Override
			public void run() {
				WanderingSwordsman self = WanderingSwordsman.this;
				
				me.cworldstar.craftcrazesf.utils.ParticleUtils.SpawnInCircle(self.getEntity().getLocation(), Particle.REDSTONE,6,72, 2, new DustOptions(Color.ORANGE, 2F));
				me.cworldstar.craftcrazesf.utils.ParticleUtils.SpawnInCircle(self.getEntity().getLocation(), Particle.REDSTONE,5,60, 2, new DustOptions(Color.RED, 2F));
				me.cworldstar.craftcrazesf.utils.ParticleUtils.SpawnInCircle(self.getEntity().getLocation(), Particle.REDSTONE,4,48, 2, new DustOptions(Color.YELLOW, 2F));
				me.cworldstar.craftcrazesf.utils.ParticleUtils.SpawnInCircle(self.getEntity().getLocation(), Particle.REDSTONE,3,36, 2, new DustOptions(Color.GRAY, 2F));
				me.cworldstar.craftcrazesf.utils.ParticleUtils.SpawnInCircle(self.getEntity().getLocation(), Particle.REDSTONE,2,24, 2, new DustOptions(Color.BLACK, 2F));
				
				for(Entity e : self.BossEntity.getNearbyEntities(12,12,12)) {
					if(e instanceof LivingEntity) {
						LivingEntity living_entity = ((LivingEntity) e);
						Location loc1 = self.BossEntity.getLocation();
						Location loc2 = living_entity.getLocation();
						Vector normalized = loc1.toVector().subtract(loc2.toVector()).normalize().multiply(loc1.distance(loc2));
						Location loc3 = loc2.add(normalized);
						living_entity.setVelocity(loc3.getDirection().normalize().multiply(loc1.distance(loc2)));
						self.getEntity().attack(living_entity);
					}
				}
			}
			
		}));
		
		addSkill("jump", new Skill(SkillType.ACTIVE, new BukkitRunnable() {

			@Override
			public void run() {
				WanderingSwordsman self = WanderingSwordsman.this;
				ParticleUtils.displayParticleRandomly(BossEntity, Particle.SMOKE_LARGE, 1);
				self.getEntity().setVelocity(new Vector(0,10,0).normalize().multiply(6));
				Optional<Entity> target = self.getTarget();
				if(target.isPresent()) {
					new BukkitRunnable() {
						@Override
						public void run() { 
							self.getEntity().attack(target.get());
							self.getEntity().teleport(target.get());
						}
					}.runTaskLater(CraftCrazeSF.getMainPlugin(), 20);
				}
			}
			
		}));
		
	}

	@Override
	protected void applyEntityEdits(LivingEntity e) {

		e.setCustomName(Utils.formatString("&7&lWandering Swordsman"));
		e.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(305);
		e.setHealth(305);
		e.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(22);
		AttributeInstance ms = e.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
		ms.setBaseValue(ms.getBaseValue() * 1.6);
		
		e.getEquipment().setItemInMainHand(SlimefunItems.SWORD_OF_BEHEADING);
	}

}
