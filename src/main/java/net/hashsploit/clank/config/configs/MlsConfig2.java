package net.hashsploit.clank.config.configs;

import com.google.gson.annotations.SerializedName;
import net.hashsploit.clank.config.objects.DatabaseInfo;
import net.hashsploit.clank.config.objects.ServerInfo;
import net.hashsploit.clank.server.medius.objects.Channel;
import net.hashsploit.clank.server.medius.objects.Location;
import net.hashsploit.clank.server.rpc.RpcServerConfig;

import java.util.ArrayList;
import java.util.List;

public class MlsConfig2 extends MediusConfig2 {

	@SerializedName("dme")
	private ServerInfo dmeConfig = new ServerInfo();

	@SerializedName("operators")
	private List<String> operators = new ArrayList<>();

	@SerializedName("channels")
	private List<Channel> channels = new ArrayList<>();

	@SerializedName("locations")
	private List<Location> locations = new ArrayList<>();

	@SerializedName("database")
	private DatabaseInfo databaseInfo = new DatabaseInfo();

	@SerializedName("rpc_server")
	private RpcServerConfig rpcServerConfig = new RpcServerConfig();

	public ServerInfo getDmeConfig() {
		return dmeConfig;
	}

	public List<String> getOperators() {
		return operators;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public DatabaseInfo getDatabaseInfo() {
		return databaseInfo;
	}

	public RpcServerConfig getRpcServerConfig() {
		return rpcServerConfig;
	}
}
