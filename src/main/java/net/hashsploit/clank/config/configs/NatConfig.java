package net.hashsploit.clank.config.configs;

import com.google.gson.annotations.SerializedName;
import net.hashsploit.clank.config.AbstractConfig;

public class NatConfig extends AbstractConfig {

    @SerializedName("udp_threads")
    private int udpThreads = 0;

    public int getUdpThreads() {
        return udpThreads;
    }
}
