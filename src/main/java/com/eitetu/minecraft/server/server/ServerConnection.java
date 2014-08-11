package com.eitetu.minecraft.server.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.eitetu.minecraft.server.MinecraftServer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ServerConnection {
	private final MinecraftServer server;
	public boolean open;

	private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(0,
			new ThreadFactoryBuilder().setNameFormat("Netty IO #%d")
					.setDaemon(true).build());

	private final List<ChannelFuture> channels = Collections.synchronizedList(new ArrayList<ChannelFuture>());
	private final List<NetworkManager> networkManagers = Collections.synchronizedList(new ArrayList<NetworkManager>());

	public ServerConnection(MinecraftServer server) {
		this.server = server;
		this.open = true;
	}

	public void bind(InetAddress paramInetAddress, int paramInt) {
		synchronized (this.channels) {
			this.channels.add(((ServerBootstrap) ((ServerBootstrap) new ServerBootstrap()
					.channel(NioServerSocketChannel.class))
					.childHandler(new ServerConnectionChannel(this)).group(nioEventLoopGroup)
					.localAddress(paramInetAddress, paramInt)).bind()
					.syncUninterruptibly());
		}
	}

	public void close() {
		this.open = false;

		for (ChannelFuture localChannelFuture : this.channels)
			localChannelFuture.channel().close().syncUninterruptibly();
	}

	public MinecraftServer getServer() {
		return server;
	}

	public void disconnect() {
		synchronized (this.networkManagers) {
			Iterator<NetworkManager> iter = this.networkManagers.iterator();
			NetworkManager item;

			while (iter.hasNext()) {
				item = iter.next();

				if (!(item.isConnected())) {
					iter.remove();

					if (item.f() != null)
						item.getPacketListener().a(item.f());
					else if (item.getPacketListener() != null)
						item.getPacketListener().a(new ChatComponentText("Disconnected"));
				} else {
					try {
						item.a();
					} catch (Exception localException) {
						if (item.c()) {
							localObject1 = CrashReport.a(localException,
									"Ticking memory connection");
							CrashReportSystemDetails localCrashReportSystemDetails = ((CrashReport) localObject1)
									.a("Ticking connection");

							localCrashReportSystemDetails.a("Connection",
									new CrashReportServerConnection(this,
											localNetworkManager));

							throw new ReportedException(
									(CrashReport) localObject1);
						}
						b.warn("Failed to handle packet for " + item.getSocketAddress(),
								localException);
						Object localObject1 = new ChatComponentText("Internal server error");
						item
								.handle(new PacketPlayOutKickDisconnect(
										(IChatBaseComponent) localObject1),
										new GenericFutureListener[] { new ServerConnectionFuture(
												this,
												localNetworkManager,
												(ChatComponentText) localObject1) });

						item.g();
					}
				}
			}
		}
	}
}
