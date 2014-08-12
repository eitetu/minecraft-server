package com.eitetu.minecraft.server;

import java.io.IOException;
import java.net.Proxy;
import java.util.UUID;

import org.hibernate.cfg.Configuration;

import com.eitetu.minecraft.server.cache.UserCache;
import com.eitetu.minecraft.server.server.ServerConnection;

import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import net.minecraft.server.ChunkCoordinates;
import net.minecraft.server.CommandDispatcher;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ICommandHandler;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.IMojangStatistics;
import net.minecraft.server.MojangStatisticsGenerator;
import net.minecraft.server.PlayerList;
import net.minecraft.server.ServerPing;
import net.minecraft.server.ThreadServerApplication;
import net.minecraft.server.World;
import net.minecraft.util.com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

public class MinecraftServer implements ICommandListener, Runnable, IMojangStatistics {
	private static final String SERVER_VERSION = "1.7.9";

	private final UserCache userCache;
	private static MinecraftServer instance;
	protected final Proxy proxy;

	private final ServerConnection serverConnection;
	private final ServerPing serverPing = new ServerPing();
	private final ICommandHandler commandDispatcher;
	private PlayerList players;
	private String motd;

	public MinecraftServer(OptionSet options, Proxy proxy) { // Eitetu-CraftBukkit Server - signature file -> OptionSet
		this.userCache = new UserCache(this, a);

		instance = this;
		proxy = proxy;

		// this.universe = file1; // CraftBukkit
		this.serverConnection = new ServerConnection(this);
		this.commandDispatcher = new CommandDispatcher();
		// this.convertable = new WorldLoaderServer(file1); // CraftBukkit -
		// moved to DedicatedServer.init
		this.T = new YggdrasilAuthenticationService(proxy, UUID.randomUUID()
				.toString());
		this.U = this.T.createMinecraftSessionService();
		this.W = this.T.createProfileRepository();
		// CraftBukkit start
		this.options = options;
		// Try to see if we're actually running in a terminal, disable jline if
		// not
		if (System.console() == null) {
			System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
			org.bukkit.craftbukkit.Main.useJline = false;
		}

		try {
			this.reader = new ConsoleReader(System.in, System.out);
			this.reader.setExpandEvents(false); // Avoid parsing exceptions for
												// uncommonly used event
												// designators
		} catch (Throwable e) {
			try {
				// Try again with jline disabled for Windows users without C++
				// 2008 Redistributable
				System.setProperty("jline.terminal",
						"jline.UnsupportedTerminal");
				System.setProperty("user.language", "en");
				org.bukkit.craftbukkit.Main.useJline = false;
				this.reader = new ConsoleReader(System.in, System.out);
				this.reader.setExpandEvents(false);
			} catch (IOException ex) {
				i.warn((String) null, ex);
			}
		}
		Runtime.getRuntime().addShutdownHook(
				new org.bukkit.craftbukkit.util.ServerShutdownThread(this));

		primaryThread = new ThreadServerApplication(this, "Server thread"); // Moved
																			// from
																			// main
	}

	public int getPlayerCount() {
		return players.getPlayerCount();
	}

	public int getMaxPlayers() {
		return players.getMaxPlayers();
	}

	public String getMotd() {
		return this.motd;
	}

	public String getVersion() {
		return SERVER_VERSION;
	}

	@Override
	public void a(MojangStatisticsGenerator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void b(MojangStatisticsGenerator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getSnooperEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean a(int arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ChunkCoordinates getChunkCoordinates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IChatBaseComponent getScoreboardDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public World getWorld() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendMessage(IChatBaseComponent arg0) {
		// TODO Auto-generated method stub

	}

}
