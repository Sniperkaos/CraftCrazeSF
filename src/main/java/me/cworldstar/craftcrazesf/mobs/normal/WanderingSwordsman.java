package me.cworldstar.craftcrazesf.mobs.normal;

import java.util.Optional;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import dev.sefiraat.sefilib.misc.ParticleUtils;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.math.RandomUtils;
import me.cworldstar.craftcrazesf.mobs.AbstractBoss;
import me.cworldstar.craftcrazesf.mobs.skills.Skill;
import me.cworldstar.craftcrazesf.mobs.skills.Skill.SkillType;

public class WanderingSwordsman extends AbstractBoss {

	public WanderingSwordsman(Location loc) {
		super(loc, Skeleton.class, "&7&lWandering Swordsman");
	
	}

	@Override
	protected void dropItem(EntityDropItemEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void death(EntityDeathEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onEntityDamagedModifier(EntityDamageEvent e) {
		// TODO Auto-generated method stub
		
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
			LivingEntity entity = (LivingEntity) e.getEntity();
			ItemStack item = entity.getEquipment().getItemInOffHand();
			if(item != null && ItemIsSword(item) ) {
				e.setDamage(e.getDamage() * 2);
			}
		}
		
	}

	@Override
	protected void registerDialogs() {
		// TODO Auto-generated method stub
		
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
					for(int i=0; i<RandomUtils.nextInt(4)+1; i++) {
						self.getEntity().attack(target.get());
					}
				}
				
			}
			
		}));
		
	}

	@Override
	protected void applyEntityEdits(LivingEntity e) {
		// TODO Auto-generated method stub
		
	}

}
