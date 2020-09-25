package net.hashsploit.clank;

import org.json.JSONObject;

public abstract class Config {
	
	private JSONObject json;
	
	public Config(JSONObject json) {
		this.json = json;
	}
	
	public JSONObject getJSON() {
		return json;
	}
	
}
