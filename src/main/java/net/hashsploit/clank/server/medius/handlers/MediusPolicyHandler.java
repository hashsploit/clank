package net.hashsploit.clank.server.medius.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MediusConfig2;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.PolicyRequest;
import net.hashsploit.clank.server.medius.serializers.PolicyResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusPolicyHandler extends MediusPacketHandler {

	private static final Logger logger = Logger.getLogger(MediusPolicyHandler.class.getName());
	
	private PolicyRequest reqPacket;
	private PolicyResponse respPacket;

	public MediusPolicyHandler() {
		super(MediusMessageType.Policy, MediusMessageType.PolicyResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new PolicyRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		
		
		
		// FIXME: use serializer PolicyResponse and implement both types of policy responses.
		String policyString = ((MediusConfig2) Clank.getInstance().getConfig()).getUsagePolicy();
		List<MediusMessage> mediusMessages = new ArrayList<MediusMessage>();
		
		if (policyString != null && policyString.length() > 0) {
			final int totalPolicyLength = policyString.length();
			final int maxTotalPolicyLength = MediusConstants.FULLPOLICY_MAXLEN.value-1; // leave one byte of room for the null terminator
			final int maxSegmentedPolicyLength = MediusConstants.POLICY_MAXLEN.value-1; // leave one byte of room for the null terminator
			
			if (totalPolicyLength > maxTotalPolicyLength) {
				logger.warning(String.format("Policy in configuration file is %d (longer than the max %d characters)! Truncating ...", totalPolicyLength, maxTotalPolicyLength));
				policyString = policyString.substring(0, maxTotalPolicyLength);
			}
			
			final List<String> segmentedPolicyStrings = new ArrayList<String>();
			
			if (policyString.length() > maxSegmentedPolicyLength) {
				for (int i=0; i<policyString.length(); i+=maxSegmentedPolicyLength) {
					int remaining = i+maxSegmentedPolicyLength;
					if (remaining > policyString.length()) {
						remaining = policyString.length();
					}
					
					String segment = policyString.substring(i, remaining);
					
					segmentedPolicyStrings.add(segment);
				}
			} else {
				segmentedPolicyStrings.add(policyString);
			}
			
			
			// TODO: when queues are added, create a for-loop to iterate through 64-byte length chunks of the policy set in the config.
			// enqueue each of the chunks respectively.
			for (int i=0; i<segmentedPolicyStrings.size(); i++) {
				
				final byte[] padding = new byte[3];
				final MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
				final byte[] policy = Utils.buildByteArrayFromString(segmentedPolicyStrings.get(i), maxSegmentedPolicyLength+1);
				final boolean endOfList = segmentedPolicyStrings.size()-1 == i;
				
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				try {
					outputStream.write(reqPacket.getMessageId()); // medius message uuid
					outputStream.write(padding); // padding
					outputStream.write(Utils.intToBytes(callbackStatus.getValue())); // medius callback status
					outputStream.write(policy); // segmented policy length
					outputStream.write(Utils.intToBytesLittle(endOfList ? 1 : 0)); // end of the policy list true/false (int)
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				logger.finest(Utils.generateDebugPacketString(MediusPolicyHandler.class.getName(),
					new String[] {
						"messageId",
						"padding",
						"callbackStatus",
						"policy",
						"endOfList",
					},
					new String[] {
						"0x" + Utils.bytesToHex(reqPacket.getMessageId()),
						"0x" + Utils.bytesToHex(padding),
						callbackStatus.name() + " (0x" + Integer.toHexString(callbackStatus.getValue() & 0xFF) + ")",
						new String(policy).replaceAll(Pattern.quote("\n"), "\\\\n").replaceAll(Pattern.quote("\r"), "\\\\r"),
						endOfList + " (0x" + Integer.toHexString(endOfList ? 1 : 0) + ")"
					}
				));
				mediusMessages.add(new MediusMessage(responseType, outputStream.toByteArray()));
			}
		} else {
			
			final byte[] padding = new byte[3];
			final MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
			final byte[] policy = Utils.padByteArray(new byte[0], MediusConstants.POLICY_MAXLEN.value);
			final boolean endOfList = true;
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				outputStream.write(reqPacket.getMessageId()); // medius message uuid
				outputStream.write(padding); // padding
				outputStream.write(Utils.intToBytes(callbackStatus.getValue())); // medius callback status
				outputStream.write(policy); // segmented policy length
				outputStream.write(Utils.intToBytesLittle(endOfList ? 1 : 0)); // end of the policy list true/false (int)
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			logger.finest(Utils.generateDebugPacketString(MediusPolicyHandler.class.getName(),
					new String[] {
						"messageId",
						"padding",
						"callbackStatus",
						"policy",
						"endOfList",
					},
					new String[] {
						"0x" + Utils.bytesToHex(reqPacket.getMessageId()),
						"0x" + Utils.bytesToHex(padding),
						callbackStatus.name() + " (0x" + Utils.intToHex(callbackStatus.getValue() & 0xFF) + ")",
						Utils.bytesToStringClean(policy),
						endOfList + " (0x" + Utils.intToHex(endOfList ? 1 : 0) + ")"
					}
				));
			
			mediusMessages.add(new MediusMessage(responseType, outputStream.toByteArray()));
		}
		
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		
		for (final MediusMessage mediusMessage : mediusMessages) {
			response.add(mediusMessage);
		}
		
		return response;

	}
}
