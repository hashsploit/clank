package net.hashsploit.clank.config.configs;

import com.google.gson.annotations.SerializedName;
import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.objects.ServerInfo;

public class DmeConfig2 extends AbstractConfig {

    @SerializedName("parent_threads")
    private int parentThreads = 1;

    @SerializedName("child_threads")
    private int childThreads = 4;

    @SerializedName("timeout")
    private int timeout = 0;

    @SerializedName("capacity")
    private int capacity = 100;

    @SerializedName("udp_threads")
    private int udpThreads = 0;

    @SerializedName("udp_address")
    private String udpAddress = null;

    @SerializedName("udp_port")
    private int udpPort = 0;

    @SerializedName("rpc")
    private ServerInfo rpcConfig = new ServerInfo();

    public int getParentThreads() {
        return parentThreads;
    }

    public int getChildThreads() {
        return childThreads;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getUdpThreads() {
        return udpThreads;
    }

    public String getUdpAddress() {
        return udpAddress;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public ServerInfo getRpcConfig() {
        return rpcConfig;
    }

}
