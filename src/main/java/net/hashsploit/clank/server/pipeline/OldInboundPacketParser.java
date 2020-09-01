package net.hashsploit.clank.server.pipeline;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.ISCERTMessage;
import net.hashsploit.clank.server.EncryptedDataPacket;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.utils.Utils;

public class OldInboundPacketParser extends ByteToMessageDecoder {
	
	private static final Logger logger = Logger.getLogger("");
	private final Client client;

	public OldInboundPacketParser(final Client client) {
		super();
		this.client = client;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.fine(ctx.channel().remoteAddress() + ": channel active");
	}


	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.fine(ctx.channel().remoteAddress() + ": channel inactive");
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		try {
			final ByteBuffer buffer = toNioBuffer(in);

			final byte[] data = new byte[buffer.remaining()];
			buffer.get(data);

			// No valid packet is < 3 or > 2048 bytes, drop the connection
			if (data.length < 3 || data.length > 2048) {
				ctx.close();
				return;
			}

			// Check if id is valid, otherwise drop the connection
			boolean validId = false;
			for (final RTPacketId id : RTPacketId.values()) {
				if (data[0] == id.getByte()) {
					validId = true;
				} else if (data[0] >= (byte) 0x80 && (byte) (data[0] - (byte)0x80) == id.getByte()) {
					validId = true;
				}
			}

			if (!validId) {
				ctx.close();
				return;
			}

			logger.finest("RAW: " + Utils.bytesToString(data));

			
			// FIXME: this is bad
			if (Clank.getInstance().getConfig().isEncryptionEnabled()) {
				// Handle splitting multiple Packet ID's on packets
				final List<ISCERTMessage> packets = processData(ctx, data);

				if (packets == null || packets.isEmpty()) {
					ctx.close();
					return;
				}

				// Add all packets to the output
				out.addAll(packets);
				
				// Testing
				for (Object obj : out) {
					if (obj instanceof DataPacket) {
						DataPacket packet = (DataPacket) obj;
						logger.finest(String.format("Packet " + packet.toString() + " [id:%s len:%d data:%s]", Utils.byteToString(packet.getId().getByte()), packet.getLength(), Utils.bytesToString(packet.getPayload())));
					}
				}
				
			} else {
				final List<DataPacket> packets = new ArrayList<DataPacket>();
				
				RTPacketId rtid = null;
				
				for (RTPacketId p : RTPacketId.values()) {
					if (p.getByte() == data[0]) {
						rtid = p;
						break;
					}
				}
				
				byte[] pktData = new byte[data.length - (1 + 2)];
				System.arraycopy(data, 1 + 2, pktData, 0, pktData.length);
				
				packets.add(new DataPacket(rtid, pktData));
				// Add all packets to the output
				out.addAll(packets);
				
				ctx.flush();
				//ctx.fireChannelActive();
				ctx.fireChannelRead(out);
			}

			// ctx.channel().writeAndFlush("Test\n".getBytes()).await();
			// ctx.channel().close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			ReferenceCountUtil.release(in);
		}
	}

	/**
	 * Find if there are multiple Medius data packets sent in one physical packet.
	 * 
	 * @param ctx
	 * @param data
	 * @return
	 */
	private static List<ISCERTMessage> processData(ChannelHandlerContext ctx, byte[] data) {
		final List<ISCERTMessage> packets = new ArrayList<ISCERTMessage>();

		int index = 0;

		try {
			while (index < data.length) {
				final byte id = data[index + 0];
				int length = (data[index + 1] | data[index + 2]) & 0xFF;

				// If the packet is >= 0x80 it is encrypted, take into consideration the
				// checksum.
				if ((id & 0xFF) >= 0x80 && length > 0) {
					length += 4;
				}

				// Invalid packet, the length bytes are longer than the data sent.
				if (length > data.length) {
					return null;
				}

				byte[] finalData = new byte[length];
				int offset = 0;

				if (length > 0) {
					// ID(1) + Length(2)
					offset += 1 + 2;
				}

				// If the id is >= 0x80 there is a checksum.
				if ((id & 0xFF) >= 0x80) {
					logger.warning("ENCRYPTED DATA PACKET");
					// Checksum(4)
					offset += 4;
					byte[] chksum = new byte[] { data[index + 3], data[index + 4], data[index + 5], data[index + 6] };
					System.arraycopy(data, index + offset, finalData, 0, finalData.length - 4);
					RTPacketId rtid = null;
					
					for (RTPacketId p : RTPacketId.values()) {
						if (p.getByte() == id) {
							rtid = p;
							break;
						}
					}
					
					packets.add(new EncryptedDataPacket(rtid, chksum, finalData));
				} else {
					logger.warning("PLAIN DATA PACKET");
					System.arraycopy(data, index + offset, finalData, 0, finalData.length);
					
					RTPacketId rtid = null;
					
					for (RTPacketId p : RTPacketId.values()) {
						if (p.getByte() == id) {
							rtid = p;
							break;
						}
					}
					
					packets.add(new DataPacket(rtid, finalData));
				}

				index += length + 3;
			}
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}

		return packets;
	}

	/**
	 * Convert Netty ByteBuff to ByteBuffer
	 * @param buffer
	 * @return
	 */
	private static ByteBuffer toNioBuffer(final ByteBuf buffer) {
		if (buffer.isDirect()) {
			return buffer.nioBuffer();
		}
		final byte[] bytes = new byte[buffer.readableBytes()];
		buffer.getBytes(buffer.readerIndex(), bytes);
		return ByteBuffer.wrap(bytes);
	}

}
