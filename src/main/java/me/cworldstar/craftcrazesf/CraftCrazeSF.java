package me.cworldstar.craftcrazesf;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import me.cworldstar.craftcrazesf.commands.ListResearch;
import me.cworldstar.craftcrazesf.commands.Token;
import me.cworldstar.craftcrazesf.listeners.IronGolemPetListener;
import me.cworldstar.craftcrazesf.machines.ExperienceGenerator;

public class CraftCrazeSF extends AbstractAddon implements SlimefunAddon {

	
	public CraftCrazeSF(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file,
			String githubUserName, String githubRepo, String autoUpdateBranch, String autoUpdateKey) {
		super(loader, description, dataFolder, file, 
				"Sniperkaos", "CraftCrazeSF", "master", "auto-update");
	}

    public CraftCrazeSF() {
    	super("Sniperkaos","SFIllegalActivities","master","auto-update");
    }  
	
	public static Config cfg;
	public static Logger logger;
	
	
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
        logger.log(Level.INFO, "Event listeners loaded!");
        
        
        logger.log(Level.INFO, "Creating commands.");
        //commands
        CommandExecutor list_research = new ListResearch(this);
        this.getCommand("list_research").setExecutor(list_research);
        
        CommandExecutor give_token = new Token();
        this.getCommand("token").setExecutor(give_token);
        
        logger.log(Level.INFO, "Commands done.");
     
        
        logger.log(Level.INFO, "CrazeCraftSF Enabled!");
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
	}

}
