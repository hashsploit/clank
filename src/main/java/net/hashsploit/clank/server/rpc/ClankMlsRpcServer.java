package net.hashsploit.clank.server.rpc;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.stub.StreamObserver;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.common.MediusServer;

public class ClankMlsRpcServer extends AbstractRpcServer {

	private static final Logger logger = Logger.getLogger(ClankMlsRpcServer.class.getName());
	
	private final MediusServer mlsServer;

	public ClankMlsRpcServer(String address, int port, MediusServer mlsServer) throws IOException {
		super(address, port);
		this.mlsServer = mlsServer;
		addService(new ClankMlsService());
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
	}

	private static class ClankMlsService extends ClankMlsServiceGrpc.ClankMlsServiceImplBase {

		/**
		 * Player update requested from DME.
		 */
		@Override
		public void playerUpdate(PlayerUpdateRequest request, StreamObserver<PlayerUpdateResponse> responseObserver) {
			responseObserver.onNext(updatePlayer(request));
			responseObserver.onCompleted();
		}

		/**
		 * World update requested from DME.
		 */
		@Override
		public void worldUpdate(WorldUpdateRequest request, StreamObserver<WorldUpdateResponse> responseObserver) {
			responseObserver.onNext(updateWorld(request));
			responseObserver.onCompleted();
		}

		private PlayerUpdateResponse updatePlayer(PlayerUpdateRequest request) {
			PlayerUpdateResponse response = PlayerUpdateResponse.newBuilder().setSuccess(true).build();
			logger.info("Data received: " + Integer.toString(request.getAccountId()));
			MediusServer mlsServer = (MediusServer) (Clank.getInstance().getServer());
			mlsServer.getLogicHandler().updatePlayerStatusFromDme(request.getAccountId(), request.getWorldId(), request.getPlayerStatus());
			return response;
		}

		private WorldUpdateResponse updateWorld(WorldUpdateRequest request) {
			WorldUpdateResponse response = WorldUpdateResponse.newBuilder().setSuccess(true).build();
			logger.severe("gRPC: World ID!: " + Integer.toString(request.getWorldStatusValue()));
			logger.severe("gRPC: World UPDATE!: " + Integer.toString(request.getWorldStatusValue()));
			
			MediusServer mlsServer = (MediusServer) (Clank.getInstance().getServer());
			mlsServer.getLogicHandler().updateDmeWorldStatus(request.getWorldId(), request.getWorldStatus());
			
			return response;
		}

	}

}
