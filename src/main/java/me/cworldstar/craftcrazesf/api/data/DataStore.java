package me.cworldstar.craftcrazesf.api.data;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
	
	private Map<String, Object> data = new HashMap<String, Object>();
	
	public DataStore() {
			
	}
	
	public void setData(String entry, Object value) {
		data.put(entry, value);
	}
}
