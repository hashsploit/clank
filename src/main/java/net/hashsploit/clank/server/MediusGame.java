package net.hashsploit.clank.server;

<<<<<<< HEAD
import net.hashsploit.clank.server.medius.MediusConstants;
=======
import java.util.ArrayList;
import java.util.logging.Logger;

>>>>>>> d8d9b511f87f3f3afa999c30d78fabb346d0687b
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.serializers.CreateGameOneRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusGame {
<<<<<<< HEAD
=======
	private static final Logger logger = Logger.getLogger(MediusGame.class.getName());
>>>>>>> d8d9b511f87f3f3afa999c30d78fabb346d0687b

	private int worldId;
	private CreateGameOneRequest req;
	private int playerCount = 1; // TODO: Update this when DME sends a "PlayerConnected" gRPC
	
	private byte[] stats = new byte[MediusConstants.GAMESTATS_MAXLEN.value];
	private byte[] genericField1;
	private byte[] genericField2;
	private byte[] genericField3;
	private byte[] genericField4;
	private byte[] genericField5;
	private byte[] genericField6;
	private byte[] genericField7;
	private byte[] genericField8;
	
//	WORLD_INACTIVE(0)
//	WORLD_STAGING(1)
//	WORLD_ACTIVE(2)
//	WORLD_CLOSED(3)
//	WORLD_PENDING_CREATION(4)
//	WORLD_PENDING_CONNECT_TO_GAME(5)
	private MediusWorldStatus worldStatus;
	
	//private ArrayList<String> players = new ArrayList<String>();

	public MediusGame(int worldId, CreateGameOneRequest req) {
		this.worldId = worldId;
		this.req = req;
		this.worldStatus = MediusWorldStatus.WORLD_PENDING_CREATION;
	}

	public CreateGameOneRequest getReqPacket() {
		return req;
	}

	public short getPlayerCount() {
		return (short) playerCount;
	}

	public byte[] getStats() {
		//return Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
		return stats;
	}
	
	public synchronized void updateStats(byte[] stats) {
		this.stats = stats;
	}
	
	
	public byte[] getGenericField1() {
		return genericField1;
	}
	
	public synchronized void setGenericField1(byte[] genericField1) {
		this.genericField1 = genericField1;
	}
	
	public byte[] getGenericField2() {
		return genericField2;
	}
	
	public synchronized void setGenericField2(byte[] genericField2) {
		this.genericField2 = genericField2;
	}
	
	public byte[] getGenericField3() {
		return genericField3;
	}
	
	public synchronized void setGenericField3(byte[] genericField3) {
		this.genericField3 = genericField3;
	}
	
	public byte[] getGenericField4() {
		return genericField4;
	}
	
	public synchronized void setGenericField4(byte[] genericField4) {
		this.genericField4 = genericField4;
	}
	
	public byte[] getGenericField5() {
		return genericField5;
	}
	
	public synchronized void setGenericField5(byte[] genericField5) {
		this.genericField5 = genericField5;
	}

	public byte[] getGenericField6() {
		return genericField6;
	}
	
	public synchronized void setGenericField6(byte[] genericField6) {
		this.genericField6 = genericField6;
	}
	
	public byte[] getGenericField7() {
		return genericField7;
	}
	
	public synchronized void setGenericField7(byte[] genericField7) {
		this.genericField7 = genericField7;
	}
	
	public byte[] getGenericField8() {
		return genericField8;
	}
	
	public synchronized void setGenericField8(byte[] genericField8) {
		this.genericField8 = genericField8;
	}
	
	public byte[] getWorldStatusBytes() {
		return Utils.intToBytesLittle(worldStatus.getValue());
	}

	public MediusWorldStatus getWorldStatus() {
		return worldStatus;
	}

	public int getWorldId() {
		return worldId;
	}

<<<<<<< HEAD
	public synchronized void updateStatus(MediusWorldStatus worldStatus) {
=======
	public void updateStatus(MediusWorldStatus worldStatus) {
		logger.info("Updating world status: [dmeWorldId: " + worldId + ", newWorldStatus: " + worldStatus.toString() + ", playercount: " + players.size() + "]");
>>>>>>> d8d9b511f87f3f3afa999c30d78fabb346d0687b
		this.worldStatus = worldStatus;
	}

}
