package net.hashsploit.clank.server.rpc;

import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import net.hashsploit.clank.server.rpc.ClankMlsServiceGrpc.ClankMlsServiceBlockingStub;

public class ClankMasRpcClient {
	
	private static final Logger logger = Logger.getLogger(ClankMasRpcClient.class.getName());

	private final ManagedChannel grpcChannel;
	private ClankMlsServiceBlockingStub syncStub;

	public ClankMasRpcClient(String address, int port) {

		// FIXME: add support for encryption
		this.grpcChannel = NettyChannelBuilder.forTarget(String.format("%s:%s", address, Integer.toString(port))).usePlaintext().build();
		this.syncStub = ClankMlsServiceGrpc.newBlockingStub(grpcChannel);

	}

	public PlayerLoginResponse loginPlayer(String username, String password, String sessionKey) {
		PlayerLoginRequest request = PlayerLoginRequest.newBuilder().setUsername(username).setPassword(password).setSessionKey(sessionKey).build();
		PlayerLoginResponse response;
		try {
			response = syncStub.playerLogin(request);
		} catch (StatusRuntimeException e) {
			e.printStackTrace();
			logger.warning("RPC failed: " + e.getStatus());
			return null;
		}
		return response;
	}

	public String generateSessionKey() {
		SessionKeyRequest request = SessionKeyRequest.newBuilder().build();
		SessionKeyResponse response;
		try {
			response = syncStub.generateSessionKey(request);
		} catch (StatusRuntimeException e) {
			e.printStackTrace();
			logger.warning("RPC failed: " + e.getStatus());
			return null;
		}
		return response.getSessionKey();
	}

}
