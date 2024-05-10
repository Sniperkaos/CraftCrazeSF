package me.cworldstar.craftcrazesf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.mooy1.infinitylib.commands.SubCommand;
import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import me.cworldstar.craftcrazesf.api.DamageManager;
import me.cworldstar.craftcrazesf.commands.ListResearch;
import me.cworldstar.craftcrazesf.commands.Reload;
import me.cworldstar.craftcrazesf.commands.Spawn;
import me.cworldstar.craftcrazesf.commands.TestUI;
import me.cworldstar.craftcrazesf.commands.Token;
import me.cworldstar.craftcrazesf.listeners.ArmorListener;
import me.cworldstar.craftcrazesf.listeners.DurabilityModifier;
import me.cworldstar.craftcrazesf.listeners.PlayerJoinEventListener;
import me.cworldstar.craftcrazesf.listeners.SlimefunItemArmorListener;
import me.cworldstar.craftcrazesf.listeners.SpawnListener;
import me.cworldstar.craftcrazesf.listeners.pets.ChickenPetListener;
import me.cworldstar.craftcrazesf.listeners.pets.ExperiencePetListener;
import me.cworldstar.craftcrazesf.listeners.pets.IronGolemPetListener;
import me.cworldstar.craftcrazesf.listeners.pets.SamuraiPetListener;
import me.cworldstar.craftcrazesf.machines.compact.AbstractCompactMachine;
import me.cworldstar.craftcrazesf.mobs.StaticMobController;
import me.cworldstar.craftcrazesf.nightmarket.Nightmarket;
import me.cworldstar.craftcrazesf.utils.LoreFormatter;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import net.milkbowl.vault.economy.Economy;

public class CraftCrazeSF extends AbstractAddon implements SlimefunAddon {

	
	public CraftCrazeSF(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file,
			String githubUserName, String githubRepo, String autoUpdateBranch, String autoUpdateKey) {
		super(loader, description, dataFolder, file, 
				"Sniperkaos", "CraftCrazeSF", "master", "auto-update");
	}

    public CraftCrazeSF() {
    	super("Sniperkaos","SFIllegalActivities","master","auto-update");
    }  
	
    
    public static boolean PlaceholderAPI_Loaded;
  
	public static Config cfg;
	public static Logger logger;
	public static Map<String, AbstractCompactMachine> compactMachines = new HashMap<String, AbstractCompactMachine>();
	public static Map<String, World> compactMachineWorlds = new HashMap<String, World>();
	public static Map<Block, String> compactMachineIds = new HashMap<Block, String>();
	public static Map<Inventory, String> compactMachineInventoryIds = new HashMap<Inventory, String>();
	public static Map<String, Block> compactMachineBlocks = new HashMap<String, Block>();
	public static Map<String, Boolean> compactMachineStatus = new HashMap<String, Boolean>();
	public static Map<String, BlockMenu> compactMachineMenus = new HashMap<String, BlockMenu>();
	public static DamageManager _damage_manager;
	private SpawnListener listener;
	public static Nightmarket nightmarket;
	public static Economy econ;
	
	public static Economy getEconomy() {
		return econ;
	}
	
	public static Nightmarket getNightmarket() {
		return nightmarket;
	}
	
	public static File data_folder;
	
	public static LoreFormatter getLoreFormatter() {
		return new LoreFormatter();
	}
	
	public static Optional<Boolean> getCompactMachineStatus(String id) {
		return Optional.ofNullable(CraftCrazeSF.compactMachineStatus.get(id));
	}
	
	public static Optional<String> getCompactMachineId(Block block) {
		return Optional.ofNullable(CraftCrazeSF.compactMachineIds.get(block));
	}
	
	public static Optional<BlockMenu> menuFromWorld(World w) {
		return Optional.ofNullable(CraftCrazeSF.compactMachineMenus.get(w.getName()));
	}
	
	@Nullable
	public static BlockMenu menuFromLocation(Location location) {
		Optional<String> compact_machine_id = CraftCrazeSF.getCompactMachineId(location.getBlock());
		if(compact_machine_id.isPresent()) {
			return compactMachineMenus.get(compact_machine_id.get());
		}
		return null;	
	}
	
	public static BlockMenu menuFromInventory(Inventory clickedInventory) {
		return compactMachineMenus.get(compactMachineInventoryIds.get(clickedInventory));
	}
	
	public static DamageManager getDamageManager() {
		return CraftCrazeSF._damage_manager;
	}
	
