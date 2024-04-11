package me.cworldstar.craftcrazesf.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.mobs.EntityDialog.*;
import me.cworldstar.craftcrazesf.mobs.skills.Skill;
import me.cworldstar.craftcrazesf.mobs.skills.Skill.SkillType;
import me.cworldstar.craftcrazesf.utils.Speak;

public abstract class AbstractBoss implements Listener {
	
	protected LivingEntity BossEntity;
	protected UUID boss_identifier;
	private Map<String, Skill> skills = new HashMap<String, Skill>();
	protected ArrayList<Player> nearby_players = new ArrayList<Player>();
	protected Personality personality;
	protected Entity target;
	protected EntityDialog chat_manager;
	protected boolean triggered = false;
	
	// spawn a new entity
	public <T extends LivingEntity> AbstractBoss(Location loc, Class<T> toSpawn, String name) {
		CraftCrazeSF plugin = CraftCrazeSF.getMainPlugin();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		boss_identifier = UUID.randomUUID();
		chat_manager = new EntityDialog(name, Personality.RANDOM);
		this.BossEntity = loc.getWorld().spawn(loc, toSpawn, SpawnReason.CUSTOM, false, null);
	}
	
	// load an existing entity
	public AbstractBoss(UUID boss_id, UUID mobUUID) {

		Entity entity = Bukkit.getEntity(mobUUID);
		if(entity instanceof LivingEntity) {
			boss_identifier = boss_id;
			chat_manager = new EntityDialog(entity.getCustomName(), Personality.RANDOM);
			this.BossEntity = (LivingEntity) entity;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity().getUniqueId().equals(BossEntity.getUniqueId())) {
			this.onEntityDamagedModifier(e);
		} else if (e.getDamager().getUniqueId().equals(BossEntity.getUniqueId())) {
			this.onEntityDamagingModifier(e);
		}
	}
	
	protected void onDeath() {
		death();
		HandlerList.unregisterAll(this);
	}
	
	protected abstract void death();
	
	protected abstract void onEntityDamagedModifier(EntityDamageByEntityEvent e);
	
	protected abstract void onEntityDamagingModifier(EntityDamageByEntityEvent e);
	
	public LivingEntity getEntity() {
		return this.BossEntity;
	}
	
	public Optional<Entity> getTarget() {
		return Optional.ofNullable(this.target);
	}
	
	protected abstract void registerDialogs();
	
	protected abstract void onRegister();
	
	public void setPersonality(Personality P) {
		this.personality = P;
	}
	
	public void registerDialog(Personality P, String[] dialogs) {
		this.chat_manager.registerAllDialogs(P, dialogs);
	}
	
	protected abstract void applyEntityEdits(LivingEntity e);
	
	public void re_register() {
		this.onRegister();
		StaticMobController.mobs.put(boss_identifier.toString(), BossEntity);
	}
	
	public void useRandomSkill() {
		ArrayList<Skill> active_skills = new ArrayList<Skill>();
		for(Entry<String, Skill> skill : this.skills.entrySet()) {
			if(skill.getValue().getType() == SkillType.ACTIVE) {
				active_skills.add(skill.getValue());
			}
		}
		
		active_skills.get(new Random().nextInt(active_skills.size())).use();
	}
	
	public void register() {
		
		this.applyEntityEdits(BossEntity);
		
		this.onRegister();
		
		StaticMobController.mobs.put(boss_identifier.toString(), BossEntity);
		
		for(Entry<String, Skill> skill : this.skills.entrySet()) {
			if(skill.getValue().getType() == SkillType.PASSIVE) {
				skill.getValue().use();
			}
		}
		
		/*
		 * 
		 * 		new BukkitRunnable() {
			@Override
			public void run() {
				if(entity.isDead()) {
					this.cancel();
				} else if(entity.getTarget() == null) {
					Speak.AOEMessage(entity, RandomUtils.selectRandom(CorporateLeader.getIdleDialog()) , 32);
				} else if(entity.getTarget() != null && entity.getTarget() instanceof Player) {
					new Speak(entity,entity.getNearbyEntities(20, 20, 20),RandomUtils.selectRandom(CorporateLeader.getAttackingDialog((Player) entity.getTarget())));
					Entity.useRandomSkill(DialogManager);

				}
			}
		}.runTaskTimer(plugin, 0, 200L);
		 * 
		 * 
		 */
		
		new BukkitRunnable() {

			@Override
			public void run() {
				AbstractBoss self = AbstractBoss.this;
				
				if(self.getEntity().isDead()) {
					onDeath();
					this.cancel();
				}
				
				for(Entity e : self.BossEntity.getNearbyEntities(12, 12, 12)) {
					if(e instanceof Player) {
						if(!self.triggered) {
							Speak.AOEMessage(self.BossEntity, "test", 20, Sound.BLOCK_NOTE_BLOCK_IMITATE_WITHER_SKELETON);
							self.triggered = true;
						}
						nearby_players.add((Player) e);
						self.target = nearby_players.get(0);
					}
				}
				
				
			}
			
		}.runTaskTimer(CraftCrazeSF.getMainPlugin(), 0, 20);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				AbstractBoss self = AbstractBoss.this;
				
				if(self.getEntity().isDead()) {
					this.cancel();
				}
			
				self.useRandomSkill();
				
				
			}
			
		}.runTaskTimer(CraftCrazeSF.getMainPlugin(), 0, 200);
		
		return;
	}

	public Map<String, Skill> getSkills() {
		return skills;
	}

	public void addSkill(String skill_id, Skill skill) {
		this.skills.put(skill_id, skill);
	}
}
