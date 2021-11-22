package net.hashsploit.clank.config.configs;

import com.google.gson.annotations.SerializedName;
import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.objects.ServerInfo;

import java.util.ArrayList;
import java.util.List;

public class MediusConfig2 extends AbstractConfig {

    @SerializedName("encryption")
    private boolean encrypted = false;

    @SerializedName("parent_threads")
    private int parentThreads = 1;

    @SerializedName("child_threads")
    private int childThreads = 4;

    @SerializedName("capacity")
    private int capacity = 100;

    @SerializedName("nat")
    private ServerInfo natConfig = new ServerInfo();

    @SerializedName("rpc")
    private ServerInfo rpcConfig = new ServerInfo();

    @SerializedName("usage_policy")
    private String usagePolicy = "";
    
    @SerializedName("privacy_policy")
    private String privacyPolicy = "";
    
    @SerializedName("announcements")
    private List<String> announcements = new ArrayList<>();

    public boolean isEncrypted() {
        return encrypted;
    }

    public int getParentThreads() {
        return parentThreads;
    }

    public int getChildThreads() {
        return childThreads;
    }

    public int getCapacity() {
        return capacity;
    }

    public ServerInfo getNatConfig() {
        return natConfig;
    }

    public ServerInfo getRpcConfig() {
        return rpcConfig;
    }
    
	public String getUsagePolicy() {
		return usagePolicy;
	}

	public String getPrivacyPolicy() {
		return privacyPolicy;
	}

    public List<String> getAnnouncements() {
        return announcements;
    }
}
