package com.eitetu.minecraft.server.util.authlib;

import com.eitetu.minecraft.server.util.authlib.minecraft.MinecraftSessionService;

public abstract interface AuthenticationService {
	public abstract UserAuthentication createUserAuthentication(Agent paramAgent);
	public abstract MinecraftSessionService createMinecraftSessionService();
	public abstract GameProfileRepository createProfileRepository();
}