package me.cworldstar.craftcrazesf;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.mooy1.infinityexpansion.items.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.items.machines.SingularityConstructor;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinityexpansion.items.materials.Singularity;
import io.github.mooy1.infinitylib.groups.MultiGroup;
import io.github.mooy1.infinitylib.groups.SubGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.cworldstar.craftcrazesf.items.AbstractLootBox;
import me.cworldstar.craftcrazesf.items.armors.AdvancedHazmat;
import me.cworldstar.craftcrazesf.items.pets.ChickenPet;
import me.cworldstar.craftcrazesf.items.pets.ExperiencePet;
import me.cworldstar.craftcrazesf.items.pets.IlluminatiPet;
import me.cworldstar.craftcrazesf.items.pets.IronGolemPet;
import me.cworldstar.craftcrazesf.items.pets.SamuraiPet;
import me.cworldstar.craftcrazesf.items.recipe.NaniteSynthesizerRecipe;
import me.cworldstar.craftcrazesf.items.tokens.BasicMachineToken;
import me.cworldstar.craftcrazesf.items.tools.WorldEater;
import me.cworldstar.craftcrazesf.machines.AutomaticSieve;
import me.cworldstar.craftcrazesf.machines.AutomaticWasher;
import me.cworldstar.craftcrazesf.machines.ChunkLoader;
import me.cworldstar.craftcrazesf.machines.ExperienceGenerator;
import me.cworldstar.craftcrazesf.machines.NaniteSynthesizer;
import me.cworldstar.craftcrazesf.machines.compact.SmallCompactMachine;
import me.cworldstar.craftcrazesf.machines.compact.io.CompactMachineIO;
import me.cworldstar.craftcrazesf.utils.Utils;

public class Registry {
	
	private MultiGroup main_group;
	private SubGroup token_group;
	private SubGroup machine_group;
	private SubGroup magic_group;
	private SubGroup armor_group;
	private SubGroup pet_group;
	private SubGroup mob_drops_group;
	private SubGroup materials_group;
	private CraftCrazeSF addon;
	private Config config;
	
	

	public static final CustomItemStack ITEM_GROUP_MAIN = new CustomItemStack(Material.PURPLE_CANDLE, "&dCrazeCraft SF", "", "&d> Click to open!");
	public static final CustomItemStack ITEM_GROUP_TOKENS = new CustomItemStack(Material.DIAMOND,"&fResearch Tokens","","&d> Click to open!");
	public static final CustomItemStack ITEM_GROUP_MACHINES = new CustomItemStack(Material.DROPPER,"&6Machines","","&d> Click to open!");
	public static final CustomItemStack ITEM_GROUP_MAGIC = new CustomItemStack(Material.ENCHANTED_BOOK,"&9Magic Items","","&d> Click to open!");
	public static final CustomItemStack ITEM_GROUP_ARMOR = new CustomItemStack(Material.CHAINMAIL_CHESTPLATE,"&eTools and Armors","","&d> Click to open!");
	public static final CustomItemStack ITEM_GROUP_MATERIALS = new CustomItemStack(Material.IRON_INGOT,"&7Materials","","&d> Click to open!");
	public static final ItemStack ITEM_GROUP_PETS = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTk2N2QwMTUyY2YzMDU0YjEzNjc5NjE2YmNjOWRhOGVmYTM0ZjE1NTlhOGU4OTIwNDlhMTkwMGVlZDA3OGI4MCJ9fX0=");
	public static final SlimefunItemStack ITEM_GROUP_MOB_DROPS = new SlimefunItemStack("MOB_DROP_TOKEN", new CustomItemStack(Material.ZOMBIE_HEAD, "&6Mob Drops"));
	
