package me.cworldstar.craftcrazesf.utils.serialization;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.cworldstar.craftcrazesf.api.network.NetworkObject;

public class NetworkObjectSerializer {
	public static String to(NetworkObject o) {
		JsonObject serialized =  new JsonObject();
		serialized.addProperty("volume", o.getVolume());
		serialized.addProperty("location", LocationSerializer.to(o.getLocation()));
		serialized.addProperty("network", o.identifier());
		
		return serialized.getAsString();
	}
	
	public static JsonObject from(String s) {
		return JsonParser.parseString(s).getAsJsonObject();
	}
}
