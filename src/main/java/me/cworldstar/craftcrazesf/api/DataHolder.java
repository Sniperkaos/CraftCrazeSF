package me.cworldstar.craftcrazesf.api;

import org.bukkit.Location;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

public interface DataHolder {
	
	default void save(String path, Location l, double value) {
		BlockStorage.addBlockInfo(l.getBlock(), path, Double.toString(value));
	}
	
	default double load(String path, Location l) {
		String block_info = BlockStorage.getBlockInfoAsJson(l.getBlock());
		if(block_info == null) {
			// doesnt exist return default
			return 0.0;
		}
		JsonObject element = JsonParser.parseString(block_info).getAsJsonObject();
		JsonElement part = element.get(path);
		if(part == null) {
			return 0.0;

		}
		
		double to_get = part.getAsDouble();
		return to_get;
	}
}
