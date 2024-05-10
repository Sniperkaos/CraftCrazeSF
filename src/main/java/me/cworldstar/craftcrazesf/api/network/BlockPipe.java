package me.cworldstar.craftcrazesf.api.network;

import java.util.Optional;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.api.DataHolder;
import me.cworldstar.craftcrazesf.api.network.NetworkObject.NetworkObjectType;
import me.cworldstar.craftcrazesf.api.network.implementation.NetworkPipeObject;
import me.cworldstar.craftcrazesf.utils.Utils;

public abstract class BlockPipe extends SlimefunItem implements DataHolder {
	
	public BlockPipe(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String network_id) {
		super(itemGroup, item, recipeType, recipe);
		
		addItemHandler(new BlockPlaceHandler(disenchantable) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				Optional<AbstractNetwork> network = AbstractNetwork.Network(network_id);
				if(network.isEmpty()) {
					e.getPlayer().sendMessage(Utils.formatString("&6[CraftCrazeSF]: &fThe network is not properly attached to this pipe. If you see this, contact a developer!"));
					CraftCrazeSF.warn("Network " + network_id + " does not exist!");
					e.setCancelled(true);
					return;
				}
				
				//-- create network pipe
				NetworkPipeObject this_object = new NetworkPipeObject(e.getBlock(), network_id, NetworkObjectType.TRANSMITTER);
				AbstractNetwork.addObjectToNetwork(network_id, this_object);
				
			}
			
		});
	}

}
