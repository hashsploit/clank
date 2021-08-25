package net.hashsploit.clank.server.rpc;

import com.google.gson.annotations.SerializedName;
import net.hashsploit.clank.config.objects.ServerInfo;

public class RpcServerConfig extends ServerInfo {

	@SerializedName("encryption")
	private Encryption encryption = new Encryption();

	public Encryption getEncryption() {
		return encryption;
	}

	public static class Encryption {

		@SerializedName("enabled")
		private boolean enabled = false;

		@SerializedName("cert_chain")
		private String certChainFile = null;

		@SerializedName("private_key")
		private String privateKeyFile = null;

		public boolean isEnabled() {
			return enabled;
		}

		public String getCertChainFile() {
			return certChainFile;
		}

		public String getPrivateKeyFile() {
			return privateKeyFile;
		}
	}
}
