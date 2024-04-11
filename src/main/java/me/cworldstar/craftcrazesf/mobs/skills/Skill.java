package me.cworldstar.craftcrazesf.mobs.skills;

import org.bukkit.scheduler.BukkitRunnable;

import me.cworldstar.craftcrazesf.CraftCrazeSF;

public class Skill {

	public enum SkillType {
		PASSIVE(1),ACTIVE(2),SEQUENCED(3);
		SkillType(int i) {
			// TODO Auto-generated constructor stub
		}
	}

	
	private int timer = 20;
	private BukkitRunnable Skill;
	private SkillType SkillType;
	public String id;
	
	public Skill(SkillType Type,BukkitRunnable Skill) {
		this.Skill = Skill;
		this.SkillType = Type;
	}
	
	public Skill(String id, SkillType Type, BukkitRunnable Skill) {
		this.id = id;
		this.Skill = Skill;
		this.SkillType = Type;
	}
	
	
	public Skill(SkillType passive, BukkitRunnable bukkitRunnable, int i) {
		this.Skill = bukkitRunnable;
		this.SkillType = passive;
		this.timer = i;
	}

	public SkillType getType() {
		return this.SkillType;
	}
	
	public void use() {
		
		switch(this.SkillType) {
			case PASSIVE:
				this.Skill.runTaskTimer(CraftCrazeSF.getPlugin(CraftCrazeSF.class), 0, this.timer);
				break;
			case ACTIVE:
				this.Skill.run();
				break;
			case SEQUENCED:
				/**
				 * 
				 * Sequenced is passive but it runs every five seconds and needs to be
				 * manually canceled.
				 * 
				 * @author cworldstar
				 */
				//this.Skill.runTaskTimer(SFDrugs.getPlugin(SFDrugs.class), 0, 5L);
			default:
				throw new Error("invalid skill type");
		}
	}
}
