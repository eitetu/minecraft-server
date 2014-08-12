package com.eitetu.minecraft.server.util.authlib;


public abstract interface ProfileLookupCallback {
	public abstract void onProfileLookupSucceeded(GameProfile paramGameProfile);
	public abstract void onProfileLookupFailed(GameProfile paramGameProfile, Exception paramException);
}
