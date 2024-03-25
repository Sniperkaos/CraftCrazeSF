package me.cworldstar.craftcrazesf.items.tokens;

import java.util.ArrayList;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.Registry;
import me.cworldstar.craftcrazesf.utils.Utils;

public class BasicMachineToken extends AToken {

	public BasicMachineToken(ItemGroup itemGroup, SlimefunItemStack item, ArrayList<NamespacedKey> researches, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe);
		this.researches = researches;
		this.addItemHandler(this.onTokenUse());
		// TODO Auto-generated constructor stub
	}

	@Override
	ItemUseHandler onTokenUse() {
		return (PlayerRightClickEvent e) -> {
			Player p = e.getPlayer(); // get the player
			World w = p.getWorld();
			Location L = p.getLocation();
			for (NamespacedKey researchId : researches) {
				Optional<Research> to_unlock = Research.getResearch(researchId);
				if (to_unlock.isPresent()) {
					to_unlock.get().unlock(p, true);
				} else {
					p.sendMessage(Utils.formatString("[Server]: &3If you see this message, research ID ".concat(researchId.toString()).concat(" is invalid.")));
				}
			}
			
			//-- fixes message spam
			String to_say = Utils.formatString(CraftCrazeSF.cfg.getString("lang.token-use"));
			Optional<SlimefunItem> sfItem = e.getSlimefunItem();
			if(sfItem.isPresent()) {
				to_say = to_say.replaceAll("\\{(token)\\}", CraftCrazeSF.cfg.getString("tokens.".concat(Registry.nameFromItem(sfItem.get())).concat(".name")));
			} else {
				to_say = "An error has occured.";
			}
			
			// send message
			p.sendMessage(to_say);
			
			ItemStack item = e.getItem();
			item.setAmount(item.getAmount() - 1);
			w.spawnParticle(Particle.FIREWORKS_SPARK, L.getX(), L.getY(), L.getZ(), 0, 0, 0, 0, 0, null);
			w.playSound(p, Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1.0F, 0);
			
		};
	}

	
	
}
