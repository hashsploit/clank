package net.hashsploit.clank.server.rpc;

import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import net.hashsploit.clank.server.rpc.ClankMlsServiceGrpc.ClankMlsServiceBlockingStub;
import net.hashsploit.clank.server.rpc.PlayerUpdateRequest.PlayerStatus;
import net.hashsploit.clank.server.rpc.WorldUpdateRequest.WorldStatus;

public class ClankDmeRpcClient {
	private static final Logger logger = Logger.getLogger(ClankDmeRpcClient.class.getName());

	private final ManagedChannel grpcChannel;
	private ClankMlsServiceBlockingStub asyncStub;

	public ClankDmeRpcClient(String address, int port) {

		// FIXME: add support for encryption
		this.grpcChannel = NettyChannelBuilder.forTarget(String.format("%s:%s", address, Integer.toString(port))).usePlaintext().build();
		this.asyncStub = ClankMlsServiceGrpc.newBlockingStub(grpcChannel);

	}

	public void updatePlayer(int accountId, int worldId, PlayerStatus playerStatus) {
		PlayerUpdateRequest request = PlayerUpdateRequest.newBuilder().setAccountId(accountId).setWorldId(worldId).setPlayerStatus(playerStatus).build();
		PlayerUpdateResponse response;
		try {
			response = asyncStub.playerUpdate(request);
		} catch (StatusRuntimeException e) {
			e.printStackTrace();
			logger.warning("RPC failed: " + e.getStatus());
			return;
		}
	}

	public void updateWorld(int worldId, WorldStatus worldStatus) {
		WorldUpdateRequest request = WorldUpdateRequest.newBuilder().setWorldId(worldId).setWorldStatus(worldStatus).build();
		WorldUpdateResponse response;
		try {
			response = asyncStub.worldUpdate(request);
		} catch (StatusRuntimeException e) {
			e.printStackTrace();
			logger.warning("RPC failed: " + e.getStatus());
			return;
		}
	}

}
