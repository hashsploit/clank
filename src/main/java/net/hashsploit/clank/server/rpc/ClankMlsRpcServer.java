package net.hashsploit.clank.server.rpc;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.stub.StreamObserver;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.database.DbManager;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;

public class ClankMlsRpcServer extends AbstractRpcServer {

	private static final Logger logger = Logger.getLogger(ClankMlsRpcServer.class.getName());
	
	private final MediusLobbyServer mlsServer;

	public ClankMlsRpcServer(String address, int port, MediusLobbyServer mlsServer) throws IOException {
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
		
		/**
		 * Player login requested from MAS.
		 */
		@Override
		public void playerLogin(PlayerLoginRequest request, StreamObserver<PlayerLoginResponse> responseObserver) {
			responseObserver.onNext(processPlayerLogin(request));
			responseObserver.onCompleted();
		}
		
		@Override
		public void generateSessionKey(SessionKeyRequest request, StreamObserver<SessionKeyResponse> responseObserver) {
			responseObserver.onNext(processGenerateSessionKey(request));
			responseObserver.onCompleted();
		}
		
		
		
		
		// Methods

		private SessionKeyResponse processGenerateSessionKey(SessionKeyRequest request) {
			String key = Clank.getInstance().getDatabase().generateSessionKey();
			SessionKeyResponse response = SessionKeyResponse.newBuilder().setSessionKey(key).build();
			return response;
		}
		
		
		private PlayerUpdateResponse updatePlayer(PlayerUpdateRequest request) {
			PlayerUpdateResponse response = PlayerUpdateResponse.newBuilder().setSuccess(true).build();
			logger.info("### gRPC Player Update: [SessionKey: " + request.getMlsToken() + ", DmeWorldId: " + Integer.toString(request.getWorldId()) + ", Player status: " + request.getPlayerStatus().toString() + "]");
			MediusLobbyServer mlsServer = (MediusLobbyServer) (Clank.getInstance().getServer());
						
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

			
			logger.finest("RPC: updatePlayerStatusFromDme(): MlsToken: " + request.getMlsToken() + "\nWorld Id: " + request.getWorldId() + "\nPlayer Status: " + status.name());
			mlsServer.updatePlayerStatusFromDme(request.getMlsToken(), request.getWorldId(), status);
			
			return response;
		}

		private WorldUpdateResponse updateWorld(WorldUpdateRequest request) {
			WorldUpdateResponse response = WorldUpdateResponse.newBuilder().setSuccess(true).build();
			logger.info("### gRPC DmeWorld Update: [dmeWorldId: " + Integer.toString(request.getWorldId()) + ", status: " + request.getWorldStatus().toString() + "]");
			MediusWorldStatus status;
			switch (request.getWorldStatus()) {
			case CREATED:
				status = MediusWorldStatus.WORLD_PENDING_CREATION;
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
			
			MediusLobbyServer mlsServer = (MediusLobbyServer) (Clank.getInstance().getServer());
			
			logger.finest("RPC: updateWorld(): World Id: " + request.getWorldId() + "\nStatus: " + status.name());
			mlsServer.updateDmeWorldStatus(request.getWorldId(), status);
			return response;
		}
		
		private PlayerLoginResponse processPlayerLogin(PlayerLoginRequest request) {
			
			DbManager db = Clank.getInstance().getDatabase();
			
			int accountId = db.validateAccount(request.getSessionKey(), request.getUsername(), request.getPassword());
			PlayerLoginResponse response;
			String mlsToken = "00000000000000000000000000000000";

			if (accountId == 0) {
				response = PlayerLoginResponse.newBuilder().setSuccess(false).setAccountId(accountId).setMlsToken(mlsToken).build();
			}
			else {
				mlsToken = db.generateMlsToken(accountId);
				response = PlayerLoginResponse.newBuilder().setSuccess(true).setAccountId(accountId).setMlsToken(mlsToken).build();
			}
			
			return response;
		}

	}

}
