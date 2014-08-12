package com.eitetu.minecraft.server.server;

import com.eitetu.minecraft.server.server.LegacyPingHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class ServerConnectionChannel extends ChannelInitializer<Channel> {
	public ServerConnectionChannel(ServerConnection serverConnection) {
	}

	@Override
	public void initChannel(Channel channel) throws Exception {
		try {
			channel.config().setOption(ChannelOption<T>.IP_TOS,
					Integer.valueOf(24));
		} catch (ChannelException localChannelException1) {
		}
		try {
			channel.config().setOption(ChannelOption.TCP_NODELAY,
					Boolean.valueOf(false));
		} catch (ChannelException localChannelException2) {
		}
		channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30))
				.addLast("legacy_query", new LegacyPingHandler(this.a))
				.addLast("splitter", new PacketSplitter())
				.addLast("decoder", new PacketDecoder(NetworkManager.h))
				.addLast("prepender", new PacketPrepender())
				.addLast("encoder", new PacketEncoder(NetworkManager.h));

		NetworkManager localNetworkManager = new NetworkManager(false);
		ServerConnection.a(this.a).add(localNetworkManager);
		paramChannel.pipeline().addLast("packet_handler", localNetworkManager);
		localNetworkManager.a(new HandshakeListener(ServerConnection.b(this.a),
				localNetworkManager));
	}

	public String getMotd() {

	}

}
