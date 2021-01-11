package net.hashsploit.clank.server.muis;

public class UniverseInfo {

	private String name;
	private String description;
	private String hostname;
	private int port;
	private int applicationId;
	private int universeId;

	public UniverseInfo(String name, String description, String hostname, int port, int applicationId, int universeId) {
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.port = port;
		this.applicationId = applicationId;
		this.universeId = universeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	
	public int getUniverseId() {
		return universeId;
	}

	public void setUniverseId(int universeId) {
		this.universeId = universeId;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("name=").append(name).append(", ");
		sb.append("description=").append(description).append(", ");
		sb.append("hostname=").append(hostname).append(", ");
		sb.append("port=").append(port).append(", ");
		sb.append("applicationId=").append(applicationId).append(", ");
		sb.append("universeId=").append(universeId);
		sb.append("]");
		return sb.toString();
	}

}