	public static CraftCrazeSF getMainPlugin() {
		return CraftCrazeSF.getPlugin(CraftCrazeSF.class);
	}
	
	public SpawnListener getSpawnListener() {
		return this.listener;
	}
	
	public static void warn(String s) {
		if (!CraftCrazeSF.config().getBoolean("options.debug")) {
			return;
		}
		CraftCrazeSF.logger.log(Level.WARNING, s);
	}
	
	public static void info(String s) {
		if (!CraftCrazeSF.config().getBoolean("options.debug")) {
			return;
		}
		CraftCrazeSF.logger.log(Level.INFO, s);
	}
	
	
	@SuppressWarnings("unused")
	@Override
	public void enable() {
		
        cfg = new Config(this);
        logger = this.getLogger();
        logger.log(Level.INFO, "Enabling CraftCrazeSF");
        
        // registry
        logger.log(Level.INFO, "Creating item registry.");
        Registry registry = new Registry(this, cfg);
        registry.RegisterItems();
        logger.log(Level.INFO, "Item registry done.");
  
        logger.log(Level.INFO, "Loading event listeners...");
        IronGolemPetListener IronGolemPetListener = new IronGolemPetListener();
        ChickenPetListener ChickenPetListener = new ChickenPetListener();
        ExperiencePetListener ExperiencePetListener = new ExperiencePetListener();
        SamuraiPetListener SamuraiPetListener = new SamuraiPetListener();
        SpawnListener SpawnListener = new SpawnListener();
        this.listener = SpawnListener;
        PlayerJoinEventListener PlayerFirstJoinListener = new PlayerJoinEventListener();
        ArmorListener ArmorListener = new ArmorListener(new ArrayList<String>());
        DurabilityModifier DurabilityModifier = new DurabilityModifier();
        SlimefunItemArmorListener SFArmorListener = new SlimefunItemArmorListener();
        _damage_manager = new DamageManager();
        logger.log(Level.INFO, "Event listeners loaded!");
        
        logger.log(Level.INFO, "Loading nightmarket...");
        CraftCrazeSF.nightmarket = new Nightmarket();
        logger.log(Level.INFO, "Nightmarket loaded!");
        
        logger.log(Level.INFO, "Creating commands.");
        //commands
        
        
        SubCommand list_research = new ListResearch(this);
        this.getAddonCommand().addSub(list_research);
        
        SubCommand spawn = new Spawn("spawn", "spawns a custom entity from this plugin", "ccsf.spawn");
        this.getAddonCommand().addSub(spawn);
        
        SubCommand give_token = new Token("token", "gives a player a token", "ccsf.token");
        this.getAddonCommand().addSub(give_token);
        
        SubCommand reload = new Reload("reload", "reloads the config", "ccsf.reload");
        this.getAddonCommand().addSub(reload);
        
        SubCommand nightmarket = new me.cworldstar.craftcrazesf.commands.Nightmarket("nightmarket", "nightmarket related stuff", "ccsf.nightmarket");
        this.getAddonCommand().addSub(nightmarket);
        
        SubCommand TestUI = new TestUI("testui", "test", "ccsf.debug");
        this.getAddonCommand().addSub(TestUI);
        
        
        //-- create the dark matter network
        
        logger.log(Level.INFO, "Commands done.");
     
        data_folder = this.getDataFolder();
     
        logger.log(Level.INFO, "Hooking vault api...");
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            logger.log(Level.SEVERE, "Vault not installed! The nightmarket will not work!");
        } else {
        	CraftCrazeSF.econ = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        }
        
        if(!(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null)) {
        	CraftCrazeSF.PlaceholderAPI_Loaded = true;
        } else {
        	logger.log(Level.INFO, "PlaceholderAPI not found. You will only be able to use %player% for the join message.");
        }
        
        
        logger.log(Level.INFO, "CrazeCraftSF Enabled!");
	}

	@Override
	public void disable() { 
		
		for(Entry<String, World> entry : compactMachineWorlds.entrySet()) {
			logger.log(Level.INFO, "Saving compact world " + entry.getKey());
			entry.getValue().save();
			logger.log(Level.INFO, "Saved world " + entry.getKey() + " successfully.");
		}
		
		StaticMobController.save();
		
	}

	public static void runSync(Runnable runnable) {
		Bukkit.getServer().getScheduler().runTaskLater(CraftCrazeSF.getMainPlugin(), runnable, 0L);
	}



}
