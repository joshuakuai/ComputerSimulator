package edu.gwu.cs6461.sim.bridge;

import java.util.HashMap;
import java.util.Map;

public class HardwareData {
	Map<String, String> data = new HashMap<String, String>();

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public void put(String key, String val) {
		data.put(key, val);
		
	}

	public String get(String key){
		
		if (data.get(key)!=null) {
			return data.get(key);
		}
		
		return "";
		
	}
	
	
}
