package net.hashsploit.clank.server.rpc;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.stub.StreamObserver;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.common.MediusServer;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;

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
			logger.severe("Data received: " + request.getMlsToken());
			logger.severe("Data received: " + Integer.toString(request.getWorldId()));
			logger.severe("Data received: " + request.getPlayerStatus().toString());
			MediusServer mlsServer = (MediusServer) (Clank.getInstance().getServer());
			
			
			MediusPlayerStatus status;
			switch (request.getPlayerStatus()) {
			case DISCONNECTED:
				status = MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD;
				break;
			case CONNECTED:
				status = MediusPlayerStatus.MEDIUS_PLAYER_IN_GAME_WORLD;
				break;
			case STAGING:
				status = MediusPlayerStatus.MEDIUS_PLAYER_IN_GAME_WORLD;
				break;
			case ACTIVE:
				status = MediusPlayerStatus.MEDIUS_PLAYER_IN_GAME_WORLD;
				break;
			default:
				status = null;
			}
			
			mlsServer.getLogicHandler().updatePlayerStatusFromDme(request.getMlsToken(), request.getWorldId(), status);
			return response;
		}

		private WorldUpdateResponse updateWorld(WorldUpdateRequest request) {
			WorldUpdateResponse response = WorldUpdateResponse.newBuilder().setSuccess(true).build();
			logger.severe("gRPC: World ID!: " + Integer.toString(request.getWorldId()));
			logger.severe("gRPC: World UPDATE!: " + request.getWorldStatus().toString());
			
			MediusWorldStatus status;
			switch (request.getWorldStatus()) {
			case CREATED:
				status = MediusWorldStatus.WORLD_STAGING;
				break;
			case STAGING:
				status = MediusWorldStatus.WORLD_STAGING;
				break;
			case DESTROYED:
				status = MediusWorldStatus.WORLD_CLOSED;
				break;
			case ACTIVE:
				status = MediusWorldStatus.WORLD_ACTIVE;
				break;
			default:
				status = null;
			}
			
			MediusServer mlsServer = (MediusServer) (Clank.getInstance().getServer());
			mlsServer.getLogicHandler().updateDmeWorldStatus(request.getWorldId(), status);
			return response;
		}

	}

}
