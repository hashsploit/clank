package net.hashsploit.clank.server.medius.objects;

import com.google.gson.annotations.SerializedName;

public class Channel {

	@SerializedName("id")
	private int id = 0;

	@SerializedName("name")
	private String name = "";

	@SerializedName("capacity")
	private int capacity = 256;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getCapacity() {
		return capacity;
	}
}
