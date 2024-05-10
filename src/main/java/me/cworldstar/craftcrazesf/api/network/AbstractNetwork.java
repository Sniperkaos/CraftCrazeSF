package me.cworldstar.craftcrazesf.api.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import io.github.bakedlibs.dough.config.Config;
import me.cworldstar.craftcrazesf.CraftCrazeSF;
import me.cworldstar.craftcrazesf.utils.serialization.LocationSerializer;
import me.cworldstar.craftcrazesf.utils.serialization.NetworkObjectSerializer;

public abstract class AbstractNetwork {
	
	public static enum Networks {
			DARK_MATTER_NETWORK,
			UNDEFINED
	}
	
	public static Map<String, AbstractNetwork> networks = new HashMap<String, AbstractNetwork>();
	
	protected BukkitTask networkTask;
	
	public String identifier;
	
	protected Map<Location, NetworkObject> objects = new HashMap<Location, NetworkObject>();
	
	public static BlockFace[] faces = new BlockFace[] {
		BlockFace.DOWN,
		BlockFace.UP,
		BlockFace.EAST,
		BlockFace.WEST,
		BlockFace.SOUTH,
		BlockFace.NORTH
	};
	
	public AbstractNetwork(String identifier) {
		
		this.identifier = identifier;
		
		this.networkTask = new BukkitRunnable() {
			@Override
			public void run() {
				AbstractNetwork network = AbstractNetwork.this;
				if(network.tick()) {
					network.update();
				}
			}
			
		}.runTaskTimer(CraftCrazeSF.getMainPlugin(), 0, 1L); //-- might just suck, change later maybe
		AbstractNetwork.networks.put(identifier, this);
	}
	
	public void save() {
		Config to_write = new Config("data-storage/CraftCrazeSF/" + identifier +".yml");
		// look at the mob one ? its the same 
		to_write.clear();
		
		CraftCrazeSF.log(Level.INFO, "Saving network objects.");
		for(Entry<Location, NetworkObject> object : objects.entrySet()) {
			to_write.setValue(LocationSerializer.to(object.getKey()), NetworkObjectSerializer.to(object.getValue()));
		}

		to_write.save();
		CraftCrazeSF.log(Level.INFO, "Stored network objects saved!");
	}
	
	
	//-- manually tick network
	public static void tickNetwork(String identifier, Location L) {
		AbstractNetwork network = AbstractNetwork.networks.get(identifier);
		if(network != null) {
			if(network.tick()) {
				network.update();
			}
		}
	}
	
	public ArrayList<NetworkObject> getNearbyNetworkObjects(NetworkObject origin) {
		ArrayList<NetworkObject> objects = new ArrayList<NetworkObject>();
		Block block = origin.getBlock();
		for(BlockFace face : faces) { 

			Block relative = block.getRelative(face);
			if ( relative != null ) {
				if(this.objects.get(relative.getLocation()) != null) {
					objects.add(this.objects.get(relative.getLocation()));
				}
			}
		}
		return objects;
	}
	
	public static void addObjectToNetwork(String identifier, NetworkObject object) {
		AbstractNetwork network = AbstractNetwork.networks.get(identifier);
		if(network != null) {
			network.objects.put(object.getLocation(), object);
		} else {
			CraftCrazeSF.warn("Identifier " + identifier + " is an invalid network.");
		}
	}
	
	public abstract boolean tick();
	public abstract void update();
	
	public NetworkObject getNetworkObjectAt(Location L) {
		return this.objects.get(L);
	}
	
	
	public Map<Location, NetworkObject> getNetworkObjects() {
		return objects;
	} 
	
	public void addNetworkObject(Location l, NetworkObject object) {
		this.objects.put(l, object);
	}

	public static Optional<AbstractNetwork> Network(String network_id) {
		return Optional.ofNullable(AbstractNetwork.networks.get(network_id));
	}
}
