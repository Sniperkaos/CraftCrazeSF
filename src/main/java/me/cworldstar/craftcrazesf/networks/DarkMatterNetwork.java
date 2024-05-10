package me.cworldstar.craftcrazesf.networks;

import java.util.Map.Entry;

import org.bukkit.Location;

import me.cworldstar.craftcrazesf.api.network.AbstractNetwork;
import me.cworldstar.craftcrazesf.api.network.NetworkObject;

public class DarkMatterNetwork extends AbstractNetwork {

	public DarkMatterNetwork(String identifier) {
		super(identifier);
	}

	@Override
	public boolean tick() {
		return true;
	}

	@Override
	public void update() {
		for(Entry<Location, NetworkObject> entry : this.objects.entrySet()) {
			Location loc = entry.getKey();
			NetworkObject object = entry.getValue();
			if(object.type() == NetworkObject.NetworkObjectType.CONSUMER || object.type() == NetworkObject.NetworkObjectType.PROVIDER) {
				object.process(this);
			}
		}
	}

}
