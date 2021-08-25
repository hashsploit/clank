package net.hashsploit.clank.config;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.objects.ServerInfo;

import java.util.logging.Level;

public class AbstractConfig extends ServerInfo {

    private JsonObject jsonObject;

    @SerializedName("mode")
    private EmulationMode emulationMode;

    @SerializedName("log_level")
    private String logLevel = "INFO";

    @SerializedName("file_log_level")
    private String fileLogLevel = null;

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public EmulationMode getEmulationMode() {
        return emulationMode;
    }

    // TODO: Log4J
    public Level getLogLevel() {
        return logLevel != null ? Level.parse(logLevel) : null;
    }

    // TODO: Log4J
    public Level getFileLogLevel() {
        return fileLogLevel != null ? Level.parse(fileLogLevel) : null;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
