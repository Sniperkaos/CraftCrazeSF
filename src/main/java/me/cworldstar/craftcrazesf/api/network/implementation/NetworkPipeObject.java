package me.cworldstar.craftcrazesf.api.network.implementation;

import org.bukkit.block.Block;

import me.cworldstar.craftcrazesf.api.network.AbstractNetwork;
import me.cworldstar.craftcrazesf.api.network.NetworkObject;

public class NetworkPipeObject extends NetworkObject {

	public NetworkPipeObject(Block b, String identifier, NetworkObjectType type) {
		super(b, identifier, type);
	}

	@Override
	public boolean process(AbstractNetwork network) {
		return true;
	}

}
