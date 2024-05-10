package me.cworldstar.craftcrazesf.api.network;

import org.bukkit.Location;
import org.bukkit.block.Block;

import me.cworldstar.craftcrazesf.api.network.AbstractNetwork.Networks;
import me.cworldstar.craftcrazesf.utils.Utils;

public abstract class NetworkObject {
	
	public enum NetworkObjectType {
			
			CONSUMER,
			PROVIDER,
			TRANSMITTER
			
	}
	
	private Block block;
	private int capacity = 0;
	private int max_capacity = 0;
	private String network_identifier;
	private NetworkObjectType object_type;
	
	public abstract boolean process(AbstractNetwork network);
	
	public NetworkObject(Block b, String identifier, NetworkObjectType type) {
		this.block = b;
		this.object_type = type;
		this.network_identifier = identifier;
		AbstractNetwork.addObjectToNetwork(identifier, this);
	}
	
	public String identifier() {
		return this.network_identifier;
	}
	
	public void setMaxCapacity(int cap) {
		this.max_capacity = cap;
	}
	
	public NetworkObjectType type() {
		return this.object_type;
	}
	
	public void setVolume(int volume) {
		this.capacity = Utils.clamp(0, max_capacity, volume);
	}
	
	public int getVolume() {
		return this.capacity;
	} 
	
	public void changeVolume(int volume, int operation) {
		if(operation == 0) {
			operation = 1;
		}
		this.capacity += Utils.clamp(0, max_capacity, volume * operation);
	}
	
	public Location getLocation() {
		return this.block.getLocation();
	}
	
	public Block getBlock() {
		return this.block;
	}

	public static NetworkObject fromType(Networks type) {
		switch(type) {
		
			case DARK_MATTER_NETWORK:
			
		
			default:
				return null;
		}
	}
	
}
