package net.hashsploit.clank.server.rpc;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.stub.StreamObserver;

public class ClankMlsRpcServer extends AbstractRpcServer {

	private static final Logger logger = Logger.getLogger(ClankMlsRpcServer.class.getName());

	public ClankMlsRpcServer(String address, int port) throws IOException {
		super(address, port);
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
			return response;
		}

		private WorldUpdateResponse updateWorld(WorldUpdateRequest request) {
			WorldUpdateResponse response = WorldUpdateResponse.newBuilder().setSuccess(true).build();
			return response;
		}

	}

}
