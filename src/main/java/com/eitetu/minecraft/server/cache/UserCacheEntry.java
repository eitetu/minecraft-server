package com.eitetu.minecraft.server.cache;

import java.util.Date;

import com.eitetu.minecraft.server.authlib.GameProfile;

public class UserCacheEntry {

	private final GameProfile gameProfile;
	private final Date date;

	public UserCacheEntry(GameProfile gameProfile, Date date) {
		this.gameProfile = gameProfile;
		this.date = date;
	}

	public GameProfile getGameProfile() {
		return this.gameProfile;
	}

	public Date getDate() {
		return this.date;
	}
}
