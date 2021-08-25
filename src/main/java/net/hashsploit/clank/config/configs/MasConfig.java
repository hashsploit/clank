package net.hashsploit.clank.config.configs;

import com.google.gson.annotations.SerializedName;
import net.hashsploit.clank.config.objects.ServerInfo;
import net.hashsploit.clank.server.medius.objects.Channel;
import net.hashsploit.clank.server.medius.objects.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MasConfig extends MediusConfig {

    @SerializedName("mls")
    private ServerInfo mlsConfig = new ServerInfo();

    @SerializedName("whitelist")
    private Whitelist whitelist = new Whitelist();

    @SerializedName("channels")
    private List<Channel> channels = new ArrayList<>();

    @SerializedName("locations")
    private List<Location> locations = new ArrayList<>();

    public ServerInfo getMlsConfig() {
        return mlsConfig;
    }

    public Whitelist getWhitelist() {
        return whitelist;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public static class Whitelist {

        @SerializedName("enabled")
        private boolean enabled = false;

        @SerializedName("players")
        private HashMap<String, String> players = new HashMap<>();

        public boolean isEnabled() {
            return enabled;
        }

        public HashMap<String, String> getPlayers() {
            return players;
        }
    }
}
