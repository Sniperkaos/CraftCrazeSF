package me.cworldstar.craftcrazesf;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.mooy1.infinitylib.groups.MultiGroup;
import io.github.mooy1.infinitylib.groups.SubGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.cworldstar.craftcrazesf.items.pets.IronGolemPet;
import me.cworldstar.craftcrazesf.items.tokens.BasicMachineToken;
import me.cworldstar.craftcrazesf.machines.ExperienceGenerator;
import me.cworldstar.craftcrazesf.utils.Utils;

public class Registry {
	
	private MultiGroup main_group;
	private SubGroup token_group;
	private SubGroup machine_group;
	private SubGroup magic_group;
	private CraftCrazeSF addon;
	private Config config;
	
	public static final CustomItemStack ITEM_GROUP_MAIN = new CustomItemStack(Material.PURPLE_CANDLE, "&dCrazeCraft SF", "", "&d> Click to open!");
	public static final CustomItemStack ITEM_GROUP_TOKENS = new CustomItemStack(Material.DIAMOND,"&fResearch Tokens","","&d> Click to open!");
	public static final CustomItemStack ITEM_GROUP_MACHINES = new CustomItemStack(Material.DROPPER,"&6Machines","","&d> Click to open!");
	public static final CustomItemStack ITEM_GROUP_MAGIC = new CustomItemStack(Material.DROPPER,"&9Magic Items","","&d> Click to open!");
	public static final ItemStack IRON_GOLEM_PET_ITEM = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkwNzkzZjU2NjE2ZjEwMTUwMmRlMWQzNGViMjU0NGY2MDdkOTg5MDBlMzY5OTM2OTI5NTMxOWU2MzBkY2Y2ZCJ9fX0=");
	public static final CustomItemStack EXPERIENCE_GENERATOR_ITEM = new CustomItemStack(Material.CYAN_CONCRETE);
	
	public static SlimefunItemStack TOKEN_BASIC_MACHINES; 
	public static SlimefunItemStack TOKEN_ADVANCED_MACHINES; 
	public static SlimefunItemStack TOKEN_ELITE_MACHINES; 
	public static SlimefunItemStack TOKEN_FUTURE_MACHINES;
	public static SlimefunItemStack IRON_GOLEM_PET;
	public static SlimefunItemStack EXPERIENCE_GENERATOR;
	
	
	
	public Registry(CraftCrazeSF plugin, Config cfg) {
		
		Registry.TOKEN_BASIC_MACHINES = new SlimefunItemStack("BASIC_MACHINE_TOKEN", Material.EMERALD, cfg.getString("tokens.basic.name"), "", cfg.getString("tokens.basic.desc"));
		Registry.TOKEN_ADVANCED_MACHINES = new SlimefunItemStack("ADVANCED_MACHINE_TOKEN", Material.DIAMOND, cfg.getString("tokens.advanced.name"), "", cfg.getString("tokens.advanced.desc"));
		Registry.TOKEN_ELITE_MACHINES = new SlimefunItemStack("ELITE_MACHINE_TOKEN", Material.DIAMOND, cfg.getString("tokens.elite.name"), "", cfg.getString("tokens.elite.desc"));
		Registry.TOKEN_FUTURE_MACHINES = new SlimefunItemStack("FUTURE_MACHINE_TOKEN", Material.DIAMOND, cfg.getString("tokens.legendary.name"), "", cfg.getString("tokens.legendary.desc"));
		Registry.IRON_GOLEM_PET = new SlimefunItemStack("IRON_GOLEM_PET", IRON_GOLEM_PET_ITEM, "&fIron Golem Pet", "", "&f>> &4&nRight-Click&r &fto enable!", "","&fCurrently: {status}.", "&fFavorite Food: {food}");
		Registry.EXPERIENCE_GENERATOR = new SlimefunItemStack("EXPERIENCE_GENERATOR", EXPERIENCE_GENERATOR_ITEM, "&eExperience Generator", "", "&f>> Passively creates experience.", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),LoreBuilder.powerBuffer(2000) ,LoreBuilder.powerPerSecond(200));
		
		SubGroup token_group = new SubGroup("token_group", Registry.ITEM_GROUP_TOKENS);
		SubGroup machine_group = new SubGroup("machine_group", Registry.ITEM_GROUP_MACHINES);
		SubGroup magic_group = new SubGroup("magic_group", Registry.ITEM_GROUP_MAGIC);
		
		MultiGroup craze_craft_sf = new MultiGroup("main_group",Registry.ITEM_GROUP_MAIN, token_group, machine_group, magic_group );
		craze_craft_sf.register(plugin);
		
    	this.main_group = craze_craft_sf;
    	this.token_group = token_group;
    	this.machine_group = machine_group;
    	this.magic_group = magic_group;
    	this.addon = plugin;
    	this.config = cfg;
	}
	
	public static String nameFromItem(SlimefunItem item) {
		
		switch(item.getId()) {
		
			case "BASIC_MACHINE_TOKEN":
				return "basic";
			case "ADVANCED_MACHINE_TOKEN":
				return "advanced";
			case "ELITE_MACHINE_TOKEN":
				return "elite";
			case "FUTURE_MACHINE_TOKEN":
				return "legendary";
			default:
				return "basic";
		}
		
	}
	
	public ArrayList<NamespacedKey> fromConfig(String path) {
		ArrayList<NamespacedKey> keys = new ArrayList<NamespacedKey>();
		List<String> researches = config.getStringList(path);
		
		if(researches == null) {
			addon.getLogger().log(Level.SEVERE, "Path is invalid.");
			return null;
		}
		
		for( String research : researches ) {
			String[] split = research.split(":");
			addon.getLogger().log(Level.INFO, research);
			JavaPlugin p = Utils.getPluginFromString(split[0]);
			if(p == null) {
				continue;
			}
			NamespacedKey k = new NamespacedKey(p, split[1]);
			keys.add(k);
		}
		
		return keys;
	}
	
	public void RegisterItems() {
		//-- Basic Machine Token
		//-- get the researches
		//-- no recipe type no recipe
		
		
		// Basic Machine Token
		new BasicMachineToken(this.token_group, TOKEN_BASIC_MACHINES, fromConfig("tokens.basic.unlocks"), RecipeType.NULL, new ItemStack[] {}).register(this.addon);
		
		// Advanced Machine Token
		new BasicMachineToken(this.token_group, TOKEN_ADVANCED_MACHINES, fromConfig("tokens.advanced.unlocks"), RecipeType.NULL, new ItemStack[] {}).register(this.addon);
		
		// Elite Machine Token
		
		new BasicMachineToken(this.token_group, TOKEN_ELITE_MACHINES, fromConfig("tokens.elite.unlocks"), RecipeType.NULL, new ItemStack[] {}).register(this.addon);
		// Legendary Machine Token
		new BasicMachineToken(this.token_group, TOKEN_FUTURE_MACHINES, fromConfig("tokens.legendary.unlocks"), RecipeType.NULL, new ItemStack[] {}).register(this.addon);
		
		// iron golem pet
		new IronGolemPet(this.magic_group, IRON_GOLEM_PET, "iron_golem_pet", RecipeType.NULL, new ItemStack[] {}, new ItemStack(Material.IRON_NUGGET)).register(this.addon);
		
		// experience generator
		new ExperienceGenerator(this.machine_group, EXPERIENCE_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT,
				SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.ALUMINUM_BRONZE_INGOT,
				SlimefunItems.DURALUMIN_INGOT, SlimefunItems.EXP_COLLECTOR, SlimefunItems.DURALUMIN_INGOT
		}).register(this.addon);
	}
}