	public static final SlimefunItemStack NANITE_SYNTHESIZER = new SlimefunItemStack(
			
			"NANITE_SYNTHESIZER",
			SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzgyZDU4ZTg5M2JmOWI4MjQzMzYyNmQwZTQ4MGU5Mzc1YjU4ZjNjMmRkZWY0NTBmNjE3YjYwZGJmNTA2Y2JiOSJ9fX0="),
			"&6&lNanite Synthesizer", 
			"", 
			"&e> Place this anywhere 5,000 blocks away from 0,0.", 
			"&e> RATE: 1 NANO PARTICLE/30 MINUTES", 
			"&c&nWARNING&r&e: If this machine reaches 600 heat,", 
			"&eit will violently explode!"
			
	);
	
	public static final SlimefunItemStack NANO_PARTICLES = new SlimefunItemStack("NANO_PARTICLE", 
			
			SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU5NTI2MGRiMmIyOGRlYTcyYzJiNDI1MmExODZkNDFkNjk0YjBkN2M4NTliMmFhN2I4OTFjMzFmNTk4OWRiOCJ9fX0="),
			"&fNano Particle",
			"&e>Moving your way up in the world."
			
	);
	
	public static final SlimefunItemStack NANO_ALLOY = new SlimefunItemStack("NANO_ALLOY",
		
			SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQwYWVhODVlYjkzYTRhMDgwNGFmM2Q1OWEyZmViZTFjNDNhMDc2Njg1NTc0YzUzODg0MmU0M2EyMTA5MGMyYyJ9fX0="),
			"&fNano Alloy",
			"&e> Very impressive flex."
			
	);
	
	public static final SlimefunItemStack NANO_CORE = new SlimefunItemStack("NANO_CORE",
		
			SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdjOGIzYjBmN2RjMmMyY2E1YzExMjhlZGE0MTRmZmE4OWIzZDA0MWE4MWJlMmNlNjEwMzMwYjJhMzU3MjIzNiJ9fX0="),
			"&fNano Core",
			"&e> A core made from millions",
			"&e> of condensed nanites."
			
	);
			
			
			
	
	
	public static SlimefunItemStack WORLD_BREAKER = new SlimefunItemStack("WORLD_BREAKER",Material.NETHERITE_PICKAXE,"&a&l&k|||&r &7&lWorld Breaker&r &a&l&k|||&r","&7Efficiency XV","&7Fortune XV","&7Unbreaking XX","",LoreBuilder.material("Nanite Alloy"), ""," &7- Right-Click to enable Silk Touch.","",LoreBuilder.powerCharged(0, 12000), "",LoreBuilder.radioactive(Radioactivity.VERY_DEADLY),LoreBuilder.HAZMAT_SUIT_REQUIRED);
	
