package me.cworldstar.craftcrazesf.mobs.normal;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;



import me.cworldstar.craftcrazesf.mobs.AbstractBoss;

public class SpiderMother extends AbstractBoss {

	public SpiderMother(Location loc) {
		super(loc, Spider.class, "&5Spider Mother");
	}

	@Override
	protected void dropItem(EntityDropItemEvent e) {

		
	}

	@Override
	protected void death(EntityDeathEvent e) {

	}

	@Override
	protected void onEntityDamagedModifier(EntityDamageEvent e) {

		
	}

	@Override
	protected void onEntityDamagingModifier(EntityDamageEvent e) {

		
	}

	@Override
	protected void registerDialogs() {

		
	}

	@Override
	protected void onRegister() {

		
	}

	@Override
	protected void applyEntityEdits(LivingEntity e) {

		
	}

}
