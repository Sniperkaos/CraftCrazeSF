package me.cworldstar.craftcrazesf.items;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.utils.ParticleUtils;
import me.cworldstar.craftcrazesf.utils.Speak;

public class Drug extends SlimefunItem implements Listener {
	private PotionEffect[] potionEffects;
	private PotionEffect[] overdoseEffects;
	private int overdoseLimit;
	private int overdoseTicks;
	private String drugId;
	private String overdoseMessage;
	/**
	 * 
	 * @param itemGroup
	 * @param item
	 * @param recipeType
	 * @param recipe
	 * @param effects
	 */
	public Drug(String drugId, ItemGroup itemGroup, SlimefunItemStack item,RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects) {
		super(itemGroup, item, recipeType, recipe);
		this.drugId = drugId;
		this.potionEffects = effects;
		this.overdoseLimit = 0;
		this.overdoseEffects = new PotionEffect[] {};
		CraftCrazeSF.instance().getServer().getPluginManager().registerEvents(this, CraftCrazeSF.instance());
	}

	/**
	 * @param String drugId
	 * @param ItemGroup itemGroup
	 * @param SlimefunItemStackitem
	 * @param RecipeType recipeType
	 * @param ItemStack[] recipe
	 * @param PotionEffect[] effects
	 * @param int overdoseLimit
	 * @param int overdoseTicks
	 * @param String overdoseText
	 * @param PotionEffect[] overdoseEffects
	 * 
	 */
	public Drug(String drugId, ItemGroup itemGroup, SlimefunItemStack item,RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects, int overdoseLimit, int overdoseTicks, String overdoseMessage, PotionEffect[] overdoseEffects) {
		super(itemGroup, item, recipeType, recipe);
		this.drugId = drugId;
		this.potionEffects = effects;
		this.overdoseLimit = overdoseLimit;
		this.overdoseEffects = overdoseEffects;
		this.overdoseTicks = overdoseTicks;
		this.overdoseMessage = overdoseMessage;
		CraftCrazeSF.instance().getServer().getPluginManager().registerEvents(this, CraftCrazeSF.instance());
	}
	
	@Override
	public void preRegister() {
        ItemUseHandler itemUseHandler = this::onItemUseRightClick;
        addItemHandler(itemUseHandler);
	}
    private void onItemUseRightClick(PlayerRightClickEvent event) {
    	
    	if(event.getPlayer().getPersistentDataContainer().has(CraftCrazeSF.createKey(this.drugId + "_overdosing"), PersistentDataType.INTEGER)) {
    		event.cancel();
    		return;
    	}
    	
    	if(event.getPlayer().getPersistentDataContainer().get(CraftCrazeSF.createKey(this.drugId + "_overdose"), PersistentDataType.INTEGER) == null) {
    		event.getPlayer().getPersistentDataContainer().set(CraftCrazeSF.createKey(this.drugId + "_overdose"), PersistentDataType.INTEGER, 1);
    	}
    	
    	for( PotionEffect effect : this.potionEffects) {
            event.getPlayer().addPotionEffect(effect);
            int overdosing = event.getPlayer().getPersistentDataContainer().get(CraftCrazeSF.createKey(this.drugId + "_overdose"), PersistentDataType.INTEGER);
            if(overdosing >= this.overdoseLimit && event.getPlayer().getPersistentDataContainer().get(CraftCrazeSF.createKey(this.drugId  + "_overdosing"),PersistentDataType.INTEGER) == null) {
            	event.getPlayer().getPersistentDataContainer().set(CraftCrazeSF.createKey(this.drugId  + "_overdosing"), PersistentDataType.INTEGER, 1);
            	this.applyOverdoseEffects(event.getPlayer());
            } else {
            	event.getPlayer().getPersistentDataContainer().set(CraftCrazeSF.createKey(this.drugId  + "_overdose"), PersistentDataType.INTEGER, event.getPlayer().getPersistentDataContainer().get(CraftCrazeSF.createKey(this.drugId + "_overdose"), PersistentDataType.INTEGER) + 1);
            }
    	}
        event.getItem().setAmount(event.getItem().getAmount()-1);
    }

	@EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {	
		if(e.getPlayer().getPersistentDataContainer().get(CraftCrazeSF.createKey(this.drugId  + "_overdosing"),PersistentDataType.INTEGER) != null && e.getItem().isSimilar(new ItemStack(Material.MILK_BUCKET))) {
			new Speak(e.getPlayer(), "&4&lOh god! What was that horrid liquid?!");
			e.setCancelled(true);
		}
	}
	
	private void applyOverdoseEffects(Player p) {
		// TODO Auto-generated method stub
		for(PotionEffect pEffect : this.overdoseEffects) {
			p.addPotionEffect(pEffect);
		}
		new Speak(p, "&4&lYou feel like you took too much " + this.getItemName() + "&4&l!");
		new Speak(p, "&4&lYou ".concat(this.overdoseMessage));
		int max_ticks = this.overdoseTicks;
		String itemName = this.drugId;
		
		
		new BukkitRunnable() {
			int ticks_elapsed = 0;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(ticks_elapsed >= max_ticks) {
					p.getPersistentDataContainer().remove(CraftCrazeSF.createKey(itemName + "_overdosing"));
					p.getPersistentDataContainer().set(CraftCrazeSF.createKey(itemName + "_overdose"), PersistentDataType.INTEGER, 1);
					for(PotionEffect eff : p.getActivePotionEffects()) {
						p.removePotionEffect(eff.getType());
					}
					new Speak(p, "&e&lYou feel better now.");
					this.cancel();
				} else {
					ticks_elapsed += 2;
					ParticleUtils.DripFromHead(p, Particle.CRIMSON_SPORE);
				}
			}
			
		}.runTaskTimer(CraftCrazeSF.getPlugin(CraftCrazeSF.class), 0, 2);
	}
}
