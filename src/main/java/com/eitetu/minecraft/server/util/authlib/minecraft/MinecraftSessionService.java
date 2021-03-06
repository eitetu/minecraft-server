package com.eitetu.minecraft.server.util.authlib.minecraft;

import java.util.Map;

import com.eitetu.minecraft.server.util.authlib.GameProfile;
import com.eitetu.minecraft.server.util.authlib.exceptions.AuthenticationException;
import com.eitetu.minecraft.server.util.authlib.exceptions.AuthenticationUnavailableException;

public abstract interface MinecraftSessionService {
	public abstract void joinServer(GameProfile gameProfile, String paramString1, String paramString2) throws AuthenticationException;
	public abstract GameProfile hasJoinedServer(GameProfile gameProfile, String paramString) throws AuthenticationUnavailableException;
	public abstract Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(GameProfile paramGameProfile, boolean paramBoolean);
	public abstract GameProfile fillProfileProperties(GameProfile paramGameProfile, boolean paramBoolean);
}