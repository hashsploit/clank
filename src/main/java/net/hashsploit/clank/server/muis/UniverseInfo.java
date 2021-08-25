package net.hashsploit.clank.server.muis;

import com.google.gson.annotations.SerializedName;

public class UniverseInfo {

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("hostname")
	private String hostname;

	@SerializedName("port")
	private int port;

	@SerializedName("application_id")
	private int applicationId;

	@SerializedName("universe_id")
	private int universeId;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public int getUniverseId() {
		return universeId;
	}

	@Override
	public String toString() {
		return "UniverseInfo{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", hostname='" + hostname + '\'' +
				", port=" + port +
				", applicationId=" + applicationId +
				", universeId=" + universeId +
				'}';
	}
}
