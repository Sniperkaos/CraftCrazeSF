package me.cworldstar.craftcrazesf.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Sound;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.math.RandomUtils;
import me.cworldstar.craftcrazesf.utils.Utils;

public class EntityDialog {
	
	public enum Personality {
		AGGRESSIVE,
		SAD,
		NEUTRAL,
		HAPPY,
		RANDOM
	}
	
	private Random randomizer = new Random();
	private Map<Personality, ArrayList<String>> Dialog = new HashMap<Personality, ArrayList<String>>();
	private Map<String, ArrayList<String>> SkillDialogs = new HashMap<String, ArrayList<String>>();
	private String EntityName;
	private Personality EntityPersonality;
	private Sound entity_sound = Sound.AMBIENT_BASALT_DELTAS_ADDITIONS;
	
	public String randomDialog() {
		return EntityName + ": &f" + this.Dialog.get(this.EntityPersonality).get(RandomUtils.nextInt(this.Dialog.get(this.EntityPersonality).size()));
	}
	
	public String randomSkillDialog(String skillId) {
		return EntityName + ": &f" + this.SkillDialogs.get(skillId).get(RandomUtils.nextInt(this.SkillDialogs.get(skillId).size()));
	}
	
	@SuppressWarnings("static-access")
	public EntityDialog(String FormattedEntityName, Personality Personality) {
		
		if(Personality.equals(Personality.RANDOM)) {
			switch(randomizer.nextInt(3)) {
				case 0:
					this.EntityPersonality = Personality.AGGRESSIVE;
					break;
				case 1:
					this.EntityPersonality = Personality.SAD;
					break;
				case 2: 
					this.EntityPersonality = Personality.NEUTRAL;
					break;
				case 3:
					this.EntityPersonality = Personality.HAPPY;
					break;
				default:
					this.EntityPersonality = Personality.AGGRESSIVE;
					break;
					
			}
		} else {
			this.EntityPersonality = Personality;
		}
		this.EntityName = FormattedEntityName;
		this.Dialog.putIfAbsent(Personality.AGGRESSIVE, new ArrayList<String>());
		this.Dialog.putIfAbsent(Personality.SAD, new ArrayList<String>());
		this.Dialog.putIfAbsent(Personality.NEUTRAL, new ArrayList<String>());
		this.Dialog.putIfAbsent(Personality.HAPPY, new ArrayList<String>());
		
		
		//THIS SHOULD NEVER BE ACCESSED! HERE FOR PLAUSIBLE DENIABILITY!
		this.Dialog.putIfAbsent(Personality.RANDOM, new ArrayList<String>());
	}
	
	public void registerDialog(Personality P, String formattedDialog) {
		this.Dialog.get(P).add(formattedDialog);
	}
	
	public Personality getPersonality() {
		return this.EntityPersonality;
	}

	public boolean skillHasDialog(String skillId) {
		
		return (this.SkillDialogs.get(skillId).size() > 0);
	}
	
	public void registerAllDialogs(Personality P, ArrayList<String> dialogs) {

		for(String dialog: dialogs) {
			this.Dialog.get(P).add(dialog);
		}
	}

	public void registerAllDialogs(Personality personality, String[] dialogList) {

		for(String dialog: dialogList) {
			this.Dialog.get(personality).add(dialog);
		}
	}

	public void registerSkillDialogs(String skillId, String[] strings) {
		
		this.SkillDialogs.putIfAbsent(skillId, new ArrayList<String>());
		
		ArrayList<String> new_strings = new ArrayList<String>();
		
		for( String dialog : strings) {
			new_strings.add(Utils.formatString(dialog));
		}
		
		this.SkillDialogs.put(skillId, new_strings);
	}

	public void registerSkillDialogs(String skillId, ArrayList<String> strings) {
		this.SkillDialogs.putIfAbsent(skillId, new ArrayList<String>());
		
		ArrayList<String> new_strings = new ArrayList<String>();
		
		for( String dialog : strings) {
			new_strings.add(Utils.formatString(dialog));
		}
		
		this.SkillDialogs.put(skillId, new_strings);

	}
	
	public void setDialogSound(Sound sound) {
		this.entity_sound = sound;
	}

	public Sound getEntitySound() {
		return entity_sound;
	}


}
