package net.hashsploit.clank.rt.serializers;

import java.util.Arrays;

import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;

public class RT_ClientAppBroadcast extends RTMessage {

	private short sourceOrTarget;
	private byte[] appPayload;

	public RT_ClientAppBroadcast(byte[] packetData) {
		super(RtMessageId.CLIENT_APP_BROADCAST, Arrays.copyOfRange(packetData, 3, packetData.length));

		byte[] payload = this.getPayload();
		// FIXME: don't assume
		sourceOrTarget = Utils.bytesToShortLittle(payload[0], payload[1]);
		appPayload = Arrays.copyOfRange(payload, 2, payload.length);
	}

	@Override
	public String toString() {
		return "ClientAppBroadcast: \n" + "sourceOrTarget: " + Utils.bytesToHex(Utils.shortToBytesLittle(sourceOrTarget)) + '\n' + "appPayload: " + Utils.bytesToHex(appPayload);
	}

	public synchronized byte[] getAppPayload() {
		return appPayload;
	}

	public synchronized void setAppPayload(byte[] appPayload) {
		this.appPayload = appPayload;
	}

}
