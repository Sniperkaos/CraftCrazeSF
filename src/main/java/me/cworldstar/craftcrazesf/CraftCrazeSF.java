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
import org.bukkit.command.CommandExecutor;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.mooy1.infinitylib.commands.SubCommand;
import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import me.cworldstar.craftcrazesf.commands.ListResearch;
import me.cworldstar.craftcrazesf.commands.Spawn;
import me.cworldstar.craftcrazesf.commands.Token;
import me.cworldstar.craftcrazesf.listeners.ArmorListener;
import me.cworldstar.craftcrazesf.listeners.PlayerJoinEventListener;
import me.cworldstar.craftcrazesf.listeners.SlimefunItemArmorListener;
import me.cworldstar.craftcrazesf.listeners.SpawnListener;
import me.cworldstar.craftcrazesf.listeners.pets.ChickenPetListener;
import me.cworldstar.craftcrazesf.listeners.pets.ExperiencePetListener;
import me.cworldstar.craftcrazesf.listeners.pets.IronGolemPetListener;
import me.cworldstar.craftcrazesf.listeners.pets.SamuraiPetListener;
import me.cworldstar.craftcrazesf.machines.compact.AbstractCompactMachine;
import me.cworldstar.craftcrazesf.mobs.StaticMobController;
import me.cworldstar.craftcrazesf.utils.LoreFormatter;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

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
	
	public static CraftCrazeSF getMainPlugin() {
		return CraftCrazeSF.getPlugin(CraftCrazeSF.class);
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
        PlayerJoinEventListener PlayerFirstJoinListener = new PlayerJoinEventListener();
        ArmorListener ArmorListener = new ArmorListener(new ArrayList<String>());
        SlimefunItemArmorListener SFArmorListener = new SlimefunItemArmorListener();
        logger.log(Level.INFO, "Event listeners loaded!");
        
        
        logger.log(Level.INFO, "Creating commands.");
        //commands
        SubCommand list_research = new ListResearch(this);
        this.getAddonCommand().addSub(list_research);
        
        SubCommand spawn = new Spawn("spawn", "spawns a custom entity from this plugin", "ccsf.spawn");
        this.getAddonCommand().addSub(spawn);
        
        SubCommand give_token = new Token("token", "gives a player a token", "ccsf.token");
        this.getAddonCommand().addSub(give_token);
        
        logger.log(Level.INFO, "Commands done.");
     
        data_folder = this.getDataFolder();
        
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



}
