package net.hashsploit.clank.config.configs;

import com.google.gson.annotations.SerializedName;
import net.hashsploit.clank.server.muis.UniverseInfo;

import java.util.ArrayList;
import java.util.List;

public class MuisConfig extends MediusConfig {

    @SerializedName("universes")
    private List<UniverseInfo> universes = new ArrayList<>();

    public List<UniverseInfo> getUniverseInformation() {
        return universes;
    }
}
