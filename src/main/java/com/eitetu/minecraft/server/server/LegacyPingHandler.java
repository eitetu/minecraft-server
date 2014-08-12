package com.eitetu.minecraft.server.server;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eitetu.minecraft.server.MinecraftServer;
import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LegacyPingHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LogManager.getLogger();
	private ServerConnection serverConnection;

	public LegacyPingHandler(ServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}

	@Override
	public void channelRead(ChannelHandlerContext channelHandlerContext, Object buffer) {
		ByteBuf localByteBuf1 = (ByteBuf) buffer;

		localByteBuf1.markReaderIndex();

		int i = 1;
		try {
			if (localByteBuf1.readUnsignedByte() != 254) {
				return;
			}

			InetSocketAddress localInetSocketAddress = (InetSocketAddress) channelHandlerContext
					.channel().remoteAddress();
			MinecraftServer localMinecraftServer = this.serverConnection.getServer();

			int readableByte = localByteBuf1.readableBytes();
			String str1;
			switch (readableByte) {
			case 0:
				logger.debug("Ping: (<1.3.x) from {}:{}",
						new Object[] {
								localInetSocketAddress.getAddress(),
								Integer.valueOf(localInetSocketAddress
										.getPort()) });

				str1 = String.format(
						"%s§%d§%d",
						new Object[] { localMinecraftServer.getMotd(),
								Integer.valueOf(localMinecraftServer.getPlayerCount()),
								Integer.valueOf(localMinecraftServer.getMaxPlayers()) });
				send(channelHandlerContext, stringToByteBuf(str1));

				break;
			case 1:
				if (localByteBuf1.readUnsignedByte() != 1) {
					return;
				}

				logger.debug("Ping: (1.4-1.5.x) from {}:{}",
						new Object[] {
								localInetSocketAddress.getAddress(),
								Integer.valueOf(localInetSocketAddress
										.getPort()) });

				str1 = String.format(
						"",
						new Object[] { Integer.valueOf(127),
								localMinecraftServer.getVersion(),
								localMinecraftServer.getMotd(),
								Integer.valueOf(localMinecraftServer.getPlayerCount()),
								Integer.valueOf(localMinecraftServer.getMaxPlayers()) });
				send(channelHandlerContext, stringToByteBuf(str1));

				break;
			default:
				boolean bool = localByteBuf1.readUnsignedByte() == 1;
				bool &= localByteBuf1.readUnsignedByte() == 250;
				bool &= "MC|PingHost".equals(new String(localByteBuf1
						.readBytes(localByteBuf1.readShort() * 2).array(),
						Charsets.UTF_16BE));
				int k = localByteBuf1.readUnsignedShort();
				bool &= localByteBuf1.readUnsignedByte() >= 73;
				bool &= 3 + localByteBuf1.readBytes(
						localByteBuf1.readShort() * 2).array().length + 4 == k;
				bool &= localByteBuf1.readInt() <= 65535;
				bool &= localByteBuf1.readableBytes() == 0;

				if (!(bool)) {
					return;
				}

				logger.debug("Ping: (1.6) from {}:{}",
						new Object[] {
								localInetSocketAddress.getAddress(),
								Integer.valueOf(localInetSocketAddress
										.getPort()) });

				String str2 = String.format(
						"",
						new Object[] { Integer.valueOf(127),
								localMinecraftServer.getVersion(),
								localMinecraftServer.getMotd(),
								Integer.valueOf(localMinecraftServer.getPlayerCount()),
								Integer.valueOf(localMinecraftServer.getMaxPlayers()) });
				ByteBuf localByteBuf2 = stringToByteBuf(str2);
				try {
					send(channelHandlerContext, localByteBuf2);
				} finally {
					localByteBuf2.release();
				}
			}

			localByteBuf1.release();
			i = 0;
		} catch (RuntimeException localRuntimeException) {
		} finally {
			if (i != 0) {
				localByteBuf1.resetReaderIndex();
				channelHandlerContext.channel().pipeline().remove("legacy_query");
				channelHandlerContext.fireChannelRead(buffer);
			}
		}
	}

	private void send(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
		channelHandlerContext
			.pipeline()
			.firstContext()
			.writeAndFlush(byteBuf)
			.addListener(ChannelFutureListener.CLOSE);
	}

	private ByteBuf stringToByteBuf(String paramString) {
		ByteBuf localByteBuf = Unpooled.buffer();
		localByteBuf.writeByte(255);

		char[] arrayOfChar1 = paramString.toCharArray();
		localByteBuf.writeShort(arrayOfChar1.length);
		for (int k : arrayOfChar1) {
			localByteBuf.writeChar(k);
		}

		return localByteBuf;
	}
}
