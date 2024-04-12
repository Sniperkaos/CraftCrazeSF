package me.cworldstar.craftcrazesf.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.math.RandomUtils;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.mobs.EntityDialog.*;
import me.cworldstar.craftcrazesf.mobs.skills.Skill;
import me.cworldstar.craftcrazesf.mobs.skills.Skill.SkillType;
import me.cworldstar.craftcrazesf.utils.Speak;
import me.cworldstar.craftcrazesf.utils.Utils;

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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity().getUniqueId().equals(BossEntity.getUniqueId())) {
			this.onEntityDamagedModifier(e);
		} 
		
		if(e.getDamageSource().getCausingEntity() == null) {
			return;
		}
		
		if (e.getDamageSource().getCausingEntity().getUniqueId().equals(BossEntity.getUniqueId())) {
			this.onEntityDamagingModifier(e);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDeath(EntityDeathEvent e) {
		if(e.getEntity().getUniqueId().equals(BossEntity.getUniqueId())) {
			onDeath(e);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDropItem(EntityDropItemEvent e) {
		if(e.getEntity().getUniqueId().equals(BossEntity.getUniqueId())) {
			dropItem(e);
		}
	}
	
	protected void onDeath(EntityDeathEvent e) {
		death(e);
		HandlerList.unregisterAll(this);
	}
	
	
	protected abstract void dropItem(EntityDropItemEvent e);
	
	protected abstract void death(EntityDeathEvent e);
	
	protected abstract void onEntityDamagedModifier(EntityDamageEvent e);
	
	protected abstract void onEntityDamagingModifier(EntityDamageEvent e);
	
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
		this.register();
	}
	
	public void useRandomSkill() {
		ArrayList<Skill> active_skills = new ArrayList<Skill>();
		for(Entry<String, Skill> skill : this.skills.entrySet()) {
			if(skill.getValue().getType() == SkillType.ACTIVE) {
				active_skills.add(skill.getValue());
			}
		}
		
		Skill chosen_skill = active_skills.get(new Random().nextInt(active_skills.size()));
		
		if(this.chat_manager.skillHasDialog(chosen_skill.id)) {
			Speak.AOEMessage(BossEntity, Utils.formatString( this.chat_manager.randomSkillDialog(chosen_skill.id) ), 12, Sound.BLOCK_NOTE_BLOCK_IMITATE_WITHER_SKELETON);
		}		
		chosen_skill.use();
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
					this.cancel();
					return;
				}
				
				List<Entity> nearby_entities = self.BossEntity.getNearbyEntities(12, 12, 12);	
				
				for(Entity e : nearby_entities) {
					if(e instanceof Player) {
						if(!self.triggered) {
							Speak.AOEMessage(self.BossEntity, Utils.formatString("&4&k[&r &4You have been noticed. &k]&r"), 20, Sound.ENTITY_LIGHTNING_BOLT_IMPACT);
							self.triggered = true;
							if(!nearby_players.contains((Player) e)) {
								nearby_players.add((Player) e);
							}
							self.target = nearby_players.get(RandomUtils.nextInt(nearby_players.size()));
						}
					}
				}
				
				
				for(Player p : nearby_players) {
					if(!nearby_entities.contains(p)) {
						if(self.target.equals(p)) {
							self.target = nearby_players.get(RandomUtils.nextInt(nearby_players.size())) ;
						}
						nearby_players.remove(p);
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
					return;
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
		skill.setId(skill_id);
		skill.lock();
		this.skills.put(skill_id, skill);
	}
}
