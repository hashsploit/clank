package net.hashsploit.clank.server.medius.objects;

import com.google.gson.annotations.SerializedName;

public class Location {

	@SerializedName("id")
	private int id = 0;

	@SerializedName("name")
	private String name = "";

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