	static {
		WORLD_BREAKER.addUnsafeEnchantment(Enchantment.DIG_SPEED, 15);
		WORLD_BREAKER.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 15);
		WORLD_BREAKER.addUnsafeEnchantment(Enchantment.DURABILITY, 20);
		ItemMeta NewPickaxeItemMeta = WORLD_BREAKER.getItemMeta();
		NewPickaxeItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		WORLD_BREAKER.setItemMeta(NewPickaxeItemMeta);
	}
	
	public static final ItemStack IRON_GOLEM_PET_ITEM = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTgyZDcxMTVkMDFjMmMwYTQxMTAwNTM2NzdjM2JmMDk1NTQ2ZGFhNmQ4ZTEyY2NmMTQ4MDEwMTgyZjNiMGVkMSJ9fX0=");
	public static final CustomItemStack EXPERIENCE_GENERATOR_ITEM = new CustomItemStack(Material.CYAN_CONCRETE);
	public static final ItemStack ILLUMINATI_PET_ITEM = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTg0ZjQ4MzBiYmMzZWZjNTAyZmU0NDM3NzY3MzQ3ZjhjYmQ3NzczMTk5MzE1YjY4Nzg5NmJhODExMDFmMjM2In19fQ==");
	public static final ItemStack CHICKEN_PET_ITEM = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc3NzA3MzE3NzE0M2Y5NDc2ODQ0YTc4ZTJlZGFiZmIzYTg2ZjNmZTMxYWIxYzBmODhiZTdiM2Y3NjljYjk1ZSJ9fX0=");
	public static final CustomItemStack AUTOMATIC_SIEVE_ITEM = new CustomItemStack(Material.FLETCHING_TABLE);
	public static final ItemStack EXPERIENCE_PET_ITEM = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdkODg1YjMyYjBkZDJkNmI3ZjFiNTgyYTM0MTg2ZjhhNTM3M2M0NjU4OWEyNzM0MjMxMzJiNDQ4YjgwMzQ2MiJ9fX0=");
	public static final CustomItemStack AUTOMATIC_WASHER_ITEM = new CustomItemStack(Material.CAULDRON);
	public static final CustomItemStack CHUNK_LOADER_ITEM = new CustomItemStack(Material.BEACON);
	public static final CustomItemStack ADVANCED_DISENCHANTER_ITEM = new CustomItemStack(Material.ENCHANTING_TABLE);
	public static final CustomItemStack SMALL_COMPACT_MACHINE_ITEM = new CustomItemStack(Material.CRYING_OBSIDIAN);
	public static final CustomItemStack COMPACT_MACHINE_IO_ITEM = new CustomItemStack(Material.NETHERITE_BLOCK);
	public static final CustomItemStack SAMURAI_PET_ITEM = new CustomItemStack(SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI5ZWZlOWU2N2Y3ZmFjYmNlOTViNWVlM2E5Nzc5ZWFlZDM4OTc4ODRjNGNhY2I5MTAxN2Q2OWYzNDMzMTg0YiJ9fX0="), "&6Pets");
	
	// Loot Chests
	public static final ItemStack RARE_LOOT_CHEST_HEAD = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU3ZWY0ZTNmYmFhMGJmNzk5ZGQxMzY5N2UyYzBmMzM5NTVhNGEwZGFiMmYyOTkyZGExN2FhMjllODFhZGY4NyJ9fX0=");
	public static final ItemStack UNCOMMON_LOOT_CHEST_HEAD = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE5OGEzMjVmMGIzN2NkMjcwZjU4YmIwOWNiOWQ3M2UxYmIwODdjYWM2MzJkZjJhNWYwNzIzMjUzMzRjNTQwIn19fQ==");
	public static final ItemStack EPIC_LOOT_CHEST_HEAD = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZlZTlkZDYzNTNiZjY4ZWM5OTRhNGZkOWExYmE1YzRlNDdlODg1YzdhOGJhNWEzN2IwYzgwOTkxMjJkY2UxMSJ9fX0=");
	public static final ItemStack LEGENDARY_LOOT_CHEST_HEAD = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODhkMzg2NGNhNzU3MTAxYThmN2Y2MGI4MDkwNjVlNWQzMGE3YWQzMjQwMGE5OGVhYTU3OWNlZjAyOGExNGRjYSJ9fX0=");
	public static final ItemStack MYTHICAL_LOOT_CHEST_HEAD = SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQxMmRjNTQzYTRiMGUwMzM5OGY2ZjBhZjFmNjgzNDBiZjU1Zjg5OTEwYjA4Nzk0ZWQ3MjM1NGJlMDcxY2ZjMCJ9fX0=");
	
	
	public static final SlimefunItemStack UNCOMMON_CHEST = new SlimefunItemStack(
			"SFDRUGS_UNCOMMON_LOOT_CHEST",
			UNCOMMON_LOOT_CHEST_HEAD,
			"&aUncommon Loot Chest",
			"",
			"&aThe most common loot chest. Contains very common resources.",
			"&e=>&r " + LoreBuilder.RIGHT_CLICK_TO_USE);
	public static final SlimefunItemStack RARE_CHEST = new SlimefunItemStack("RARE_LOOT_CHEST",
			RARE_LOOT_CHEST_HEAD,
			"&bRare Loot Chest",
			"",
			"&bWhile the items in this chest would be considered rare, they're quite common.",
			"&bUse these resources to build up your strength.",
			"&e=>&r " + LoreBuilder.RIGHT_CLICK_TO_USE);
	public static final SlimefunItemStack EPIC_CHEST = new SlimefunItemStack("EPIC_LOOT_CHEST",
			EPIC_LOOT_CHEST_HEAD,
			"&6Epic Loot Chest",
			"",
			"&6The Epic loot chest is filled with industrial-age advanced technology.",
			"&6Warlords look at you with awe and jealousy.",
			"&e=>&r " + LoreBuilder.RIGHT_CLICK_TO_USE);
	public static final SlimefunItemStack LEGENDARY_CHEST = new SlimefunItemStack("LEGENDARY_LOOT_CHEST",
			LEGENDARY_LOOT_CHEST_HEAD,
			"&cLegendary Loot Chest",
			"",
			"&cIt is said that not even the filled vaults of saudi princes could buy even the",
			"&cheapest of items contained in these. Be proud of your achievement, and your newfound riches.",
			"&e=>&r " + LoreBuilder.RIGHT_CLICK_TO_USE);
	public static final SlimefunItemStack MYTHICAL_CHEST = new SlimefunItemStack("MYTHICAL_LOOT_CHEST",
			MYTHICAL_LOOT_CHEST_HEAD,
			"&dMythical Loot Chest",
			"",
			"&dThe items contained in the Mythical chest are said to be impossible. Legends, even.",
			"&dBut today, you prove them wrong. Claim your prize, you've earned it.",
			"&e=>&r " + LoreBuilder.RIGHT_CLICK_TO_USE
	);
	public static final SlimefunItemStack CORPORATE_LOCKBOX = new SlimefunItemStack("CORPORATE_LOOT_CHEST",
		
			SlimefunUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E2MTAwODBiMDEyNWZjNDI3M2JhNjAzZmIzOGZlYWFkZDFhZjAwMGI1ZmM3NGFmYTE4Y2VlNDU4NzRkMTk5NyJ9fX0="),
			"&4&lCorporate Lockbox",
			"",
			"&4Nobody knows how these are distributed. But to those lucky few who are able to decode them,",
			"&4not even the universe is safe from your newfound ambition and power.",
			"&e=>&r " + LoreBuilder.RIGHT_CLICK_TO_OPEN
			
	);
	
	
	// tokens
	public static SlimefunItemStack TOKEN_BASIC_MACHINES; 
	public static SlimefunItemStack TOKEN_ADVANCED_MACHINES; 
	public static SlimefunItemStack TOKEN_ELITE_MACHINES; 
	public static SlimefunItemStack TOKEN_FUTURE_MACHINES;
	
	//pets
	
	public static final SlimefunItemStack PET_CATALYST = new SlimefunItemStack("PET_CATALYST", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIwMWFlMWE4YTA0ZGY1MjY1NmY1ZTQ4MTNlMWZiY2Y5Nzg3N2RiYmZiYzQyNjhkMDQzMTZkNmY5Zjc1MyJ9fX0=","&ePet Catalyst");
	public static SlimefunItemStack IRON_GOLEM_PET;
	public static SlimefunItemStack ILLUMINATI_PET;
	public static SlimefunItemStack CHICKEN_PET;
	public static SlimefunItemStack EXPERIENCE_PET;
	public static SlimefunItemStack SAMURAI_PET;
	
	
	//machines
	public static SlimefunItemStack EXPERIENCE_GENERATOR;
	public static SlimefunItemStack AUTOMATIC_SIEVE;
	public static SlimefunItemStack AUTOMATIC_WASHER;
	public static SlimefunItemStack CHUNK_LOADER;
	public static SlimefunItemStack ADVANCED_DISENCHANTER;

	
	// compact machines
	public static SlimefunItemStack SMALL_COMPACT_MACHINE;
	public static SlimefunItemStack COMPACT_MACHINE_IO;
	
	//armors
	public static CustomItemStack ADVANCED_HAZMAT_BOOTS_ITEM = new CustomItemStack(Material.LEATHER_BOOTS);
	static {
		LeatherArmorMeta meta = (LeatherArmorMeta) ADVANCED_HAZMAT_BOOTS_ITEM.getItemMeta();
		meta.setDisplayName(Utils.formatString("&4Advanced Hazmat Boots"));
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(Utils.formatString("&7Stronger than diamond!"));
		lore.add(Utils.formatString("&7Full Set Effects:"));
		lore.add(Utils.formatString("&e- Radiation immunity."));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE);
		meta.setColor(Color.BLACK);
		meta.setLore(lore);
		meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(),"SFDRUGS_DRUG_ARMOR",10,Operation.ADD_NUMBER,EquipmentSlot.FEET));
		ADVANCED_HAZMAT_BOOTS_ITEM.setItemMeta(meta);
	}
	
	public static SlimefunItemStack ADVANCED_HAZMAT_BOOTS;
	
	
	public Registry(CraftCrazeSF plugin, Config cfg) {
		
		Registry.TOKEN_BASIC_MACHINES = new SlimefunItemStack("BASIC_MACHINE_TOKEN", Material.EMERALD, cfg.getString("tokens.basic.name"), "", cfg.getString("tokens.basic.desc"));
		Registry.TOKEN_ADVANCED_MACHINES = new SlimefunItemStack("ADVANCED_MACHINE_TOKEN", Material.DIAMOND, cfg.getString("tokens.advanced.name"), "", cfg.getString("tokens.advanced.desc"));
		Registry.TOKEN_ELITE_MACHINES = new SlimefunItemStack("ELITE_MACHINE_TOKEN", Material.DIAMOND, cfg.getString("tokens.elite.name"), "", cfg.getString("tokens.elite.desc"));
		Registry.TOKEN_FUTURE_MACHINES = new SlimefunItemStack("FUTURE_MACHINE_TOKEN", Material.DIAMOND, cfg.getString("tokens.legendary.name"), "", cfg.getString("tokens.legendary.desc"));
		Registry.IRON_GOLEM_PET = new SlimefunItemStack("IRON_GOLEM_PET", IRON_GOLEM_PET_ITEM, "&fIron Golem Pet", "", "&f>> &4&nRight-Click&r &fto enable!", "","&fReduces damage taken by 5%.","","&fCurrently: {status}.", "&fFavorite Food: &nIron Nugget&r&f.");
		Registry.EXPERIENCE_GENERATOR = new SlimefunItemStack("EXPERIENCE_GENERATOR", EXPERIENCE_GENERATOR_ITEM, "&eExperience Generator", "", "&f>> Passively creates experience.", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),LoreBuilder.powerBuffer(2000) ,LoreBuilder.powerPerSecond(200));
		Registry.ILLUMINATI_PET = new SlimefunItemStack("ILLUMINATI_PET", ILLUMINATI_PET_ITEM, "&eIlluminati Pet", "", "&f>> &4&nRight-Click&r &fto use!", "","&eSpawns in random items.","","&fFavorite Food: &b&nDiamond Block&r."); 
		Registry.CHICKEN_PET = new SlimefunItemStack("CHICKEN_PET", CHICKEN_PET_ITEM, "&fChicken Pet", "", "&f>> You take no fall damage.", "", "&fFavorite Food: &nSeeds&r&f.");
		Registry.AUTOMATIC_SIEVE = new SlimefunItemStack("AUTOMATIC_SIEVE", AUTOMATIC_SIEVE_ITEM, "&2Automatic Sieve", "", "&7> Requires &c&nfire&r&7 underneath!","&7> &6Does not require power&7!","","&7> Will output to nearby chests.");
		Registry.ADVANCED_HAZMAT_BOOTS = new SlimefunItemStack("ADVANCED_HAZMAT_BOOTS", ADVANCED_HAZMAT_BOOTS_ITEM);
		Registry.EXPERIENCE_PET = new SlimefunItemStack("EXPERIENCE_PET", EXPERIENCE_PET_ITEM, "&aExperience Pet", "", "&f>> Gain 50% more exp.", "&fFavorite Food: &e&nBottle o' Enchanting&r&f.");
		Registry.AUTOMATIC_WASHER = new SlimefunItemStack("AUTOMATIC_WASHER", AUTOMATIC_WASHER_ITEM, "&fAutomatic Washer","", "&7> Requires &c&nfire&r&7 underneath!","&7> &6Does not require power&7!", "","&7> Will output to nearby chests.");
		Registry.CHUNK_LOADER = new SlimefunItemStack("CCSF_CHUNK_LOADER", CHUNK_LOADER_ITEM, "&fChunk Loader", "", "&f> Force loads a chunk it's in.");
		Registry.ADVANCED_DISENCHANTER = new SlimefunItemStack("SF_ADVANCED_DISENCHANTER", ADVANCED_DISENCHANTER_ITEM, "&cAE Advanced Disenchanter", "", "&f> Can remove AE enchants!");
		Registry.SMALL_COMPACT_MACHINE = new SlimefunItemStack("SMALL_COMPACT_MACHINE", SMALL_COMPACT_MACHINE_ITEM, "&fSmall Compact Machine", "", "&f> Contains a small 8x8 dimension.");
		Registry.COMPACT_MACHINE_IO = new SlimefunItemStack("COMPACT_MACHINE_IO", COMPACT_MACHINE_IO_ITEM, "&fCompact Machine IO", "", "&f> Allows items to be moved between the compact", "&fmachine and the dimension.");
		Registry.SAMURAI_PET = new SlimefunItemStack("SAMURAI_PET", SAMURAI_PET_ITEM, "&6Samurai Pet", "", "&f> You are able to critically strike for", "&f75% extra damage every 8 hits.");
		
		SubGroup token_group = new SubGroup("token_group", Registry.ITEM_GROUP_TOKENS);
		SubGroup machine_group = new SubGroup("machine_group", Registry.ITEM_GROUP_MACHINES);
		SubGroup magic_group = new SubGroup("magic_group", Registry.ITEM_GROUP_MAGIC);
		SubGroup armor_group = new SubGroup("armor_group", Registry.ITEM_GROUP_ARMOR);
		SubGroup pet_group = new SubGroup("pet_group", Registry.ITEM_GROUP_PETS);
		SubGroup mob_drops = new SubGroup("mob_drops", Registry.ITEM_GROUP_MOB_DROPS);
		SubGroup materials_group = new SubGroup("materials_group", ITEM_GROUP_MATERIALS);
		
		MultiGroup craze_craft_sf = new MultiGroup("main_group",Registry.ITEM_GROUP_MAIN, token_group, machine_group, magic_group, armor_group, pet_group );
		craze_craft_sf.register(plugin);
		
    	this.main_group = craze_craft_sf;
    	this.token_group = token_group;
    	this.machine_group = machine_group;
    	this.magic_group = magic_group;
    	this.armor_group = armor_group;
    	this.pet_group = pet_group;
    	this.mob_drops_group = mob_drops;
    	this.materials_group = materials_group;
    	
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
		
		RecipeType NANITE_SYNTHESIZER_RECIPE = new NaniteSynthesizerRecipe(CraftCrazeSF.createKey("nsr"), NANITE_SYNTHESIZER, new String[] {
				"§7Made using the nanite synthesizer."
		});
		
		// Basic Machine Token
		BasicMachineToken common_token = new BasicMachineToken(this.token_group, TOKEN_BASIC_MACHINES, fromConfig("tokens.basic.unlocks"), RecipeType.NULL, new ItemStack[] {});
		common_token.register(this.addon);
		
		// Advanced Machine Token
		new BasicMachineToken(this.token_group, TOKEN_ADVANCED_MACHINES, fromConfig("tokens.advanced.unlocks"), RecipeType.NULL, new ItemStack[] {}).register(this.addon);
		
		// Elite Machine Token
		
		new BasicMachineToken(this.token_group, TOKEN_ELITE_MACHINES, fromConfig("tokens.elite.unlocks"), RecipeType.NULL, new ItemStack[] {}).register(this.addon);
		// Legendary Machine Token
		new BasicMachineToken(this.token_group, TOKEN_FUTURE_MACHINES, fromConfig("tokens.legendary.unlocks"), RecipeType.NULL, new ItemStack[] {}).register(this.addon);
		
		// iron golem pet
		SlimefunItem PetCatalyst = new SlimefunItem(this.magic_group,PET_CATALYST, RecipeType.MAGIC_WORKBENCH, new ItemStack[] {
				SlimefunItems.BLANK_RUNE, SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3,
				SlimefunItems.MAGIC_LUMP_3, new ItemStack(Material.EGG), SlimefunItems.MAGIC_LUMP_3,
				SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3, SlimefunItems.BLANK_RUNE
		});
		PetCatalyst.register(addon);
		new IronGolemPet(this.pet_group, IRON_GOLEM_PET, "IRON_GOLEM_PET", RecipeType.NULL, new ItemStack[] {}, new ItemStack(Material.IRON_NUGGET)).register(this.addon);
		new IlluminatiPet(this.pet_group, ILLUMINATI_PET, RecipeType.NULL, new ItemStack[] {}, new ItemStack(Material.DIAMOND_BLOCK)).register(this.addon);
		new ChickenPet(this.pet_group, CHICKEN_PET,"CHICKEN_PET", RecipeType.NULL, new ItemStack[] {}, new ItemStack(Material.WHEAT_SEEDS), 8).register(this.addon);
		new ExperiencePet(this.pet_group, EXPERIENCE_PET, "EXPERIENCE_PET", RecipeType.ANCIENT_ALTAR, new ItemStack[] {
				new ItemStack(Material.EXPERIENCE_BOTTLE), new ItemStack(Material.NETHERITE_BLOCK), SlimefunItems.TALISMAN_WISE, 
				SlimefunItems.TALISMAN_TRAVELLER, PET_CATALYST , SlimefunItems.ENCHANTMENT_RUNE, 
				SlimefunItems.ENCHANTMENT_RUNE, SlimefunItems.TALISMAN_HUNTER, SlimefunItems.TALISMAN_WIZARD
		}, new ItemStack(Material.EXPERIENCE_BOTTLE), 2).register(this.addon);
		
		
		// experience generator
		new ExperienceGenerator(this.machine_group, EXPERIENCE_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT,
				SlimefunItems.ALUMINUM_BRONZE_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.ALUMINUM_BRONZE_INGOT,
				SlimefunItems.DURALUMIN_INGOT, SlimefunItems.EXP_COLLECTOR, SlimefunItems.DURALUMIN_INGOT
		}).register(this.addon);
		
		new SamuraiPet(this.pet_group, SAMURAI_PET, "SAMURAI_PET", RecipeType.MAGIC_WORKBENCH, new ItemStack[] {
				PET_CATALYST, new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.DIAMOND_SWORD),
				new ItemStack(Material.IRON_SWORD), new ItemStack(Material.GOLDEN_SWORD), PET_CATALYST,
				SlimefunItems.SWORD_OF_BEHEADING, PET_CATALYST, SlimefunItems.SEISMIC_AXE
		}).register(addon);
		
		new NaniteSynthesizer(this.machine_group, NANITE_SYNTHESIZER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				Materials.MACHINE_CIRCUIT, Materials.MACHINE_CIRCUIT, Materials.MACHINE_CIRCUIT,
				Materials.ADAMANTITE, Materials.MACHINE_CORE, Materials.ADAMANTITE,
				Materials.VOID_INGOT, Materials.VOID_INGOT, Materials.VOID_INGOT
		}).register(addon);
		
		// hazmat boots
		new AdvancedHazmat(this.armor_group, ADVANCED_HAZMAT_BOOTS, RecipeType.NULL, new ItemStack[] {}, null, null).register(this.addon);
		
		// test enchanted item
		
		//automatic sieve
		new AutomaticSieve(this.machine_group, AUTOMATIC_SIEVE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				SlimefunItems.COBALT_INGOT, SlimefunItems.NICKEL_INGOT, SlimefunItems.COBALT_INGOT,
				SlimefunItems.CARBONADO, SlimefunItems.CARBON_CHUNK, SlimefunItems.CARBONADO,
				SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_ALLOY_INGOT
		}).register(this.addon);
		new AutomaticWasher(this.machine_group, AUTOMATIC_WASHER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				SlimefunItems.COBALT_INGOT, SlimefunItems.NICKEL_INGOT, SlimefunItems.COBALT_INGOT,
				SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.WATER_RUNE, SlimefunItems.ADVANCED_CIRCUIT_BOARD,
				SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_ALLOY_INGOT
		}).register(this.addon);
		//new AdvancedDisenchanter(this.machine_group, ADVANCED_DISENCHANTER, RecipeType.NULL, new ItemStack[] {
		//		SlimefunItems.AUTO_DISENCHANTER_2, SlimefunItems.BOOSTED_URANIUM
		//}).register(this.addon);
		new ChunkLoader(this.machine_group, CHUNK_LOADER, RecipeType.NULL, new ItemStack[] {}).register(this.addon);
		new SmallCompactMachine(this.machine_group, SMALL_COMPACT_MACHINE, RecipeType.NULL, new ItemStack[] {}).register(this.addon);
		new CompactMachineIO(this.machine_group, COMPACT_MACHINE_IO, RecipeType.NULL, new ItemStack[] {}).register(this.addon);
		new WorldEater(this.armor_group, WORLD_BREAKER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				NANO_ALLOY,NANO_ALLOY,NANO_ALLOY,
				null,NANO_CORE, null,
				NANO_CORE,null,null
				
		}).register(this.addon);
		
		
		//materials
		new SlimefunItem(this.materials_group, NANO_PARTICLES, NANITE_SYNTHESIZER_RECIPE, new ItemStack[] {}).register(addon);
		new SlimefunItem(this.materials_group, NANO_ALLOY, RecipeType.SMELTERY, new ItemStack[] {
				new CustomItemStack(NANO_PARTICLES, 16), new CustomItemStack(SlimefunItems.PLUTONIUM, 32), new CustomItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 64),
				new CustomItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 64), new CustomItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 64), new CustomItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 64),
				new CustomItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 64), new CustomItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 64), new CustomItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 64)
		}).register(addon);
		
		
		new SlimefunItem(this.materials_group, NANO_CORE , RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
			new CustomItemStack(NANO_PARTICLES, 64), new CustomItemStack(NANO_PARTICLES, 64), new CustomItemStack(NANO_PARTICLES, 64),
			new CustomItemStack(NANO_PARTICLES, 64), new CustomItemStack(NANO_PARTICLES, 64), new CustomItemStack(NANO_PARTICLES, 64),
			Materials.INFINITE_INGOT,Materials.INFINITE_INGOT,Materials.INFINITE_INGOT
		}).register(addon);
		

		
		// uncommon loot box
		AbstractLootBox UncommonLootBox = new AbstractLootBox(this.mob_drops_group, UNCOMMON_CHEST, RecipeType.MOB_DROP, new ItemStack[] {});
		UncommonLootBox.addDrop(new CustomItemStack(SlimefunItems.ADVANCED_CIRCUIT_BOARD,  8), 4);
		UncommonLootBox.addDrop(new CustomItemStack(SlimefunItems.ELECTRIC_MOTOR,  2), 4);
		UncommonLootBox.addDrop(new CustomItemStack(SlimefunItems.ANDROID_MEMORY_CORE,  1), 4);
		UncommonLootBox.addDrop(new CustomItemStack(SlimefunItems.BATTERY,  10), 5);
		UncommonLootBox.addDrop(new CustomItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT,  8), 3);
		UncommonLootBox.addDrop(new CustomItemStack(common_token.getItem(), 1), 4);
		UncommonLootBox.register(addon);
		
		
		
	}
}
