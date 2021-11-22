package net.hashsploit.clank.config.objects;

import com.google.gson.annotations.SerializedName;

public class ServerInfo {

	@SerializedName("address")
	private String address = null;

	@SerializedName("port")
	private int port = 0;

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

}
